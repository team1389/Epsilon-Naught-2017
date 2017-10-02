package org.usfirst.frc.team1389.watchers;

import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.command_framework.command_base.Command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class WebDashWatcher extends Command
{
	NetworkTable dashTable;
	RobotSoftware robot;

	public WebDashWatcher(RobotSoftware robot)
	{
		this.robot = robot;
	}

	private enum WebDashKeys
	{
		TIME("time_running"), FRONT_LEFT_CURRENT("front-left-current"), BACK_LEFT_CURRENT(
				"back-left-current"), FRONT_RIGHT_CURRENT(
						"front-right-current"), BACK_RIGHT_CURRENT("back-right-current"), GYRO_ANGLE("gyro-position");
		protected final String key;

		private WebDashKeys(String key)
		{
			this.key = key;
		}
	}

	public void initialize()
	{
		dashTable = NetworkTable.getTable("WebDashboard");
	}

	public boolean execute()
	{
		dashTable.putNumber(WebDashKeys.FRONT_LEFT_CURRENT.key, robot.flCurrent.get());
		dashTable.putNumber(WebDashKeys.BACK_LEFT_CURRENT.key, robot.blCurrent.get());
		dashTable.putNumber(WebDashKeys.FRONT_RIGHT_CURRENT.key, robot.frCurrent.get());
		dashTable.putNumber(WebDashKeys.BACK_RIGHT_CURRENT.key, robot.brCurrent.get());
		dashTable.putNumber(WebDashKeys.GYRO_ANGLE.key, robot.gyroInput.get());
		dashTable.putNumber(WebDashKeys.TIME.key, Timer.getFPGATimestamp());

		return false;
	}
}
