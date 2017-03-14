package org.usfirst.frc.team1389.autonomous;

import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.system.drive.FourWheelSignal;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

/**
 * Auton to cross the baseline
 * 
 * @author Quunii
 *
 */
public class CrossBaseline extends AutoModeBase {
	RobotSoftware robot;
	RobotCommands commands;

	/**
	 * 
	 * @param robot
	 *            container of all ohm streams
	 */
	public CrossBaseline(RobotSoftware robot) {
		this.robot = robot;
		commands = new RobotCommands(robot);
	}

	/**
	 * watches voltage being applied to every drive train motor
	 */
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(robot.voltageDrive);
	}

	/**
	 * drives forwards over the baseline
	 */
	@Override
	protected void routine() throws AutoModeEndedException {
		robot.voltageDrive.set(new FourWheelSignal(0.5,0.5,0.5,0.5));
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		robot.voltageDrive.set(new FourWheelSignal(0,0,0,0));
	}

	/**
	 * id of this auton
	 */
	@Override
	public String getIdentifier() {
		return "Cross Baseline";
	}

}
