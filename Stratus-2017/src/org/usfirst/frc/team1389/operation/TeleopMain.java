package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.robot.RobotSoftware;
import org.usfirst.frc.team1389.robot.controls.ControlBoard;
import org.usfirst.frc.team1389.watchers.DebugDash;

import com.team1389.system.SystemManager;

public class TeleopMain {
	SystemManager manager;
	ControlBoard controls;
	RobotSoftware robot;

	public TeleopMain(RobotSoftware robot) {
		this.robot = robot;
	}

	public void init() {
		controls = ControlBoard.getInstance();

		manager = new SystemManager(/* Add your subsystems here */);
		manager.init();
		DebugDash.getInstance().watch(/* watch any Ohm object! */);

	}

	public void periodic() {
		manager.update();
	}
}
