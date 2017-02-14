package org.usfirst.frc.team1389.systems;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.SmoothSetController;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.EnumInfo;

public class GearIntakeSystem extends Subsystem {
	private AngleIn<Position> armAngle;
	private AngleIn<Speed> armVel;
	private SmoothSetController armPositionPID;
	private PercentOut intakeVoltageOut;
	private RangeIn<Value> intakeCurrentDraw;
	protected State state;

	public GearIntakeSystem(AngleIn<Position> armAngle, AngleIn<Speed> armVel, PercentOut armVoltage,
			PercentOut intakeVoltage, RangeIn<Value> intakeCurrent) {
		this.armAngle = armAngle;
		this.armVel = armVel;
		this.armPositionPID = new SmoothSetController(new PIDConstants(.06, 1E-6, 0), 400, 400, 150, armAngle, armVel,
				armVoltage);
		setState(State.STOWED);
		this.intakeCurrentDraw = intakeCurrent;
		this.intakeVoltageOut = intakeVoltage;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(armAngle.getWatchable("angle"), armVel.getWatchable("velocity"),
				intakeVoltageOut.getWatchable("intake voltage"), intakeCurrentDraw.getWatchable("intake current"),
				new EnumInfo("Name", () -> state));
	}

	@Override
	public String getName() {
		return "GearSystem";
	}

	@Override
	public void init() {
		enterState(State.STOWED);
	}

	@Override
	public void update() {
		armPositionPID.update();
	}

	private enum Angle {
		DOWN(10), PLACING(80), CARRYING(110), STOWED(125), PLACED(30);
		public final double angle;

		private Angle(double angle) {
			this.angle = angle;
		}
	}

	public enum State {
		INTAKING, CARRYING, STOWED, PLACING, ALIGNING;
	}

	public Command preparePlaceGear() {
		return CommandUtil.combineSequential(setIntake(0), new SetAngle(Angle.PLACING),
				setStateCommand(State.ALIGNING));
	}

	public Command placeGear() {
		return CommandUtil.combineSequential(setStateCommand(State.PLACING), enablePID(true), setIntake(0),
				new SetAngleSlow(Angle.PLACED.angle, false), CommandUtil.createCommand(() -> isNear(70, 1)),
				setIntake(.25), CommandUtil.createCommand(() -> isNear(Angle.PLACED.angle, 10)), setIntake(0));
	}

	public Command intakeGear() {
		return CommandUtil.combineSequential(enablePID(true), new SetAngle(Angle.DOWN), setStateCommand(State.INTAKING),
				new IntakeUntilGear(), setIntake(-.5), new SetAngle(Angle.CARRYING), setStateCommand(State.CARRYING),
				setIntake(0));
	}

	public Command stowArm() {
		return CommandUtil.combineSequential(enablePID(true), setIntake(0), new SetAngle(Angle.STOWED),
				setStateCommand(State.STOWED));
	}

	public Command enablePID(boolean val) {
		return CommandUtil.createCommand(() -> armPositionPID.setEnabled(val));
	}

	public Command setIntake(double voltage) {
		return CommandUtil.createCommand(() -> intakeVoltageOut.set(voltage));
	}

	public Command setStateCommand(State s) {
		return CommandUtil.createCommand(() -> setState(s));
	}

	public void setState(State s) {
		state = s;
	}

	public void enterState(State desired) {
		switch (desired) {
		case STOWED:
			schedule(stowArm());
			break;
		case INTAKING:
			schedule(intakeGear());
			break;
		case PLACING:
			schedule(placeGear());
			break;
		case ALIGNING:
			schedule(preparePlaceGear());
		default:
			break;
		}

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

		public SetAngle(Angle pos, boolean waitForFinish) {
			this(pos.angle, waitForFinish);
		}

		public SetAngle(double position) {
			this(position, true);
		}

		public SetAngle(Angle pos) {
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
