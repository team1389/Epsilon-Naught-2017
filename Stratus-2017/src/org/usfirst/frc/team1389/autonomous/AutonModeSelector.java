package org.usfirst.frc.team1389.autonomous;

import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;

/**
 * returns the selected auton
 * 
 * @author Quunii
 *
 */
public class AutonModeSelector {
	/**
	 * 
	 * @param autonOption
	 *            the selected Auton
	 * @return the corresponding auton to that which is selected
	 */
	public static AutoModeBase createAutoMode(AutonOption autonOption) {
		return autonOption.setupAutoModeBase(RobotSoftware.getInstance());
		
	}
}
