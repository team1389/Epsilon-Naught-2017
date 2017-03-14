package org.usfirst.frc.team1389.autonomous.command;

import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.command.DriveStraightCommand;
import com.team1389.auto.command.TurnAngleCommand;
import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.value_types.Percent;

/**
 * 
 * @author Quunii container class containing nested classes
 */
public class RobotCommands {
	RobotSoftware robot;

	/**
	 * 
	 * @param software
	 *            container of all ohm streams
	 */
	public RobotCommands(RobotSoftware software) {
		this.robot = software;
	}

	/**
	 * DriveStraightCommand integration using local streams
	 * 
	 * @author Quunii
	 *
	 */
	public class DriveStraight extends DriveStraightCommand {

		/**
		 * overload constructor
		 * 
		 * @param distance
		 *            the distance to travel
		 */
		public DriveStraight(double distance) {
			this(distance, 5);
		}

		/**
		 * passes params to superclass constructor, along with local streams
		 * 
		 * @param distance
		 *            the distance to travel
		 * @param speed
		 *            the max speed
		 */
		public DriveStraight(double distance, double speed) {
			super(new PIDConstants(5, .1, 0),new PIDConstants(0, 0, 0), robot.voltageDrive.getAsTank(), robot.flPos, robot.frPos, robot.gyroInput,
					distance, 5, 5, speed,1);
		}
	}

	/**
	 * TurnAngleCommand integration using local streams
	 * 
	 * @author Quunii
	 *
	 */
	public class TurnAngle extends TurnAngleCommand<Percent> {

		/**
		 * passes params and local streams to superclass constructor
		 * 
		 * @param angle
		 *            the angle to turn
		 * @param absolute
		 *            whether the given angle is absolute, or relative to
		 *            starting position
		 */
		public TurnAngle(double angle, boolean absolute) {
			super(angle, absolute, 2, robot.gyro.getAngleInput(),
					TurnAngleCommand.createTurnController(robot.voltageDrive.getAsTank()),
					new PIDConstants(0.05, 0, 0.0));
		}

	}
}
