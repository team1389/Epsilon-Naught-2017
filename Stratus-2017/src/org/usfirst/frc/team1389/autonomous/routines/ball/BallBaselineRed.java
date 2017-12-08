package org.usfirst.frc.team1389.autonomous.routines.ball;

import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;
import org.usfirst.frc.team1389.systems.HopperSystem;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.configuration.PIDConstants;
import com.team1389.control.PIDController;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.drive.OctoMecanumSystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class BallBaselineRed extends AutoModeBase
{
	RobotSoftware robot;
	DigitalOut hopperL;
	DigitalOut hopperR;
	RobotCommands commands;
	PIDController pid;

	public BallBaselineRed(RobotSoftware robot)
	{
		this.robot = robot;
		this.hopperL = robot.dumperPistonLeft.getDigitalOut();
		this.hopperR = robot.dumperPistonRight.getDigitalOut();
		commands = new RobotCommands(robot);
		pid = new PIDController(new PIDConstants(.03, .0001, .001), robot.armAngle,
				robot.armElevator.getVoltageOutput());

	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return stem;
	}

	@Override
	protected void routine() throws AutoModeEndedException
	{
		/*
		 * robot.pistons.set(OctoMecanumSystem.DriveMode.MECANUM.solenoidVal);
		 * runCommand(commands.new MecanumMove(1.0, .2, 0, .45));
		 * hopper.set(HopperSystem.DumperPosition.DUMP.pos); runCommand(new
		 * WaitTimeCommand(3)); runCommand(commands.new DriveStraightOpenLoop(1, .3));
		 * runCommand(commands.new TurnAngle( 45, false)); runCommand(commands.new
		 * DriveStraightOpenLoop(3, .3));
		 */
		System.out.println("Debug practice ballbaseline red");
		robot.pistons.set(OctoMecanumSystem.DriveMode.MECANUM.solenoidVal);
		runCommand(commands.new MecanumMove(1.0, .2, 0, .45));
		hopperL.set(HopperSystem.DumperPosition.DUMP.pos);
		hopperR.set(HopperSystem.DumperPosition.DUMP.pos);
		runCommand(new WaitTimeCommand(2));
		runCommand(commands.new DriveStraightOpenLoop(2, .5)); // change to 2
		pid.enable();
		pid.setSetpoint(45);
		pid.setAbsoluteTolerance(4);
		if (pid.onTarget())
			pid.disable();
		WaitTimeCommand wait = new WaitTimeCommand(5);
		runCommand(wait);
		if (wait.isFinished())
		{
			pid.disable();
		}
	}

	@Override
	public String getIdentifier()
	{
		return "BallBaseline";
	}

}
