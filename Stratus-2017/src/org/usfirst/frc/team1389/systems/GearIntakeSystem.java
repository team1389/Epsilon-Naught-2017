package org.usfirst.frc.team1389.systems;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.configuration.PIDInput;
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
/**
 * The System responsible for controlling the gear intake
 * @author Quunii
 *
 */
public class GearIntakeSystem extends Subsystem {
	private AngleIn<Position> armAngle;
	private AngleIn<Speed> armVel;
	private SmoothSetController armPositionPID;
	protected PercentOut intakeVoltageOut;
	protected RangeIn<Value> intakeCurrentDraw;
	protected State state;
	/**
	 * defaults to Stowed State
	 * @param armAngle encoder value of angle of arm
	 * @param armVel speed of arm
	 * @param armVoltage applies voltage to arm motors
	 * @param intakeVoltage applies voltage to intake motors
	 * @param intakeCurrent curret draw on intake
	 */
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
				new EnumInfo("intake state", () -> state), scheduler, new PIDInput("arm pid tuner", new PIDConstants(.06, 1E-6, 0), false, (p) -> armPositionPID.setPID(
						p)));
	}

	@Override
	protected void schedule(Command command) {
		super.schedule(command);
	}

	@Override
	public String getName() {
		return "GearIntake";
	}

	@Override
	public void init() {
		//enterState(State.STOWED);
	}

	/**
	 * updates smooth set controller
	 */
	@Override
	public void update() {
		armPositionPID.update();
	}
	/**
	 * 
	 * @return current state
	 */
	public State getState() {
		return this.state;
	}
	
	private enum Angle {
		DOWN(0), PLACING(80), CARRYING(110), STOWED(120), PLACED(30);
		public final double angle;

		private Angle(double angle) {
			this.angle = angle;
		}
	}

	public enum State {
		INTAKING, CARRYING, STOWED, PLACING, ALIGNING;
	}
	/**
	 * 
	 * @return command that aligns gear with peg
	 */
	public Command preparePlaceGear() {
		return CommandUtil
				.combineSequential(enablePID(true), setIntake(0), new SetAngle(Angle.PLACING),
						setStateCommand(State.ALIGNING))
					.setName("prepare-pos");
	}
	/**
	 * 
	 * @return command that places gear on peg
	 */
	public Command placeGear() {
		return CommandUtil
				.combineSequential(enablePID(true), setStateCommand(State.PLACING), setIntake(0),
						new SetAngleSlow(Angle.PLACED.angle, false), CommandUtil.createCommand(() -> isNear(70, 1)),
						setIntake(.25), CommandUtil.createCommand(() -> isNear(Angle.PLACED.angle, 10)), setIntake(0))
					.setName("placing");
	}
/**
 * 
 * @return commmand that intakes a gear, and carries it
 */
	public Command intakeGear() {
		return CommandUtil.combineSequential(CommandUtil
				.combineSequential(enablePID(true), new SetAngle(Angle.DOWN), setStateCommand(State.INTAKING))
					.setName("lowering"),
				new IntakeUntilGear().setName("awaiting"), carryGear());
	}
	/**
	 * 
	 * @return command that carries a gear
	 */
	public Command carryGear() {
		return CommandUtil.combineSequential(enablePID(true), setIntake(-.5), new SetAngle(Angle.CARRYING),
				setStateCommand(State.CARRYING), setIntake(0).setName("raising-to-carry"));
	}
	/**
	 * 
	 * @return a command that stows the arm
	 */
	public Command stowArm() {
		return CommandUtil
				.combineSequential(enablePID(true), setIntake(0), new SetAngle(Angle.STOWED),
						setStateCommand(State.STOWED), enablePID(false))
					.setName("stowing");
	}
	/**
	 * 
	 * @param val whether to enable the Smooth Set controller
	 * @return Command to enable or disable Smooth Set Controller
	 */
	public Command enablePID(boolean val) {
		return CommandUtil.createCommand(() -> armPositionPID.setEnabled(val));
	}
	/**
	 * 
	 * @param voltage  the voltage to apply to intake motors
	 * @return Command that applies voltage to intake motors
	 */
	public Command setIntake(double voltage) {
		return CommandUtil.createCommand(() -> intakeVoltageOut.set(voltage));
	}
	/**
	 * 
	 * @param s the State to switch to
	 * @return a command to set state
	 */
	public Command setStateCommand(State s) {
		return CommandUtil.createCommand(() -> setState(s));
	}
	/**
	 * sets state based on parameter
	 * @param s the state to be set to
	 */
	public void setState(State s) {
		state = s;
	}
	/**
	 * 
	 * @param desired State that corresponds with desired command
	 * schedules command based on state passed in 
	 */
	public void enterState(State desired) {
		scheduler.cancelAll();
		intakeVoltageOut.set(0);
		scheduler.schedule(getEnterStateCommand(desired));
	}
	/**
	 * 
	 * @param desired The desired State
	 * @return a command corresponding with passed in State
	 */
	public Command getEnterStateCommand(State desired) {
		switch (desired) {
		case STOWED:
			return (stowArm());

		case INTAKING:
			return (intakeGear());

		case PLACING:
			return (placeGear());

		case ALIGNING:
			return (preparePlaceGear());

		case CARRYING:
			return (carryGear());

		default:
			return null;
		}

	}
	
	/**
	 * intakes until a gear is detected
	 * @author Quunii
	 *
	 */
	public class IntakeUntilGear extends Command {
		/**
		 * reverses motors
		 */
		@Override
		protected void initialize() {
			intakeVoltageOut.set(-1);
		}
		/**
		 * stops motors when current draw spikes, signifying intaken gear
		 */
		@Override
		protected boolean execute() {
			return intakeCurrentDraw.get() > 55;
		}
		/**
		 * sets voltage to 0 when done
		 */
		@Override
		protected void done() {
			intakeVoltageOut.set(0);
		}

		public String getName() {
			return "awaiting-gear";
		}

	}
	/**
	 * sets an angle for arm to reach
	 * @author Quunii
	 *
	 */
	public class SetAngle extends Command {
		double position;
		boolean waitForFinish;
		/**
		 * 
		 * @param position desired angle
		 * @param waitForFinish whether to wait for this command to end before running another
		 */
		public SetAngle(double position, boolean waitForFinish) {
			this.position = position;
			this.waitForFinish = waitForFinish;
		}
		/**
		 * 
		 * @param pos desired angle
		 * @param waitForFinish  whether to wait for this command to end before running another
		 */
		public SetAngle(Angle pos, boolean waitForFinish) {
			this(pos.angle, waitForFinish);
		}
		/**
		 * assumes that command should be executed before running another
		 * @param position desired angle
		 */
		public SetAngle(double position) {
			this(position, true);
		}
		/**
		 * assumes that command should be executed before running another
		 * @param pos desired angle
		 */
		public SetAngle(Angle pos) {
			this(pos, true);
		}
		/**
		 * pass Smooth Set Controller the position to reach
		 */
		@Override
		protected void initialize() {
			armPositionPID.setSetpoint(position);
		}
		/**
		 * command is complete if waitForFinish is not true, or the arm is within a degree of the desired angle
		 */
		@Override
		protected boolean execute() {
			return !waitForFinish || isNear(position, 1);
		}
	}
	/**
	 * slower version of SetAngle
	 * @author Quunii
	 *
	 */
	public class SetAngleSlow extends SetAngle {

		public SetAngleSlow(double position, boolean waitForFinish) {
			super(position, waitForFinish);
		}

		@Override
		protected void initialize() {
			armPositionPID.setSetpoint(position, 150, 150, 70);
		}

	}
	/**
	 * 
	 * @param angle goal angle
	 * @param nearTolerance Margin of Error
	 * @return whether the arm is close enough to the desired angle
	 */
	public boolean isNear(double angle, double nearTolerance) {
		return Math.abs(angle - armAngle.get()) < nearTolerance;
	}
	/*
	 * @Override public void schedule(Command command) {
	 * super.schedule(CommandUtil.combineSequential(CommandUtil.createCommand(scheduler::isFinished)
	 * , command)); }
	 */

}
