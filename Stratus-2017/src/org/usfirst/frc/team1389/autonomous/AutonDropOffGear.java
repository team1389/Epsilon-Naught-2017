package org.usfirst.frc.team1389.autonomous;

import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.command_framework.CommandScheduler;
import com.team1389.command_framework.CommandUtil;
import com.team1389.trajectory.PathFollowingSystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

import jaci.pathfinder.Waypoint;

public class AutonDropOffGear extends AutoModeBase {

	RobotSoftware robot;
	CommandScheduler scheduler;
	PathFollowingSystem cont;

	public AutonDropOffGear(RobotSoftware robot) {
		this.robot = robot;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(robot.gyro, robot.voltageDrive);
	}

	@Override
	public String getName() {
		return "Drop off Gear";
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		Waypoint[] points = new Waypoint[] { new Waypoint(0, 30, 0), new Waypoint(-101, 56, Math.toRadians(300)) };
		Waypoint[] points2 = new Waypoint[] { new Waypoint(50, 50, 0), new Waypoint(-20, -20, 0) };

		scheduler.schedule(CommandUtil.combineSequential(cont.new PathFollowCommand(points, false, 180),
				CommandUtil.createCommand(robot.gyro::reset), cont.new PathFollowCommand(points2, false, 180)));
	}

}
