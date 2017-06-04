package org.usfirst.frc.team1389.autonomous.routines.experimental;

import org.usfirst.frc.team1389.autonomous.AutoConstants;
import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.auto.command.DrivePIDTuneCommand;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.NumberInfo;

import edu.wpi.first.wpilibj.Preferences;

public class DrivePidTune extends AutoModeBase {
	RobotSoftware robot;
	RobotCommands commands;
	DrivePIDTuneCommand pidTune;
	boolean halfOscillation;

	public DrivePidTune(RobotSoftware robot) {
		this.robot = robot;
		commands = new RobotCommands(robot);

	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(new NumberInfo("tuned drive P", () -> AutoConstants.driveConstants.p),
				new NumberInfo("tuned drive I", () -> AutoConstants.driveConstants.i),
				new NumberInfo("tuned drive D", () -> AutoConstants.driveConstants.d));
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		// make sure that distance is in rotations
		pidTune = commands.new StraightPIDTune(AutoConstants.DriveTuningP, 1, false);
		runCommand(pidTune);
		AutoConstants.driveConstants = pidTune.getZieglerNichols();
		Preferences.getInstance().putDouble("tuned drive P", AutoConstants.driveConstants.p);
		Preferences.getInstance().putDouble("tuned drive I",  AutoConstants.driveConstants.i);
		Preferences.getInstance().putDouble("tuned drive D", AutoConstants.driveConstants.d);
	}

	@Override
	public String getIdentifier() {
		return "DrivePIDTune";
	}

}
