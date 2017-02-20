package org.usfirst.frc.team1389.robot;

import com.team1389.concurrent.OhmThreadService;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.drive.FourDriveOut;

import edu.wpi.first.wpilibj.Compressor;

public class RobotSoftware extends RobotHardware {
	private static RobotSoftware INSTANCE = new RobotSoftware();
	public RangeOut<Percent> ballVoltageOut = ballIntake.getVoltageOutput();
	public AngleIn<Position> gyroInput = new AngleIn<>(Position.class, () -> 0.0);
	public DigitalOut pistons = flPiston.getDigitalOut().addFollowers(frPiston.getDigitalOut(),
			rlPiston.getDigitalOut(), rrPiston.getDigitalOut());
	public FourDriveOut<Percent> voltageDrive = new FourDriveOut<>(frontLeft.getVoltageOutput(),
			frontRight.getVoltageOutput(), rearLeft.getVoltageOutput(), rearRight.getVoltageOutput());
	public AngleIn<Position> armAngle = armElevator
			.getAbsoluteIn()
				.adjustRange(RobotConstants.armAbsoluteMin, RobotConstants.armAbsoluteMax, 0, 90)
				.setRange(0, 360)
				.mapToAngle(Position.class);
	public AngleIn<Speed> armVel = armElevator.getSpeedInput().scale(28 / 12).mapToAngle(Speed.class);
	public RangeIn<Value> gearIntakeCurrent = pdp.getCurrentIn(pdp_GEAR_INTAKE_CURRENT);
	public PercentOut climberVoltageOut = climber.getVoltageOutput();
	public RangeIn<Value> climberCurrent = pdp.getCurrentIn(pdp_Climber_Val);
	public OhmThreadService threadManager = new OhmThreadService(20);

	public static RobotSoftware getInstance() {
		return INSTANCE;
	}

	public RobotSoftware() {
		new Compressor(0).setClosedLoopControl(false);
	}

}
