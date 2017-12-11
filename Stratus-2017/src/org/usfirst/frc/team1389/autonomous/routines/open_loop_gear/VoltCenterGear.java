package org.usfirst.frc.team1389.autonomous.routines.open_loop_gear;

import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.command_framework.command_base.Command;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class VoltCenterGear extends AutoModeBase
{
	RobotCommands commands;
	RobotSoftware robot;
	
	public VoltCenterGear(RobotSoftware robot)
	{
		this.robot = robot;
		commands = new RobotCommands(robot);
	}
	
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		// TODO Auto-generated method stub
		Command move = commands.new DriveStraightOpenLoop(0.3, 8);
		runCommand(move);
		Command spin = commands.new TurnAngle(180, true);
		runCommand(spin);
		runCommand(move);
		runCommand(spin);
		Command armMove = commands.new ArmMovement(82);
		runCommand(armMove);
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}