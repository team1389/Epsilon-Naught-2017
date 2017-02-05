package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.robot.RobotSoftware;
import org.usfirst.frc.team1389.robot.controls.ControlBoard;
import org.usfirst.frc.team1389.systems.OctoMecanumSystem;
import org.usfirst.frc.team1389.watchers.DebugDash;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.system.Subsystem;
import com.team1389.system.SystemManager;

public class TeleopMain {
	SystemManager manager;
	ControlBoard controls;
	RobotSoftware robot;
	DigitalIn thumb;

	public TeleopMain(RobotSoftware robot) {
		this.robot = robot;
	}

	public void init() {
		controls = ControlBoard.getInstance();
		// Subsystem drive = new CurvatureDriveSystem(robot.voltageDrive.getAsTank(),
		// controls.i_yAxis.get(),
		// controls.i_xAxis.get(), controls.i_trigger.get());
	//	Subsystem drive = new MecanumDriveSystem(controls.i_xAxis.get(), controls.i_yAxis.get().invert(),
	//			controls.i_twistAxis.get(), robot.voltageDrive, robot.gyroInput, controls.i_trigger.get());
	//	thumb = controls.i_thumb.get();
		Subsystem drive = new OctoMecanumSystem(robot.voltageDrive, robot.pistons, robot.gyroInput, controls.i_xAxis.get(), controls.i_yAxis.get(), controls.twistAxis, controls.trimAxis, controls.i_thumb.get(), controls.i_trigger.get());
		manager = new SystemManager(drive);
		manager.init();
		DebugDash.getInstance().watch(drive);

	}


	public void periodic() {
		manager.update();
	}
}
