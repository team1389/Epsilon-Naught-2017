package frc.robot;

import com.team1389.hardware.inputs.hardware.PDPHardware;
import com.team1389.hardware.inputs.hardware.SpartanGyro;
import com.team1389.hardware.inputs.hardware.SwitchHardware;
import com.team1389.hardware.outputs.hardware.CANTalonHardware;
import com.team1389.hardware.outputs.hardware.DoubleSolenoidHardware;
import com.team1389.hardware.outputs.hardware.VictorHardware;
import com.team1389.hardware.registry.Registry;
import com.team1389.hardware.registry.port_types.CAN;

/**
 * responsible for initializing and storing hardware objects defined in
 * {@link RobotLayout}
 * 
 * @author amind
 * @see RobotLayout
 * @see RobotMap
 */
public class RobotHardware extends RobotLayout {

	/**
	 * Initializes robot hardware by subsystem. <br>
	 * note: use this method as an index to show hardware initializations that
	 * occur, and to find the init code for a particular system's hardware
	 */
	protected RobotHardware() {
		registry = new Registry();
		pdp = new PDPHardware(new CAN(0), registry);
		initDriveTrain();
		initDriveTrainPneumatics();
		initGearIntake();
		initHopper();
		initClimber();
	}

	private void initClimber() {
		climberA = new VictorHardware(inv_CLIMBER_MOTOR_A, pwm_CLIMBER_MOTOR_A, registry);
		climberB = new VictorHardware(inv_CLIMBER_MOTOR_B, pwm_CLIMBER_MOTOR_B, registry);
		climberC = new VictorHardware(inv_CLIMBER_MOTOR_C, pwm_CLIMBER_MOTOR_C, registry);
	}

	private void initHopper() {
		gatePiston = new DoubleSolenoidHardware(mod_HOPPER_PCM, pcm_GATE_PISTON_A, pcm_GATE_PISTON_B, registry);
		dumperPistonRight = new DoubleSolenoidHardware(mod_HOPPER_PCM, pcm_DUMPER_PISTON_RIGHT_A,
				pcm_DUMPER_PISTON_RIGHT_B, registry);
		dumperPistonLeft = new DoubleSolenoidHardware(mod_HOPPER_PCM, pcm_DUMPER_PISTON_LEFT_A,
				pcm_DUMPER_PISTON_LEFT_B, registry);
	}

	private void initGearIntake() {
		armElevator = new VictorHardware(inv_ARM_ELEVATOR_MOTOR, pwm_ARM_ELEVATOR_MOTOR, registry);
		gearIntake = new VictorHardware(inv_GEAR_INTAKE_MOTOR, pwm_GEAR_INTAKE_MOTOR, registry);
		beamBreakSensor = new SwitchHardware(dio_GEAR_BEAM_BREAK, registry);
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
