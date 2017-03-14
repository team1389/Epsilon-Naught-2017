package org.usfirst.frc.team1389.robot;

import java.util.function.Function;

import org.usfirst.frc.team1389.robot.controls.ControlBoard;

import com.team1389.concurrent.OhmThreadService;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PositionEncoderIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.drive.FourDriveOut;

public class RobotSoftware extends RobotHardware {
	private static RobotSoftware INSTANCE = new RobotSoftware();
	public AngleIn<Position> gyroInput;
	public DigitalOut pistons;
	public FourDriveOut<Percent> voltageDrive;
	public AngleIn<Position> armAngle;
	public AngleIn<Position> armAngleAbsolute;
	public AngleIn<Speed> armVel;
	public RangeIn<Value> gearIntakeCurrent;
	public RangeIn<Position> flPos, frPos;
	public OhmThreadService threadManager;
	public DigitalIn timeRunning;
	public RangeIn<Value> flCurrent, frCurrent, blCurrent, brCurrent;
	public RangeIn<Value> armCurrent;
	public DigitalOut rumble;

	public static RobotSoftware getInstance() {
		return INSTANCE;
	}

	public RobotSoftware() {

		// gyroInput = new AngleIn<>(Position.class, () -> 0.0);
		gyroInput = gyro.getAngleInput();

		pistons = flPiston.getDigitalOut().addFollowers(frPiston.getDigitalOut(), rlPiston.getDigitalOut(),
				rrPiston.getDigitalOut());

		voltageDrive = new FourDriveOut<>(frontLeft.getVoltageOutput(), frontRight.getVoltageOutput(),
				rearLeft.getVoltageOutput(), rearRight.getVoltageOutput());

		armAngleAbsolute = armPot.getAnalogInput().map(d -> 10 - d).mapToAngle(Position.class);
		armAngle = armElevator.getPositionInput().scale(RobotConstants.armSprocketRatio).mapToAngle(Position.class);
		armVel = armElevator.getSpeedInput().scale(RobotConstants.armSprocketRatio).mapToAngle(Speed.class);
		zeroArmAngle();

		gearIntakeCurrent = pdp.getCurrentIn(pdp_GEAR_INTAKE_CURRENT);
		
		flCurrent = frontLeft.getCurrentIn();
		frCurrent = frontRight.getCurrentIn();
		blCurrent = rearLeft.getCurrentIn();
		brCurrent = rearRight.getCurrentIn();
		armCurrent = armElevator.getCurrentIn();
		Function<PositionEncoderIn, RangeIn<Position>> posFunc = e -> e.<PositionEncoderIn>setTicksPerRotation(1024)
				.mapToRange(0, 1).scale(18 / 16);

		flPos = posFunc.apply(frontLeft.getPositionInput());
		frPos = posFunc.apply(frontRight.getPositionInput());
		/*
		 * flCurrent = pdp.getCurrentIn(pdp_FRONT_LEFT_CURRENT); frCurrent =
		 * pdp.getCurrentIn(pdp_FRONT_RIGHT_CURRENT); blCurrent =
		 * pdp.getCurrentIn(pdp_REAR_LEFT_CURRENT); brCurrent =
		 * pdp.getCurrentIn(pdp_REAR_RIGHT_CURRENT);
		 */

		ControlBoard board = ControlBoard.getInstance();
		rumble = new DigitalOut((b) -> board.rumble.accept(b? 1.0 : 0.0));
		threadManager = new OhmThreadService(20);
	}

	public void zeroArmAngle() {
		double val = armAngleAbsolute.get() * 360 - armAngle.get();
		System.out.println(val);
		armAngle.offset(val);
	}

}
