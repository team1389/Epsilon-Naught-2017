package org.usfirst.frc.team1389.autonomous.routines;

import org.usfirst.frc.team1389.autonomous.AutoConstants;
import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.drive.DriveOut;
import com.team1389.system.drive.DriveSignal;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Timer;

/**
 * Auton to cross the baseline
 * 
 * @author Quunii
 *
 */
public class CrossBaselineOpenLoop extends AutoModeBase
{
	RobotSoftware robot;
	RobotCommands commands;

	/**
	 * 
	 * @param robot
	 *            container of all ohm streams
	 */
	public CrossBaselineOpenLoop(RobotSoftware robot)
	{
		this.robot = robot;
		commands = new RobotCommands(robot);
	}

	/**
	 * watches voltage being applied to every drive train motor
	 */
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return stem.put(robot.voltageDrive);
	}

	/**
	 * drives forwards over the baseline
	 */
	@Override
	protected void routine() throws AutoModeEndedException
	{
		System.out.println("crossing baseline");
		double voltage = 0.5;
		double waitTime = 1.3;
		DriveOut<Percent> asTank = robot.voltageDrive.getAsTank();

		asTank.set(voltage, voltage);
		Timer.delay(waitTime);
		asTank.set(DriveSignal.NEUTRAL);
	}

	/**
	 * id of this auton
	 */
	@Override
	public String getIdentifier()
	{
		return "Cross Baseline";
	}

}
