package org.usfirst.frc.team1389.operation;

import java.util.function.Supplier;

import org.usfirst.frc.team1389.robot.RobotSoftware;
import org.usfirst.frc.team1389.robot.controls.ControlBoard;
import org.usfirst.frc.team1389.systems.BallIntakeSystem;
import org.usfirst.frc.team1389.systems.ClimberSystem;
import org.usfirst.frc.team1389.systems.GearIntakeSystem;
import org.usfirst.frc.team1389.systems.OctoMecanumSystem;
import org.usfirst.frc.team1389.systems.TeleopGearIntakeSystem;
import org.usfirst.frc.team1389.watchers.DebugDash;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.system.Subsystem;
import com.team1389.system.SystemManager;

public class TeleopMain {
	SystemManager manager;
	ControlBoard controls;
	RobotSoftware robot;
	DigitalIn thumb;
	Supplier<GearIntakeSystem.State> state;

	public TeleopMain(RobotSoftware robot) {
		this.robot = robot;
	}

	public void init() {

		controls = ControlBoard.getInstance();
		Subsystem drive = new OctoMecanumSystem(robot.voltageDrive, robot.pistons, robot.gyroInput,
				controls.i_xAxis.get(), controls.i_yAxis.get(), controls.twistAxis, controls.trimAxis,
				controls.i_thumb.get(), controls.i_trigger.get());
		Subsystem gearIntake = setupGearIntake();
		Subsystem ballIntake = setUpBallIntake();
		Subsystem climbing = setUpClimbing();
		
		manager = new SystemManager(drive, gearIntake, ballIntake, climbing);
		manager.init();
		DebugDash.getInstance().watch(gearIntake, robot.armElevator.getAbsoluteIn().getWatchable("absolute pos"),
				robot.pdp.getCurrentIn().getWatchable("total"), controls.aButton.getWatchable("button"));

	}


	private Subsystem setupGearIntake() {

		TeleopGearIntakeSystem Supplier = new TeleopGearIntakeSystem(robot.armAngle, robot.armVel,
				robot.armElevator.getVoltageOutput(),

				robot.gearIntake.getVoltageOutput(), robot.gearIntakeCurrent, controls.i_aButton.get(),
				controls.i_yButton.get(), controls.i_xButton.get(), controls.i_bButton.get(),
				controls.i_leftVertAxis.get());
		state = () -> Supplier.getState();
		// robot.armElevator.getSensorTracker(FeedbackDevice.CtreMagEncoder_Absolute));
		return Supplier;
	}

	private Subsystem setUpBallIntake() {
		return new BallIntakeSystem(controls.trigger, state, robot.ballVoltageOut);

	}
	
	private ClimberSystem setUpClimbing() {
		return new ClimberSystem(controls.leftTriggerAxis, robot.climberCurrent, robot.climberVoltageOut);
	}

	public void periodic() {
		manager.update();

	}
}
