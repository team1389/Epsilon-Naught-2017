package org.usfirst.frc.team1389.autonomous.gears.offseason;

import org.usfirst.frc.team1389.autonomous.AutoConstants;
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

public class CenterGear extends AutoModeBase
{
	RobotSoftware robot;
	RobotCommands commands;
	GearIntakeSystem gearIntake;

	public CenterGear(RobotSoftware robot)
	{
		this.robot = robot;
		commands = new RobotCommands(robot);
		gearIntake = new GearIntakeSystem(robot.armAngle, robot.armVel, robot.armElevator.getVoltageOutput(),
				robot.gearIntake.getVoltageOutput(), robot.gearBeamBreak, robot.gearIntakeCurrent);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return stem.put(robot.voltageDrive, robot.flPos.getWatchable("left encoder"),
				robot.frPos.getWatchable("right encoder"));
	}

	@Override
	protected void routine() throws AutoModeEndedException
	{
		// Command driveToPeg = commands.new
		// DriveStraight(AutoConstants.getRotations(AutoConstants.CENTER_GEAR_DIST));
		Command driveToPeg = commands.new DriveStraightOpenLoop(.45, .5);
		
		Command lowerArm = gearIntake.preparePlaceGear();
		
		Command placeGear = gearIntake.placeGear();

		runCommand(placeGear);//CommandUtil.combineSequential(CommandUtil.combineSimultaneous(driveToPeg, lowerArm), placeGear));
		System.out.println(gearIntake.getState());
	}

	@Override
	public String getIdentifier()
	{    

		return "Center Gear";
	}

}
