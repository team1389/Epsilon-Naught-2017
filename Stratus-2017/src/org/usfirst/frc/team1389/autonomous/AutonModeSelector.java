package org.usfirst.frc.team1389.autonomous;

import org.usfirst.frc.team1389.autonomous.passive_gear.PlaceGearCenter;
import org.usfirst.frc.team1389.autonomous.passive_gear.PlaceGearLeft;
import org.usfirst.frc.team1389.autonomous.passive_gear.PlaceGearRight;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;

public class AutonModeSelector {
	public static AutoModeBase createAutoMode(AutonOption autonOption) {
		switch (autonOption) {
		case CROSS_BASELINE:
			return new CrossBaseline(RobotSoftware.getInstance());
		case PLACE_GEAR_LEFT_PASSIVE:
			return new PlaceGearLeft(RobotSoftware.getInstance());
		case PLACE_GEAR_RIGHT_PASSIVE:
			return new PlaceGearRight(RobotSoftware.getInstance());
		case PLACE_GEAR_CENTER_PASSIVE:
			return new PlaceGearCenter(RobotSoftware.getInstance());
		default:
			System.out.println("ERROR: unexpected auto mode: " + autonOption);
			return null;
		}
	}
}
