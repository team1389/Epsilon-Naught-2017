package org.usfirst.frc.team1389.autonomous.routines.open_loop_gear;

import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.drive.FourDriveOut;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class RightOpenLoopPlaceGear extends AutoModeBase {
	RobotCommands commands;
	RobotSoftware robot;
	PercentOut armMotor;
	FourDriveOut<Percent> drive;

	public RightOpenLoopPlaceGear(RobotSoftware robot) {
		this.robot = robot;
		this.armMotor = robot.armElevator.getVoltageOutput();
		drive = robot.voltageDrive;
		commands = new RobotCommands(robot);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem;
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		Command lowerArm = CommandUtil.combineSequential(CommandUtil.createCommand(() -> armMotor.set(.2)),
				new WaitTimeCommand(.5), CommandUtil.createCommand(() -> armMotor.set(0.0)));
		Command drive = CommandUtil.combineSequential(commands.new DriveStraightOpenLoop(1.65, 0.3),
				commands.new TurnAngleOpenLoop(0.5, -.3, .5), commands.new DriveStraightOpenLoop(1.73, 0.3));
		System.out.println("RUNNING");
		runCommand(CommandUtil.combineSimultaneous(lowerArm, drive));
		System.out.println("DONE");
	}

	@Override
	public String getIdentifier() {
		return "OpenLoopPlaceRightGear";
	}

}
