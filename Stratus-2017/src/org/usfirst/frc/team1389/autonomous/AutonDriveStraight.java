package org.usfirst.frc.team1389.autonomous;

import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.command_framework.CommandScheduler;
import com.team1389.command_framework.CommandUtil;
import com.team1389.system.drive.FourWheelSignal;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class AutonDriveStraight extends AutoModeBase {
	CommandScheduler scheduler;
	RobotSoftware robot;

	public AutonDriveStraight(RobotSoftware robot) {
		this.robot = robot;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(robot.gyro, robot.voltageDrive);
	}

	@Override
	public String getName() {
		return "DRIVE STRAIGHT";
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		robot.gyro.reset();
		scheduler.schedule(CommandUtil.createCommand(() -> robot.voltageDrive.set(new FourWheelSignal(.25, .25, .25, .25))));

	}

}
