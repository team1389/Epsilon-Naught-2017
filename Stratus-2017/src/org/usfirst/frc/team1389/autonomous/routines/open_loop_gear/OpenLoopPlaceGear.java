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

public class OpenLoopPlaceGear extends AutoModeBase {
	RobotCommands commands;
	RobotSoftware robot;
	PercentOut armMotor;
	FourDriveOut<Percent> drive;
	PercentOut intakeMotor;

	public OpenLoopPlaceGear(RobotSoftware robot) {
		this.robot = robot;
		this.armMotor = robot.armElevator.getVoltageOutput();
		drive = robot.voltageDrive;
		commands = new RobotCommands(robot);
		intakeMotor = robot.gearIntake.getVoltageOutput();
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem;
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		System.out.println("auto started");
		/*Command lowerArm = CommandUtil.combineSequential(CommandUtil.createCommand(() -> armMotor.set(.5)),
				new WaitTimeCommand(.7), CommandUtil.createCommand(() -> armMotor.set(0.0)));*/
		Command lowerArm = CommandUtil.combineSequential(CommandUtil.createCommand(() -> armMotor.set(.5)),
				new WaitTimeCommand(.3), CommandUtil.createCommand(() -> armMotor.set(0.0)));

		Command driveBack = CommandUtil.combineSequential(new WaitTimeCommand(.5),
				commands.new DriveStraightOpenLoop(0.8, -0.2));

		Command driveApproach = CommandUtil.combineSimultaneous(commands.new DriveStraightOpenLoop(3.2, 0.3),
				CommandUtil.createCommand(() -> intakeMotor.set(-1)), new WaitTimeCommand(1.5),
				CommandUtil.createCommand(() -> intakeMotor.set(0)));
//CommandUtil.combineSequential(driveApproach, CommandUtil.combineSimultaneous(driveBack, lowerArm))
		System.out.println("RUNNING");
		runCommand(lowerArm);
		System.out.println("DONE");
	}

	@Override
	public String getIdentifier() {
		return "OpenLoopPlaceGear";
	}

}
