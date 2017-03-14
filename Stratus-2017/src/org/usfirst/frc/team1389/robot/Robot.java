
package org.usfirst.frc.team1389.robot;

import org.usfirst.frc.team1389.operation.TeleopMain;
import org.usfirst.frc.team1389.watchers.DashboardInput;
import org.usfirst.frc.team1389.watchers.DebugDash;
import org.usfirst.frc.team1389.watchers.WebDashWatcher;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeExecuter;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;

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
	AutoModeExecuter autoModeExecuter;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		robot = RobotSoftware.getInstance();
		DashboardInput.getInstance().init();
		robot.threadManager.init();
		teleOperator = new TeleopMain(robot);
		WebDashWatcher dash = new WebDashWatcher(robot);
		robot.threadManager.scheduleCommand(dash, 200);
		CameraServer.getInstance().startAutomaticCapture(1);
		autoModeExecuter = new AutoModeExecuter();
		DebugDash.getInstance().watch(robot.flPos.getWatchable("Left encoder"),
				robot.gyroInput.getWatchable("Gyro angle"), robot.flCurrent.getWatchable("front left current"),
				robot.frCurrent.getWatchable("front right current"),
				robot.brCurrent.getWatchable("rear right current"),robot.blCurrent.getWatchable("rear left current"), robot.armCurrent.getWatchable("arm current"));

	}

	@Override
	public void autonomousInit() {
		robot.threadManager.init();
		autoModeExecuter.stop();
		AutoModeBase selectedAutonMode = DashboardInput.getInstance().getSelectedAutonMode();
		autoModeExecuter.setAutoMode(selectedAutonMode);
		robot.threadManager.borrowThreadToRun(autoModeExecuter);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void disabledInit() {
		robot.threadManager.init();
	}

	@Override
	public void disabledPeriodic() {
	}

	@Override
	public void teleopInit() {
		robot.threadManager.init();
		DebugDash.getInstance().outputToDashboard();
		autoModeExecuter.stop();
		teleOperator.init();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		teleOperator.periodic();
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testInit() {
	}
}
