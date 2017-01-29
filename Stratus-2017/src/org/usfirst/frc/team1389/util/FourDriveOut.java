package org.usfirst.frc.team1389.util;

import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.drive.DriveOut;
import com.team1389.system.drive.FourWheelSignal;

public class FourDriveOut<T extends Value> {
	public final DriveOut<T> front;
	public final DriveOut<T> rear;

	public FourDriveOut(DriveOut<T> front, DriveOut<T> rear) {
		this.front = front;
		this.rear = rear;
	}

	public FourDriveOut(RangeOut<T> frontLeft, RangeOut<T> frontRight, RangeOut<T> backLeft, RangeOut<T> backRight) {
		this(new DriveOut<T>(frontLeft, frontRight), new DriveOut<T>(backLeft, backRight));
	}

	public void set(FourWheelSignal driveSignal) {
		front.set(driveSignal.getTopWheels());
		rear.set(driveSignal.getBottomWheels());
	}
}
