package org.usfirst.frc.team1389.autonomous;

import org.usfirst.frc.team1389.robot.RobotConstants;

/**
 * constants for distance to destinations in auton (all in rotations)
 * 
 * @author Quunii
 *
 */
public class AutoConstants
{
	public static final double SIDE_GEAR_STRAIGHT = 74.0625, SIDE_GEAR_TURN = 60, SIDE_GEAR_APPROACH = 83.6875;
	public static final double ACTIVE_STOP_SHORT = 6;
	public static final double CENTER_GEAR_DIST = 114.3;
	// 18 ft per second
	// distance to center 9.52 ft
	// should take about 0.52 seconds
	public static final double BASELINE_DIST = 93.3;// TODO find baseline dist in
													// inches;

	public static double getRotations(double distInches)
	{
		return distInches / (RobotConstants.WheelDiameter * Math.PI);
	}

	public static double getTimeToTravel(double inches, double percentVoltage)
	{
		double inchesPerSec = RobotConstants.MaxVelocity * percentVoltage;
		return inches / inchesPerSec;
	}
}
