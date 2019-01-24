package frc.robot;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeExecuter;
import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.DIO;
import frc.robot.RobotSoftware;
import frc.operation.TeleopMain;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotSoftware robot;
	TeleopMain teleOperator;
	Registry registry;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {

		registry = new Registry();
		robot = RobotSoftware.getInstance();
		teleOperator = new TeleopMain(robot);


	}

	@Override
	public void autonomousInit() {


	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		teleOperator.init();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		// left side good
		// right side is good
		teleOperator.periodic();
	}

	@Override

	public void disabledInit() {
	}

	@Override

	public void disabledPeriodic() {

	}
}
