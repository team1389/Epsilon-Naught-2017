package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;
import com.team1389.hardware.registry.Registry;

/**
 * responsible for initializing and storing hardware objects defined in {@link RobotLayout}
 * 
 * @author amind
 * @see RobotLayout
 * @see RobotMap
 */
public class RobotHardware extends RobotLayout {

	/**
	 * Initializes robot hardware by subsystem. <br>
	 * note: use this method as an index to show hardware initializations that occur, and to find
	 * the init code for a particular system's hardware
	 */
	protected RobotHardware() {
		registry = new Registry();
		// TODO gyro = new GyroHardware<SPIPort>(GyroHardware.ADXRS_453, spi_GyroPort, registry);
		initDriveTrain();
		initDriveTrainPneumatics();
	}

	public Registry getRegistry() {
		return registry;
	}

	private void initDriveTrainPneumatics() {
		flPiston = new DoubleSolenoidHardware(pcm_FRONT_LEFT_PISTON_A, pcm_FRONT_LEFT_PISTON_B, registry);
		frPiston = new DoubleSolenoidHardware(pcm_FRONT_RIGHT_PISTON_A, pcm_FRONT_RIGHT_PISTON_B, registry);
		rlPiston = new DoubleSolenoidHardware(pcm_REAR_LEFT_PISTON_A, pcm_REAR_LEFT_PISTON_B, registry);
		rrPiston = new DoubleSolenoidHardware(pcm_REAR_RIGHT_PISTON_A, pcm_REAR_RIGHT_PISTON_B, registry);
	}

	private void initDriveTrain() {
		frontLeft = new CANTalonHardware(inv_LEFT_FRONT_MOTOR, can_LEFT_FRONT_MOTOR, registry);
		frontRight = new CANTalonHardware(inv_RIGHT_FRONT_MOTOR, can_RIGHT_FRONT_MOTOR, registry);
		rearLeft = new CANTalonHardware(inv_LEFT_REAR_MOTOR, can_LEFT_REAR_MOTOR, registry);
		rearRight = new CANTalonHardware(inv_RIGHT_REAR_MOTOR, can_RIGHT_REAR_MOTOR, registry);

	}
}
