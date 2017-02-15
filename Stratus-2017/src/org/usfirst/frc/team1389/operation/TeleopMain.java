package org.usfirst.frc.team1389.operation;

import org.usfirst.frc.team1389.robot.RobotSoftware;
import org.usfirst.frc.team1389.robot.controls.ControlBoard;
import org.usfirst.frc.team1389.systems.TeleopGearIntakeSystem;
import org.usfirst.frc.team1389.systems.OctoMecanumSystem;
import org.usfirst.frc.team1389.watchers.DebugDash;

import com.ctre.CANTalon.FeedbackDevice;
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
		Subsystem drive = new OctoMecanumSystem(robot.voltageDrive, robot.pistons, robot.gyroInput,
				controls.i_xAxis.get(), controls.i_yAxis.get(), controls.twistAxis, controls.trimAxis,
				controls.i_thumb.get(), controls.i_trigger.get());
		Subsystem gearIntake = setupGearIntake();
		manager = new SystemManager(drive, gearIntake);
		manager.init();
		DebugDash.getInstance().watch(gearIntake, robot.armElevator.getAbsoluteIn().getWatchable("absolute pos"),
				robot.pdp.getCurrentIn().getWatchable("total"), controls.aButton.getWatchable("button"),
				robot.armElevator.getSensorTracker(FeedbackDevice.CtreMagEncoder_Absolute).getWatchable("is sensor"));

	}

	private Subsystem setupGearIntake() {
		return new TeleopGearIntakeSystem(robot.armAngle, robot.armVel, robot.armElevator.getVoltageOutput(),
				robot.gearIntake.getVoltageOutput(), robot.gearIntakeCurrent, controls.i_aButton.get(),
				controls.i_yButton.get(), controls.i_xButton.get(), controls.i_bButton.get(),
				controls.i_leftVertAxis.get(), robot.armElevator.getSensorTracker(FeedbackDevice.CtreMagEncoder_Absolute).invert());
	}

	public void periodic() {
		manager.update();
	}
}
