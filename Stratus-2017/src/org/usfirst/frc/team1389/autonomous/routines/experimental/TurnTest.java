package org.usfirst.frc.team1389.autonomous.routines.experimental;

import org.usfirst.frc.team1389.autonomous.command.RobotCommands;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class TurnTest extends AutoModeBase {
	RobotCommands commands;

	public TurnTest(RobotSoftware robot) {
		this.commands = new RobotCommands(robot);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem;
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		runCommand(commands.new TurnAngle(90, false));
	}

	@Override
	public String getIdentifier() {
		return "Turn Test";
	}

}
