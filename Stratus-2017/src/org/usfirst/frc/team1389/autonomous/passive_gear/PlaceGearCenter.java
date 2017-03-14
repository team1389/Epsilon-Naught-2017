package org.usfirst.frc.team1389.autonomous.passive_gear;

import org.usfirst.frc.team1389.autonomous.AutoConstants;
import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

/**
 * auton for placing gear on middle peg with passive intake
 * 
 * @author Quunii
 *
 */
public class PlaceGearCenter extends AutoModeBase {
	private RobotSoftware robot;
	private RobotCommands commands;

	/**
	 * 
	 * @param robot
	 *            container of all local ohm streams
	 */
	public PlaceGearCenter(RobotSoftware robot) {
		this.robot = robot;
	}

	/**
	 * watches voltage being applied to each drive train motor
	 */
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(robot.voltageDrive);
	}

	/**
	 * runs command to return from center peg
	 */
	@Override
	protected void routine() throws AutoModeEndedException {
		System.out.println("running gear to center");
		runCommand(commands.new DriveStraight(-AutoConstants.CENTER_GEAR_DIST));
	}

	/**
	 * id for this auton
	 */
	@Override
	public String getIdentifier() {
		return "Passive Place Gear Center";
	}
}
