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

	// DRIVER CONTROLS
	private PercentIn yAxis = driveController.getAxis(ax_X_AXIS).applyDeadband(.075);
	public Supplier<PercentIn> i_yAxis = yAxis::copy;

	public PercentIn xAxis = driveController.getAxis(ax_Y_AXIS).applyDeadband(.075);
	public Supplier<PercentIn> i_xAxis = yAxis::copy;

	public PercentIn twistAxis = driveController.getAxis(ax_TWIST_AXIS).applyDeadband(.075);
	public Supplier<PercentIn> i_twistAxis = twistAxis::copy;
	
	public PercentIn trimAxis = driveController.getAxis(ax_TWIST_AXIS).applyDeadband(.075);
	public Supplier<PercentIn> i_trimAxis = trimAxis::copy;

	public DigitalIn trigger = driveController.getButton(btn_TRIGGER);
	public Supplier<DigitalIn> i_trigger = trigger::copy;
}
