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

public class VoltCenterGear extends AutoModeBase {
	RobotCommands commands;
	RobotSoftware robot;
	GearIntakeSystem gearIntake;

	public VoltCenterGear(RobotSoftware robot) {
		this.robot = robot;
		commands = new RobotCommands(robot);
		gearIntake = new GearIntakeSystem(robot.armAngle, robot.armVel, robot.armElevator.getVoltageOutput(),
				robot.gearIntake.getVoltageOutput(), robot.gearBeamBreak);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem;
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		System.out.println("starting center gear");
		Command driveAndLower = CommandUtil.combineSimultaneous(commands.new DriveStraightOpenLoop(5, .5),
				CommandUtil.combineSequential(new WaitTimeCommand(1), gearIntake.preparePlaceGear()));
		Command auto = CommandUtil.combineSequential(driveAndLower, gearIntake.placeGear(),
				commands.new DriveStraightOpenLoop(-2, .5));
		runCommand(gearIntake.pairWithBackgroundCommand(auto));
	}

	@Override
	public String getIdentifier() {
		return "OpenLoopPlaceGear";
	}

}
