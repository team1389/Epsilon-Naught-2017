package org.usfirst.frc.team1389.robot;

import com.team1389.concurrent.OhmThreadService;
import com.team1389.hardware.inputs.software.AngleIn;
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
	public AngleIn<Speed> armVel;
	public RangeIn<Value> gearIntakeCurrent;
	public RangeIn<Value> climberCurrent;
	public OhmThreadService threadManager;

	public static RobotSoftware getInstance() {
		return INSTANCE;
	}

	public RobotSoftware() {
		 gyroInput = new AngleIn<>(Position.class, () -> 0.0);
		 pistons = flPiston.getDigitalOut().addFollowers(frPiston.getDigitalOut(),
					rlPiston.getDigitalOut(), rrPiston.getDigitalOut(), new DigitalOut(System.out::println));
		 voltageDrive = new FourDriveOut<>(frontLeft.getVoltageOutput(),
					frontRight.getVoltageOutput(), rearLeft.getVoltageOutput(), rearRight.getVoltageOutput());
		 armAngle = armElevator
				.getAbsoluteIn()
						.adjustRange(RobotConstants.armAbsoluteMin, RobotConstants.armAbsoluteMax, 0, 90)
						.setRange(0, 360)
						.mapToAngle(Position.class);
		 armVel = armElevator.getSpeedInput().scale(28 / 12).mapToAngle(Speed.class);
		 gearIntakeCurrent = pdp.getCurrentIn(pdp_GEAR_INTAKE_CURRENT);
		 threadManager = new OhmThreadService(20);
	}

}
