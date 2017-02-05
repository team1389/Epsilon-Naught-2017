package org.usfirst.frc.team1389.robot;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.drive.FourDriveOut;

public class RobotSoftware extends RobotHardware {
	private static RobotSoftware INSTANCE = new RobotSoftware();
	public AngleIn<Position> gyroInput = new AngleIn<>(Position.class, () -> 0.0);
	public DigitalOut pistons = flPiston.getDigitalOut().addFollowers(frPiston.getDigitalOut(),
			rlPiston.getDigitalOut(), rrPiston.getDigitalOut());
	public FourDriveOut<Percent> voltageDrive = new FourDriveOut<>(frontLeft.getVoltageOutput(),
			frontRight.getVoltageOutput(), rearLeft.getVoltageOutput(), rearRight.getVoltageOutput());

	public static RobotSoftware getInstance() {
		return INSTANCE;
	}

}
