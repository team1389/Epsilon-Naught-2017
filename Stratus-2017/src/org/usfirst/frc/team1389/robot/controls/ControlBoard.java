package org.usfirst.frc.team1389.robot.controls;

import java.util.function.Supplier;

import com.team1389.hardware.inputs.hardware.JoystickHardware;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;

/**
 * A basic framework for the robot controls. Like the RobotHardware, one instance of the
 * ControlBoard object is created upon startup, then other methods request the singleton
 * ControlBoard instance.
 * 
 * @author amind
 * @see ControlMap
 */
public class ControlBoard extends ControlMap {
	private static ControlBoard mInstance = new ControlBoard();

	public static ControlBoard getInstance() {
		return mInstance;
	}

	private ControlBoard() {
	}

	private final JoystickHardware driveController = new JoystickHardware(DRIVE_CONTROLLER);
	private final JoystickHardware manipController = new JoystickHardware(MANIP_CONTROLLER);

	// DRIVER CONTROLS
	private PercentIn yAxis = driveController.getAxis(ax_Y_AXIS).applyDeadband(.075).invert();
	public Supplier<PercentIn> i_yAxis = yAxis::copy;

	private PercentIn xAxis = driveController.getAxis(ax_X_AXIS).applyDeadband(.075);
	public Supplier<PercentIn> i_xAxis = xAxis::copy;

	public PercentIn twistAxis = driveController.getAxis(ax_TWIST_AXIS).applyDeadband(.075);
	public Supplier<PercentIn> i_twistAxis = twistAxis::copy;

	public PercentIn trimAxis = driveController.getAxis(ax_TRIM_AXIS).applyDeadband(.075);
	public Supplier<PercentIn> i_trimAxis = trimAxis::copy;

	public DigitalIn trigger = driveController.getButton(btn_TRIGGER);
	public Supplier<DigitalIn> i_trigger = trigger::copy;
	
	public DigitalIn rightTrigger = driveController.getButton(btn_RIGHT_TRIGGER);
	public Supplier<DigitalIn> i_right_trigger = rightTrigger::copy;

	public DigitalIn thumb = driveController.getButton(btn_THUMB).latched();
	public Supplier<DigitalIn> i_thumb = thumb::copy;

	public DigitalIn aButton = manipController.getButton(btn_A).latched();
	public Supplier<DigitalIn> i_aButton = aButton::copy;

	public DigitalIn yButton = manipController.getButton(btn_Y).latched();
	public Supplier<DigitalIn> i_yButton = yButton::copy;

	public DigitalIn xButton = manipController.getButton(btn_X).latched();
	public Supplier<DigitalIn> i_xButton = xButton::copy;

	public DigitalIn bButton = manipController.getButton(btn_B).latched();
	public Supplier<DigitalIn> i_bButton = bButton::copy;

	public DigitalIn leftBumper = manipController.getButton(btn_LEFT_BUMPER);

	public DigitalIn rightBumper = manipController.getButton(btn_RIGHT_BUMPER).latched();
	

	public PercentIn leftTriggerAxis = manipController.getAxis(ax_LEFT_TRIGGER).applyDeadband(.075);
	public Supplier<PercentIn> i_leftTriggerAxis = leftTriggerAxis::copy;

	public PercentIn leftVertAxis = manipController.getAxis(ax_LEFT_VERT_AXIS).applyDeadband(.075);
	public Supplier<PercentIn> i_leftVertAxis = leftVertAxis::copy;

}
