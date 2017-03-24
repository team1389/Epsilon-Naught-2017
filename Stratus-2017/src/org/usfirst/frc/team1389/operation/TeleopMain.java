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
import org.usfirst.frc.team1389.systems.TeleopHopperSystem;
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
	 * @param robot container of all ohm streams
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
		manager = new SystemManager(drive, ballIntake, climbing, lights, gearIntake, setupHopper());
		manager.init();
		DebugDash.getInstance().watch(robot.armAngleAbsolute.getWatchable("absolute pos"));
	}

	/**
	 * 
	 * @return a new OctoMecanumSystem
	 */
	private Subsystem setupDrive() {
		return new OctoMecanumSystem(robot.voltageDrive, robot.pistons, robot.gyroInput, controls.driveXAxis(),
				controls.driveYAxis(), controls.driveYaw(), controls.driveTrim(), controls.driveModeBtn(),
				controls.driveModifierBtn());
	}

	/**
	 * 
	 * @return a new GearIntakeSystem
	 */
	private GearIntakeSystem setupGearIntake() {
		TeleopGearIntakeSystem Supplier = new TeleopGearIntakeSystem(robot.armAngleAbsolute, robot.armVel,
				robot.armElevator.getVoltageOutput(), robot.gearIntake.getVoltageOutput(), robot.gearIntakeCurrent,
				controls.intakeGearBtn(), controls.prepareArmBtn(), controls.placeGearBtn(), controls.stowArmBtn(),
				controls.armAngleAxis(), controls.gearRumble());
		return Supplier;
	}

	/**
	 * 
	 * @param state supplier of state of GearIntake
	 * @return a new BallIntakeSystem
	 */
	private Subsystem setUpBallIntake(Supplier<GearIntakeSystem.State> state) {
		return new BallIntakeSystem(controls.ballIntakeBtn(), state, robot.ballIntake.getVoltageOutput());
	}

	/**
	 * 
	 * @return a new ClimberSystem
	 */
	private ClimberSystem setUpClimbing() {
		return new ClimberSystem(controls.climberThrottle(), robot.climber.getVoltageOutput());
	}

	private TeleopHopperSystem setupHopper() {
		return new TeleopHopperSystem(robot.dumperPiston.getDigitalOut(), robot.dumperPiston.getDigitalOut(),
				new DigitalIn(() -> true), controls.dumpHopperBtn(), controls.resetHopperBtn());
	}

	/**
	 * periodically calls update method in all subsystems
	 */
	public void periodic() {
		manager.update();
	}

}
