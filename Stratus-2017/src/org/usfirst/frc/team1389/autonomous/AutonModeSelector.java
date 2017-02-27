package org.usfirst.frc.team1389.autonomous;

import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;

public class AutonModeSelector {
	public static AutoModeBase createAutoMode(AutonOption autonOption) {
		switch (autonOption) {
		case DRIVE_STRAIGHT:
			return new DriveStraightDropOffGear(RobotSoftware.getInstance());
		default:
			System.out.println("ERROR: unexpected auto mode: " + autonOption);
			return null;
		}
	}
}
