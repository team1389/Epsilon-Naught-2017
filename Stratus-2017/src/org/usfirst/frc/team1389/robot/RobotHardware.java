package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.hardware.PDPHardware;
import com.team1389.hardware.outputs.hardware.CANLightHardware;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.CAN;

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
		pdp = new PDPHardware(new CAN(0), registry);
		lights = new CANLightHardware(can_LIGHT_CONTROLLER, registry);
		ballIntake = new VictorHardware(inv_BALL_INTAKE_MOTOR, pwm_BALL_INTAKE_MOTOR, registry);
		climber = new VictorHardware(inv_CLIMBER_MOTOR, pwm_CLIMBER_MOTOR, registry);
		initDriveTrain();
		initDriveTrainPneumatics();
		initGearIntake();
	}

	private void initGearIntake() {
		armElevator = new CANTalonHardware(inv_ARM_ELEVATOR_MOTOR, sinv_ARM_ELEVATOR_MOTOR, can_ARM_ELEVATOR_MOTOR,
				registry);
		gearIntake = new VictorHardware(inv_GEAR_INTAKE_MOTOR, pwm_GEAR_INTAKE_MOTOR, registry);
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
		frontLeft = new CANTalonHardware(inv_LEFT_FRONT_MOTOR, sinv_LEFT_FRONT_MOTOR, can_LEFT_FRONT_MOTOR, registry);
		frontRight = new CANTalonHardware(inv_RIGHT_FRONT_MOTOR, sinv_RIGHT_FRONT_MOTOR, can_RIGHT_FRONT_MOTOR,
				registry);
		rearLeft = new CANTalonHardware(inv_LEFT_REAR_MOTOR, can_LEFT_REAR_MOTOR, registry);
		rearRight = new CANTalonHardware(inv_RIGHT_REAR_MOTOR, can_RIGHT_REAR_MOTOR, registry);

	}
}
