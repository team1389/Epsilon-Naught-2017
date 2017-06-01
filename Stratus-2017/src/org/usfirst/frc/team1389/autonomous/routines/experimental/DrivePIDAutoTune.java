package org.usfirst.frc.team1389.autonomous.routines.experimental;

import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.util.Timer;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class DrivePIDAutoTune extends AutoModeBase{
RobotSoftware robot;
RobotCommands commands;
Timer timer;
boolean halfOscillation;
	public DrivePIDAutoTune(RobotSoftware robot){
		this.robot = robot;
		commands = new RobotCommands(robot);
		timer = new Timer();
		
	}
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

}
