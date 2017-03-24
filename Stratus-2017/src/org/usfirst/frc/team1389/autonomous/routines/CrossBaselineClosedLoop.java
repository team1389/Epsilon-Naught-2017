package org.usfirst.frc.team1389.autonomous.routines;

import org.usfirst.frc.team1389.autonomous.AutoConstants;
import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

/**
 * Auton to cross the baseline
 * 
 * @author Quunii
 *
 */
public class CrossBaselineClosedLoop extends AutoModeBase {
	RobotSoftware robot;
	RobotCommands commands;

	/**
	 * 
	 * @param robot container of all ohm streams
	 */
	public CrossBaselineClosedLoop(RobotSoftware robot) {
		this.robot = robot;
		commands = new RobotCommands(robot);
	}

	/**
	 * watches voltage being applied to every drive train motor
	 */
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(robot.voltageDrive, robot.flPos.getWatchable("left encoder"), robot.frPos.getWatchable("right encoder"));
	}

	/**
	 * drives forwards over the baseline
	 */
	@Override
	protected void routine() throws AutoModeEndedException {
		runCommand(commands.new DriveStraight(AutoConstants.getRotations(AutoConstants.BASELINE_DIST)));
	}

	/**
	 * id of this auton
	 */
	@Override
	public String getIdentifier() {
		return "Cross Baseline";
	}

}
