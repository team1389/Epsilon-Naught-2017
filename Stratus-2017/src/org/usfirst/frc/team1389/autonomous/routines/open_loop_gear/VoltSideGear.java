package org.usfirst.frc.team1389.autonomous.routines.open_loop_gear;

import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;
import org.usfirst.frc.team1389.systems.GearIntakeSystem;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class VoltSideGear extends AutoModeBase
{
	RobotCommands commands;
	RobotSoftware robot;
	GearIntakeSystem gearIntake;

	public VoltSideGear(RobotSoftware robot)
	{
		this.robot = robot;
		commands = new RobotCommands(robot);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return stem;
	}

	@Override
	protected void routine() throws AutoModeEndedException
	{
		/*Command driveAndLower = CommandUtil.combineSimultaneous(commands.new DriveStraightOpenLoop(5, .5),
				CommandUtil.combineSequential(new WaitTimeCommand(1), gearIntake.preparePlaceGear()));
		Command turnAndDrive = CommandUtil.combineSequential(commands.new TurnAngle(60, true),
				commands.new DriveStraightOpenLoop(2, .5));
		Command auto = CommandUtil.combineSequential(driveAndLower, turnAndDrive, gearIntake.placeGear(),
				commands.new DriveStraightOpenLoop(-2, .5));
		runCommand(gearIntake.pairWithBackgroundCommand(auto));*/
	}

	@Override
	public String getIdentifier()
	{
		return "OpenLoopSideGear";
	}

}
