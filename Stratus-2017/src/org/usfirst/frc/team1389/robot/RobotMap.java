package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.registry.port_types.CAN;
import com.team1389.hardware.registry.port_types.PCM;
import com.team1389.hardware.registry.port_types.PWM;
import com.team1389.hardware.registry.port_types.SPIPort;

import edu.wpi.first.wpilibj.SPI;

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
	protected static final CAN can_LEFT_FRONT_MOTOR = new CAN(2); protected static final boolean inv_LEFT_FRONT_MOTOR = true;
	protected static final CAN can_RIGHT_FRONT_MOTOR = new CAN(4); protected static final boolean inv_RIGHT_FRONT_MOTOR = false;
	protected static final CAN can_LEFT_REAR_MOTOR = new CAN(6); protected static final boolean inv_LEFT_REAR_MOTOR = true;
	protected static final CAN can_RIGHT_REAR_MOTOR = new CAN(3); protected static final boolean inv_RIGHT_REAR_MOTOR = false;
	protected static final CAN can_ARM_ELEVATOR_MOTOR = new CAN(5); protected static final boolean inv_ARM_ELEVATOR_MOTOR = false;
	protected static final SPIPort spi_GyroPort = new SPIPort(SPI.Port.kOnboardCS0);
	protected static final boolean sinv_ARM_ELEVATOR_MOTOR = false;
	protected static final PWM pwm_GEAR_INTAKE_MOTOR = new PWM(1); protected static final boolean inv_GEAR_INTAKE_MOTOR = true;
	protected static final PWM pwm_BALL_INTAKE_MOTOR = new PWM(4); protected static final boolean inv_BALL_INTAKE_MOTOR = false;
	protected static final PCM pcm_FRONT_LEFT_PISTON_A = new PCM(7);
	protected static final PCM pcm_FRONT_LEFT_PISTON_B = new PCM(0);
	protected static final PCM pcm_FRONT_RIGHT_PISTON_A = new PCM(4);
	protected static final PCM pcm_FRONT_RIGHT_PISTON_B = new PCM(3);
	protected static final PCM pcm_REAR_LEFT_PISTON_A = new PCM(6);
	protected static final PCM pcm_REAR_LEFT_PISTON_B = new PCM(1);
	protected static final PCM pcm_REAR_RIGHT_PISTON_A = new PCM(5);
	protected static final PCM pcm_REAR_RIGHT_PISTON_B = new PCM(2);
	protected static final int pdp_GEAR_INTAKE_CURRENT = 4;

}

