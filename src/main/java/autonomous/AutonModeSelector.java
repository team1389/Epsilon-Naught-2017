package autonomous;

import com.team1389.auto.AutoModeBase;

import robot.RobotSoftware;

/**
 * returns the selected auton
 * 
 * @author Quunii
 *
 */
public class AutonModeSelector
{
	/**
	 * 
	 * @param autonOption
	 *            the selected Auton
	 * @return the corresponding auton to that which is selected
	 */
	public static AutoModeBase createAutoMode(AutonOption autonOption)
	{
		if (autonOption == null)
		{
			return null;
		} else
		{
			return autonOption.setupAutoModeBase(RobotSoftware.getInstance());
		}
	}
}
