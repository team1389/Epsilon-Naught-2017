package org.usfirst.frc.team1389.robot;
/**
 * constants of robot dimensions, as well those used for calculations
 * @author Quunii
 *
 */
public class RobotConstants {
	public static final double INCHES_TO_METERS = .0254;
	//was 8
	public static final double WheelDiameter = 4; // in

	public static final int armAbsoluteMin = 1950;
	public static final int armAbsoluteMax = 4390;
	public static final double armPotSoftStop = .39;
	public static final double armPotTurns = 10;
	public static final double armSprocketRatio = 28 / 12;

	/**
	 * constants for odometry calculations
	 */
	public static final double TrackWidth = 22; // in
	public static final double TrackLength = 23;
	public static final double TrackScrub = 1;

	/**
	 * constants for motion profiling
	 */
	public static final double MaxVelocity = 22; // m/s
	public static final double MaxAcceleration = 22; // m/s^2
	public static final double MaxDeceleration = 22; // m/s^2

}
