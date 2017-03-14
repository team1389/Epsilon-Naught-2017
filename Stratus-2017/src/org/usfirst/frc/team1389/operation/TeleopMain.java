package org.usfirst.frc.team1389.operation;

import java.util.function.Supplier;

import org.usfirst.frc.team1389.robot.RobotSoftware;
import org.usfirst.frc.team1389.robot.controls.ControlBoard;
import org.usfirst.frc.team1389.systems.BallIntakeSystem;
import org.usfirst.frc.team1389.systems.ClimberSystem;
import org.usfirst.frc.team1389.systems.FancyLightSystem;
import org.usfirst.frc.team1389.systems.GearIntakeSystem;
import org.usfirst.frc.team1389.systems.OctoMecanumSystem;
import org.usfirst.frc.team1389.systems.TeleopGearIntakeSystem;
import org.usfirst.frc.team1389.watchers.DebugDash;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.system.Subsystem;
import com.team1389.system.SystemManager;

/**
 * system manager
 * 
 * @author Quunii
 *
 */
public class TeleopMain {
	SystemManager manager;
	ControlBoard controls;
	RobotSoftware robot;
	DigitalIn timeRunning;

	/**
	 * 
	 * @param robot
	 *            container of all ohm streams
	 */
	public TeleopMain(RobotSoftware robot) {
		this.robot = robot;
	}

	/**
	 * initializes systems, and adds them to a list of systems to update
	 */
	public void init() {
		controls = ControlBoard.getInstance();
		GearIntakeSystem gearIntake = setupGearIntake();
		Subsystem drive = setupDrive();
		Subsystem ballIntake = setUpBallIntake(() -> GearIntakeSystem.State.STOWED);
		Subsystem climbing = setUpClimbing();
		Subsystem lights = new FancyLightSystem(robot.lights.getColorOutput(), () -> GearIntakeSystem.State.STOWED);
		manager = new SystemManager(drive, ballIntake, climbing, lights, gearIntake);
		manager.init();
		DebugDash.getInstance().watch(robot.armAngleAbsolute.getWatchable("absolute pos"));
	}

	/**
	 * 
	 * @return a new OctoMecanumSystem
	 */
	private Subsystem setupDrive() {
		return new OctoMecanumSystem(robot.voltageDrive, robot.pistons, robot.gyroInput, controls.i_xAxis.get(),
				controls.i_yAxis.get(), controls.twistAxis, controls.trimAxis, controls.i_thumb.get(),
				controls.i_trigger.get());
	}

	/**
	 * 
	 * @return a new GearIntakeSystem
	 */
	private GearIntakeSystem setupGearIntake() {
		TeleopGearIntakeSystem Supplier = new TeleopGearIntakeSystem(robot.armAngleAbsolute,
				robot.armVel /* could be a problem here */, robot.armElevator.getVoltageOutput(),
				robot.gearIntake.getVoltageOutput(), robot.gearIntakeCurrent, controls.i_aButton.get(),
				controls.i_yButton.get(), controls.i_xButton.get(), controls.i_bButton.get(),
				controls.i_leftVertAxis.get(), robot.rumble);
		return Supplier;
	}

	/**
	 * 
	 * @param state
	 *            supplier of state of GearIntake
	 * @return a new BallIntakeSystem
	 */
	private Subsystem setUpBallIntake(Supplier<GearIntakeSystem.State> state) {
		return new BallIntakeSystem(controls.rightBumper, state, robot.ballIntake.getVoltageOutput());
	}

	/**
	 * 
	 * @return a new ClimberSystem
	 */
	private ClimberSystem setUpClimbing() {
		return new ClimberSystem(controls.i_leftTriggerAxis.get(), robot.climber.getVoltageOutput());
	}

	/**
	 * periodically calls update method in all subsystems
	 */
	public void periodic() {
		manager.update();
	}

}
