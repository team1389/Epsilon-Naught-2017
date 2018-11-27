package operation;

import java.util.function.Supplier;

import com.team1389.hardware.controls.ControlBoard;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.system.Subsystem;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.OctocanumSystem;
import com.team1389.system.drive.OctocanumSystem.DriveMode;

import robot.RobotSoftware;
import systems.ClimberSystem;
import systems.FancyLightSystem;
import systems.GearIntakeSystem;
import systems.TeleopGearIntakeSystem;
import systems.TeleopHopperSystem;
import watchers.DebugDash;

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
		OctocanumSystem drive = setupDrive();
		GearIntakeSystem gearIntake = setupGearIntake(drive.getDriveModeTracker());
		Subsystem climbing = setUpClimbing();
		Subsystem lights = new FancyLightSystem(robot.lights.getColorOutput(), gearIntake::hasGear,
				gearIntake::getState);
		DebugDash.getInstance().watch(lights, gearIntake);
		manager = new SystemManager(drive, climbing, lights, gearIntake, setupHopper());
		manager.init();
	}

	/**
	 * 
	 * @return a new OctocanumSystem
	 */
	private OctocanumSystem setupDrive() {
		return new OctocanumSystem(robot.voltageDrive, robot.pistons, robot.gyroInput, controls.driveXAxis(),
				controls.driveYAxis(), controls.driveYaw(), controls.driveTrim(), controls.driveModeBtn(),
				controls.driveModifierBtn());
	}

	/**
	 * 
	 * @return a new GearIntakeSystem
	 */
	private GearIntakeSystem setupGearIntake(Supplier<DriveMode> driveMode) {
		TeleopGearIntakeSystem Supplier = new TeleopGearIntakeSystem(robot.armAngle, robot.armVel,
				robot.armElevator.getVoltageController(), robot.gearIntake.getVoltageController(), robot.gearBeamBreak,
				robot.gearIntakeCurrent, driveMode, controls.aButton(), controls.bButton(), controls.xButton(),
				controls.yButton(), controls.leftStickYAxis(), controls.rightTrigger(), controls.setRumble(),
				controls.startButton());
		return Supplier;
	}

	/**
	 * 
	 * @return a new ClimberSystem
	 */
	private ClimberSystem setUpClimbing() {
		return new ClimberSystem(controls.leftTrigger(), robot.climberVoltage);
	}

	private TeleopHopperSystem setupHopper() {

		return new TeleopHopperSystem(robot.dumperPistonRight.getDigitalOut(), robot.dumperPistonLeft.getDigitalOut(),
				robot.dumperPistonRight.getDigitalOut(), new DigitalIn(() -> true), controls.upDPad(),
				controls.downDPad());
	}

	/**
	 * periodically calls update method in all subsystems
	 */
	public void periodic() {
		manager.update();
		if (controls.backButton().get()) {
			robot.zeroAngle();
		}
	}

}
