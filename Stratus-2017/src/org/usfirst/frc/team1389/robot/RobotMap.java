package org.usfirst.frc.team1389.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * <p>
 * <b>Conventions</b>: <br>
 * For I/O ports, the naming convention is <em>type_ALL_CAPS_IDENTIFIER</em>.
 * for example, a talon port might be named can_LEFT_MOTOR_A. Possible port
 * types and identifiers are CAN (can), Analog (anlg), PWM (pwm), USB (usb), PCM
 * (pcm), DIO (dio), etc
 * <p>
 * Inputs and Outputs may be inverted. The inversions in this map should only
 * relate to the physical configuration of the robot. A positive value should
 * cause the output to move in the most logical direction (I.e, the drive motors
 * should move forward with positive voltage values) <br>
 * the convention for inversion constants is
 * <em>inv_ASSOCIATED_IO_IDENTIFIER</em> for outputs and
 * <em>sinv_ASSOCIATED_IO_IDENTIFIER</em> for inputs.
 */
public class RobotMap {
	//Outputs
	
	//Drivetrain
	protected static final int can_LEFT_MOTOR_A = 4; protected static final boolean inv_LEFT_MOTOR_A = true;
	protected static final int can_LEFT_MOTOR_B = 5; protected static final boolean inv_LEFT_MOTOR_B = true;
	protected static final int can_LEFT_MOTOR_C = 6; protected static final boolean inv_LEFT_MOTOR_C = true;
	protected static final int can_RIGHT_MOTOR_A = 2; protected static final boolean inv_RIGHT_MOTOR_A = false;
	protected static final int can_RIGHT_MOTOR_B = 1; protected static final boolean inv_RIGHT_MOTOR_B = false;
	protected static final int can_RIGHT_MOTOR_C = 3; protected static final boolean inv_RIGHT_MOTOR_C = false;
	
	//Arm
	protected static final int can_TURRET_MOTOR = 10; protected static final boolean inv_TURRET_MOTOR = false;
	protected static final int anlg_TURRET_GYRO=1;
	
	protected static final int can_ELEVATOR_MOTOR_A = 12; protected static final boolean inv_ELEVATOR_MOTOR_A = true;
	protected static final int can_ELEVATOR_MOTOR_B = 8; protected static final boolean inv_ELEVATOR_MOTOR_B = true;
	protected static final int anlg_ARM_POTENTIOMETER=0;
	
	//Ball manipulator
	protected static final int pwm_INTAKE_MOTOR = 0; protected static final boolean inv_INTAKE_MOTOR = true;
	protected static final int can_FLYWHEEL_MOTOR_A = 9; protected static final boolean inv_FLYWHEEL_MOTOR_A = true;
	protected static final int can_FLYWHEEL_MOTOR_B = 7; protected static final boolean inv_FLYWHEEL_MOTOR_B = true;
	
	//Inputs
	protected static final int dio_INTAKE_IR_A = 0; protected static final boolean sinv_INTAKE_IR_B = true;
	protected static final int dio_INTAKE_IR_B = 1; protected static final boolean sinv_INTAKE_IR_A = true;
	
	protected static final boolean sinv_LEFT_ENCODER = true;
	protected static final boolean sinv_RIGHT_ENCODER = false;
	protected static final boolean sinv_ELEVATOR_ENCODER = true;
	protected static final boolean sinv_FLYWHEEL_ENCODER = false;	

}

