package org.usfirst.frc.team1389.systems;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.SmoothSetController;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class GearIntakeSystem extends Subsystem {
	private AngleIn<Position> armAngle;
	private AngleIn<Speed> armVel;
	private SmoothSetController armPositionPID;
	private PercentOut intakeVoltageOut;
	private RangeIn<Value> intakeCurrentDraw;
	private DigitalIn intakeGearButton;
	private DigitalIn prepareGearButton;
	private DigitalIn placeGearButton;

	public GearIntakeSystem(AngleIn<Position> armAngle, AngleIn<Speed> armVel, PercentOut armVoltage,
			PercentOut intakeVoltage, RangeIn<Value> intakeCurrent, DigitalIn intakeGearButton,
			DigitalIn prepareGearButton, DigitalIn placeGearButton) {
		this.armAngle = armAngle;
		this.armVel = armVel;
		this.armPositionPID = new SmoothSetController(new PIDConstants(.06, 1E-6, 0), 400, 400, 70, armAngle, armVel,
				armVoltage);
		this.intakeVoltageOut = intakeVoltage;
		this.intakeCurrentDraw = intakeCurrent;
		this.intakeGearButton = intakeGearButton;
		this.prepareGearButton = prepareGearButton;
		this.placeGearButton = placeGearButton;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(armAngle.getWatchable("angle"), armVel.getWatchable("velocity"),
				intakeVoltageOut.getWatchable("intake voltage"), intakeCurrentDraw.getWatchable("intake current"));
	}

	@Override
	public String getName() {
		return "GearSystem";
	}

	@Override
	public void init() {
		schedule(stowArm());
	}

	@Override
	public void update() {
		if (intakeGearButton.get()) {
			schedule(intakeGear());
		}
		if (prepareGearButton.get()) {
			schedule(preparePlaceGear());
		}
		if (placeGearButton.get()) {
			schedule(placeGear());
		}
		armPositionPID.update();
	}

	private enum IntakePosition {
		DOWN(10), PLACING(90), CARRYING(110), STOWED(125), PLACED(10);
		public final double angle;

		private IntakePosition(double angle) {
			this.angle = angle;
		}
	}

	public Command preparePlaceGear() {
		return new SetAngle(IntakePosition.PLACING.angle);
	}

	public Command placeGear() {
		return CommandUtil.combineSequential(enablePID(true), new SetAngleSlow(IntakePosition.PLACED.angle, false),
				CommandUtil.createCommand(() -> isNear(70, 1)), setIntake(.25),
				CommandUtil.createCommand(() -> isNear(IntakePosition.PLACED.angle, 10)), setIntake(0));
	}

	public Command intakeGear() {
		return CommandUtil.combineSequential(enablePID(true), new SetAngle(IntakePosition.DOWN), new IntakeUntilGear(),
				setIntake(-.5), new SetAngle(IntakePosition.CARRYING), setIntake(0));
	}

	public Command stowArm() {
		return CommandUtil.combineSequential(enablePID(true), new SetAngle(IntakePosition.STOWED));
	}

	public Command enablePID(boolean val) {
		return CommandUtil.createCommand(() -> armPositionPID.setEnabled(val));
	}

	public Command setIntake(double voltage) {
		return CommandUtil.createCommand(() -> intakeVoltageOut.set(voltage));
	}

	public class IntakeUntilGear extends Command {

		@Override
		protected void initialize() {
			intakeVoltageOut.set(-1);
		}

		@Override
		protected boolean execute() {
			return intakeCurrentDraw.get() > 55;
		}

		@Override
		protected void done() {
			intakeVoltageOut.set(0);
		}

	}

	public class SetAngle extends Command {
		double position;
		boolean waitForFinish;

		public SetAngle(double position, boolean waitForFinish) {
			this.position = position;
			this.waitForFinish = waitForFinish;
		}

		public SetAngle(IntakePosition pos, boolean waitForFinish) {
			this(pos.angle, waitForFinish);
		}

		public SetAngle(double position) {
			this(position, true);
		}

		public SetAngle(IntakePosition pos) {
			this(pos, true);
		}

		@Override
		protected void initialize() {
			armPositionPID.setSetpoint(position);
		}

		@Override
		protected boolean execute() {
			return !waitForFinish || isNear(position, 1);
		}
	}

	public class SetAngleSlow extends SetAngle {

		public SetAngleSlow(double position, boolean waitForFinish) {
			super(position, waitForFinish);
		}

		@Override
		protected void initialize() {
			armPositionPID.setSetpoint(position, 150, 150, 70);
		}

	}

	public boolean isNear(double angle, double nearTolerance) {
		return Math.abs(angle - armAngle.get()) < nearTolerance;
	}
	/*
	 * @Override public void schedule(Command command) {
	 * super.schedule(CommandUtil.combineSequential(CommandUtil.createCommand(scheduler::isFinished)
	 * , command)); }
	 */

}
