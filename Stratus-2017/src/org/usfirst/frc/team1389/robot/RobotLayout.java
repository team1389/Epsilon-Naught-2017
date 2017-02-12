package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.hardware.GyroHardware;
import com.team1389.hardware.inputs.hardware.PDPHardware;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.registry.Registry;

/**
 * contains a list of declared hardware objects for this robot. Separated from {@link RobotHardware}
 * to make it easier to see what hardware is connected to the robot.
 * 
 * @author amind
 *
 */
public class RobotLayout extends RobotMap {
	public Registry registry;
	public CANTalonHardware frontLeft;
	public CANTalonHardware frontRight;
	public CANTalonHardware rearLeft;
	public CANTalonHardware rearRight;
	public CANTalonHardware armElevator;
	public VictorHardware gearIntake;
	public PDPHardware pdp;
	public DoubleSolenoidHardware flPiston, frPiston, rlPiston, rrPiston;
	public GyroHardware<?> gyro;
}
