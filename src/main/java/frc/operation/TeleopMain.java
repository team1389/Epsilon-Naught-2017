package frc.operation;

import java.util.function.Supplier;

import com.team1389.hardware.controls.ControlBoard;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.system.Subsystem;
import com.team1389.system.SystemManager;
import com.team1389.system.drive.OctocanumSystem;
import com.team1389.system.drive.TwinStickMecanumSystem;
import com.team1389.system.drive.OctocanumSystem.DriveMode;
import com.team1389.watch.Watcher;

import frc.robot.RobotSoftware;
import frc.systems.ClimberSystem;
import frc.systems.GearIntakeSystem;
import frc.systems.PneumaticTwinMecanum;
import frc.systems.SimpleGearIntakeSystem;
import frc.systems.TeleopGearIntakeSystem;
import frc.systems.TeleopHopperSystem;

/**
 * system manager
 * 
 * 
 * @author Quunii
 *
 */
public class TeleopMain
{
	SystemManager manager;
	ControlBoard controls;
	RobotSoftware robot;
	DigitalIn timeRunning;
	Watcher watch;

	/**
	 * 
	 * @param robot
	 *                  container of all ohm streams
	 */
	public TeleopMain(RobotSoftware robot)
	{
		this.robot = robot;
	}

	/**
	 * initializes systems, and adds them to a list of systems to update
	 */
	public void init()
	{
		controls = ControlBoard.getInstance();
		Subsystem drive = setUpTwinstickDrive();
		Subsystem gearManip = setupSimpleIntakeSystem();
		manager = new SystemManager(drive, gearManip);
		manager.init();
	}

	/**
	 * 
	 * @return a new OctocanumSystem
	 */
	private OctocanumSystem setupDrive()
	{
		return new OctocanumSystem(robot.voltageDrive, robot.pistons, robot.gyroInput, controls.driveXAxis(),
				controls.driveYAxis(), controls.driveYaw(), controls.driveTrim(), controls.xLeftBumper(),
				controls.xRightBumper());
	}

	private Subsystem setUpTwinstickDrive()
	{
		return new PneumaticTwinMecanum(controls.xLeftDriveY(), controls.xLeftDriveX(), controls.xRightDriveX(),
				robot.voltageDrive, controls.xLeftBumper(), controls.xRightBumper(), robot.pistons);
	}

	private Subsystem setupSimpleIntakeSystem()
	{
		return new SimpleGearIntakeSystem(controls.aButton(), controls.leftStickYAxis(), controls.rightTrigger(),
				robot.armElevator.getVoltageController(), robot.gearIntake.getVoltageController());
	}

	/**
	 * 
	 * @return a new GearIntakeSystem
	 */
	private GearIntakeSystem setupGearIntake()
	{

		TeleopGearIntakeSystem Supplier = new TeleopGearIntakeSystem(robot.armAngle, robot.armVel,
				robot.armElevator.getVoltageController(), robot.gearIntake.getVoltageController(), robot.gearBeamBreak,
				robot.gearIntakeCurrent, () -> DriveMode.MECANUM, controls.aButton(), controls.bButton(),
				controls.xButton(), controls.yButton(), controls.leftStickYAxis(), controls.rightTrigger(),
				controls.setRumble(), controls.startButton());
		return Supplier;
	}

	/**
	 * 
	 * @return a new ClimberSystem
	 */
	private ClimberSystem setUpClimbing()
	{
		return new ClimberSystem(controls.leftTrigger(), robot.climberVoltage);
	}

	private TeleopHopperSystem setupHopper()
	{

		return new TeleopHopperSystem(robot.dumperPistonRight.getDigitalOut(), robot.dumperPistonLeft.getDigitalOut(),
				robot.dumperPistonRight.getDigitalOut(), new DigitalIn(() -> true), controls.upDPad(),
				controls.downDPad());
	}

	/**
	 * periodically calls update method in all subsystems
	 */
	public void periodic()
	{
		manager.update();

	}

}
