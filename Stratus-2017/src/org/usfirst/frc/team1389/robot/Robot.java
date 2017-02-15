
package org.usfirst.frc.team1389.robot;

import org.usfirst.frc.team1389.operation.TeleopMain;
import org.usfirst.frc.team1389.robot.controls.ControlBoard;
import org.usfirst.frc.team1389.watchers.DashboardInput;
import org.usfirst.frc.team1389.watchers.DebugDash;

import com.team1389.auto.AutoModeExecuter;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the IterativeRobot documentation. If you change the name of this class
 * or the package after creating this project, you must also update the manifest file in the
 * resource directory.
 */
public class Robot extends IterativeRobot {
	RobotSoftware robot;
	TeleopMain teleOperator;
	AutoModeExecuter autoModeExecuter;

	/**
	 * This function is run when the robot is first started up and should be used for any
	 * initialization code.
	 */
	@Override
	public void robotInit() {
		robot = RobotSoftware.getInstance();
		teleOperator = new TeleopMain(robot);
		autoModeExecuter = new AutoModeExecuter();
	}

	UsbCamera camera1 = new UsbCamera("USB Camera " + 1, 1);
	UsbCamera camera2 = new UsbCamera("USB Camera " + 0, 0);
	MjpegServer server = CameraServer.getInstance().addServer("Driver feed", 5801);

	@Override
	public void autonomousInit() {
		robot.threadManager.init();
		autoModeExecuter.stop();
		autoModeExecuter.setAutoMode(DashboardInput.getInstance().getSelectedAutonMode());
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void disabledPeriodic() {
	}

	boolean cam;

	@Override
	public void teleopInit() {
		robot.threadManager.init();
		DebugDash.getInstance().outputToDashboard();
		autoModeExecuter.stop();
		teleOperator.init();
		cam = true;
		camera1.setResolution(640, 480);
		camera2.setResolution(640, 480);
		server.setSource(camera1);
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		teleOperator.periodic();
		if (ControlBoard.getInstance().yButton.get()) {
			server.setSource(cam ? camera1 : camera2);
			cam = !cam;
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
		DebugDash.getInstance().display();
	}

}
