package org.usfirst.frc.team1389.autonomous.command;

import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.command.DriveStraightCommand;
import com.team1389.auto.command.TurnAngleCommand;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.drive.DriveOut;
import com.team1389.system.drive.DriveSignal;
import com.team1389.util.Timer;

public class RobotCommands {
	RobotSoftware robot;

	public RobotCommands(RobotSoftware software) {
		this.robot = software;
	}

	public class DriveStraight extends DriveStraightCommand {
		public DriveStraight(double distance) {
			this(distance, 5);
		}

		public DriveStraight(double distance, double speed) {
			super(new PIDConstants(1, .03, .02), new PIDConstants(0, 0, .0), robot.voltageDrive.getAsTank(),
					robot.flPos, robot.frPos, robot.gyroInput.copy().invert(), distance, 2, 2, speed, .05);
		}
	}

	public class DriveStraightOpenLoop extends Command {
		Timer timer;
		double time;
		DriveOut<Percent> tankDrive;
		double voltage, angleOffset;

		public DriveStraightOpenLoop(double time, double voltage) {
			this.time = time;
			timer = new Timer();
			tankDrive = robot.compensatedDrive.getAsTank();

			/*
			 * tankDrive = new
			 * FourDriveOut<>(robot.frontLeft.getCompensatedVoltageOut(),
			 * robot.frontRight.getCompensatedVoltageOut(),
			 * robot.rearLeft.getCompensatedVoltageOut(),
			 * robot.rearRight.getCompensatedVoltageOut()).getAsTank();
			 */
			this.voltage = voltage;
		}

		@Override
		protected void initialize() {
			timer.zero();
			angleOffset = robot.gyroInput.get();
		}

		@Override
		protected boolean execute() {
			double gyroTerm = robot.gyroInput.get() - angleOffset;
			double val = .1 * -gyroTerm;
			tankDrive.set(voltage - val, voltage + val);
			return timer.get() > time;
		}

		@Override
		protected void done() {
			tankDrive.set(DriveSignal.NEUTRAL);
		}
	}

	public class TurnAngle extends TurnAngleCommand<Percent> {

		public TurnAngle(double angle, boolean absolute) {
			super(angle, absolute, 2, robot.gyro.getAngleInput(),
					TurnAngleCommand.createTurnController(robot.voltageDrive.getAsTank()),
					new PIDConstants(0.05, .001, .5));
		}

	}

	public class TurnAngleOpenLoop extends Command {
		double time, lVoltage, rVoltage;
		DriveOut<Percent> drive;
		Timer timer;

		public TurnAngleOpenLoop(double time, double lVoltage, double rVoltage) {
			this.time = time;
			this.lVoltage = lVoltage;
			this.rVoltage = rVoltage;
			drive = robot.voltageDrive.getAsTank();
			timer = new Timer();
		}

		@Override
		protected boolean execute() {
			drive.set(lVoltage, rVoltage);
			return timer.get() >= time;
		}

	}
}