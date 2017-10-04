package org.usfirst.frc.team1389.robot;

import java.util.function.Function;

import com.team1389.concurrent.OhmThreadService;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PositionEncoderIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.drive.FourDriveOut;

import edu.wpi.first.wpilibj.Preferences;

public class RobotSoftware extends RobotHardware
{
	private static RobotSoftware INSTANCE = new RobotSoftware();
	public AngleIn<Position> gyroInput;
	public DigitalOut pistons;
	public FourDriveOut<Percent> voltageDrive;
	public FourDriveOut<Percent> compensatedDrive;
	public AngleIn<Position> armAngle;
	public AngleIn<Position> armAngleNoOffset;
	public AngleIn<Speed> armVel;
	public RangeIn<Value> gearIntakeCurrent;
	public RangeIn<Position> flPos, frPos;
	public OhmThreadService threadManager;
	public DigitalIn timeRunning;
	public RangeIn<Value> flCurrent, frCurrent, blCurrent, brCurrent;
	public RangeIn<Value> armCurrent;
	public DigitalIn gearBeamBreak;
	public PercentOut climberVoltage;

	public static RobotSoftware getInstance()
	{
		return INSTANCE;
	}

	public RobotSoftware()
	{
		compensatedDrive = new FourDriveOut<>(frontLeft.getCompensatedVoltageOut(),
				frontRight.getCompensatedVoltageOut(), rearLeft.getCompensatedVoltageOut(),
				rearRight.getCompensatedVoltageOut());
		// gyroInput = new AngleIn<>(Position.class, () -> 0.0);
		gyroInput = gyro.getAngleInput();

		pistons = flPiston.getDigitalOut().addFollowers(frPiston.getDigitalOut(), rlPiston.getDigitalOut(),
				rrPiston.getDigitalOut());

		voltageDrive = new FourDriveOut<>(frontLeft.getVoltageOutput(), frontRight.getVoltageOutput(),
				rearLeft.getVoltageOutput(), rearRight.getVoltageOutput());
		armAngleNoOffset = armElevator.getAbsoluteIn().mapToAngle(Position.class)
				.scale(RobotConstants.armSprocketRatio);
		armAngle = armAngleNoOffset.copy();//.offset(-RobotConstants.armOffset);
		armVel = armElevator.getSpeedInput().scale(RobotConstants.armSprocketRatio).mapToAngle(Speed.class);

		gearIntakeCurrent = pdp.getCurrentIn(pdp_GEAR_INTAKE_CURRENT);
		gearBeamBreak = beamBreakSensor.getSwitchInput();
		flCurrent = frontLeft.getCurrentIn();
		frCurrent = frontRight.getCurrentIn();
		blCurrent = rearLeft.getCurrentIn();
		brCurrent = rearRight.getCurrentIn();
		armCurrent = armElevator.getCurrentIn();
		//was 1024 max, 4096 is for ctre mag encoder
		Function<PositionEncoderIn, RangeIn<Position>> posFunc = e -> e.adjustRange(0, 4096, 0, 1).scale(18.0 / 16.0);

		flPos = posFunc.apply(frontLeft.getPositionInput());
		frPos = posFunc.apply(frontRight.getPositionInput());
		/*
		 * flCurrent = pdp.getCurrentIn(pdp_FRONT_LEFT_CURRENT); frCurrent =
		 * pdp.getCurrentIn(pdp_FRONT_RIGHT_CURRENT); blCurrent =
		 * pdp.getCurrentIn(pdp_REAR_LEFT_CURRENT); brCurrent =
		 * pdp.getCurrentIn(pdp_REAR_RIGHT_CURRENT);
		 */
		climberVoltage = climberA.getVoltageOutput().addFollowers(climberB.getVoltageOutput())
				.addFollowers(climberC.getVoltageOutput());
		threadManager = new OhmThreadService(20);
	}

	public void zeroAngle()
	{
		/*double offset = armAngleNoOffset.get();
		System.out.println("Angle offset: " + offset);
		Preferences.getInstance().putDouble("offset", offset);
		armAngle.clone(armAngleNoOffset.copy().offset(-offset));
	*/
	}

}
