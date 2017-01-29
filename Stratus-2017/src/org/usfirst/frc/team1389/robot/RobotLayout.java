package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.hardware.GyroHardware;
import com.team1389.hardware.inputs.hardware.NavXHardware;
import com.team1389.hardware.inputs.hardware.PotentiometerHardware;
import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.outputs.hardware.CANTalonGroup;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.registry.Registry;

/**
 * contains a list of declared hardware objects for this robot. Separated from
 * {@link RobotHardware} to make it easier to see what hardware is connected to
 * the robot.
 * 
 * @author amind
 *
 */
public class RobotLayout extends RobotMap {
	public Registry registry;

	public SwitchHardware IRsensor1;
	public SwitchHardware IRsensor2;

	CANTalonHardware leftA;
	CANTalonHardware leftB;
	CANTalonHardware leftC;
	public CANTalonGroup leftDrive;

	public GyroHardware turretGyro;
	public NavXHardware navX;

	public PotentiometerHardware armPot;
	CANTalonHardware rightA;
	CANTalonHardware rightB;
	CANTalonHardware rightC;
	public CANTalonGroup rightDrive;

	CANTalonHardware elevationA;
	CANTalonHardware elevationB;
	public CANTalonGroup elevation;

	public CANTalonHardware turret;
	public VictorHardware intake;
}
