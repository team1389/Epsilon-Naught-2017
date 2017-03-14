package org.usfirst.frc.team1389.autonomous.passive_gear;

import org.usfirst.frc.team1389.autonomous.AutoConstants;
import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.command_framework.CommandUtil;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

/**
 *auton for placing gear on right peg with passive intake
 * 
 * @author Quunii
 *
 */
public class PlaceGearRight extends AutoModeBase {
	private RobotSoftware robot;
	private RobotCommands commands;

	/**
	 * 
	 * @param robot
	 *            container of all local ohm streams
	 */
	public PlaceGearRight(RobotSoftware robot) {
		this.robot = robot;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(robot.voltageDrive);
	}

	/**
	 * runs series of commands such that the robot returns to starting position
	 */
	
	@Override
	protected void routine() throws AutoModeEndedException {
		runCommand(CommandUtil.combineSequential(commands.new DriveStraight(-AutoConstants.SIDE_GEAR_STRAIGHT),
				commands.new TurnAngle(AutoConstants.SIDE_GEAR_TURN, true),
				commands.new DriveStraight(-AutoConstants.SIDE_GEAR_APPROACH)));
	}

	/**
	 * id of this auton
	 */
	@Override
	public String getIdentifier() {
		return "Passive Place Gear Left";
	}
	
}

