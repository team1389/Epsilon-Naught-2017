package org.usfirst.frc.team1389.systems;

import java.util.function.Function;

import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class TeleopGearIntakeSystem extends GearIntakeSystem {
	public static final boolean USE_MANUAL = true;
	public static final double RUMBLE_TIME = 1.5;

	private DigitalIn intakeGearButton;
	private DigitalIn prepareGearButton;
	private DigitalIn placeGearButton;
	private DigitalIn stowGearButton;
	private PercentOut armVoltage;
	private PercentIn armManualAxis;
	private DigitalIn sensorFailure;
	private DigitalOut rumble;

	private boolean intakeRunning, failure;

	public TeleopGearIntakeSystem(AngleIn<Position> armAngle, AngleIn<Speed> armVel, PercentOut armVoltage,
			PercentOut intakeVoltage, RangeIn<Value> intakeCurrent, DigitalIn intakeGearButton,
			DigitalIn prepareGearButton, DigitalIn placeGearButton, DigitalIn stowGearButton, PercentIn armManualAxis,
			DigitalIn sensorFailure, DigitalOut rumble) {
		super(armAngle, armVel, armVoltage, intakeVoltage, intakeCurrent);
		this.intakeGearButton = intakeGearButton;
		this.prepareGearButton = prepareGearButton;
		this.placeGearButton = placeGearButton;
		this.stowGearButton = stowGearButton;
		this.armVoltage = armVoltage;
		this.armManualAxis = armManualAxis;
		this.sensorFailure = sensorFailure;
		failure = false;
		this.rumble = rumble;
	}

	public TeleopGearIntakeSystem(AngleIn<Position> armAngle, AngleIn<Speed> armVel, PercentOut armVoltage,
			PercentOut intakeVoltage, RangeIn<Value> intakeCurrent, DigitalIn intakeGearButton,
			DigitalIn prepareGearButton, DigitalIn placeGearButton, DigitalIn stowGearButton, PercentIn armManualAxis,
			DigitalOut rumble) {
		this(armAngle, armVel, armVoltage, intakeVoltage, intakeCurrent, intakeGearButton, prepareGearButton,
				placeGearButton, stowGearButton, armManualAxis, new DigitalIn(() -> false), rumble);

	}

	@SuppressWarnings("unused")
	@Override
	public void update() {
		if (sensorFailure.get()) {
			failure = true;
		}

		if (USE_MANUAL || failure) {
			updateManualMode();
		} else {
			updateAdvancedMode();
		}

	}

	private void updateManualMode() {
		armVoltage.set(armManualAxis.get());
		intakeRunning = intakeGearButton.get() ^ intakeRunning;
		if (intakeRunning) {
			setState(State.INTAKING);
		} else {
			setState(State.CARRYING);
		}

		boolean gearIn = intakeCurrentDraw.get() > 55;
		if (gearIn) {
			rumble.set(true);
		}

		intakeVoltageOut.set(intakeRunning ? -1 : 0.0);

	}

	private void updateAdvancedMode() {
		if (intakeGearButton.get()) {
			System.out.println("enter intake mode");
			enterState(State.INTAKING);
		}
		if (prepareGearButton.get()) {
			enterState(State.ALIGNING);
		}
		if (placeGearButton.get()) {
			enterState(State.PLACING);
		}
		if (stowGearButton.get()) {
			enterState(State.STOWED);
		}
		super.update();
	}

	private Command getRumbleCommand() {
		Function<Boolean, Command> rumbler = val -> CommandUtil.createCommand(() -> rumble.set(val));
		return CommandUtil.combineSequential(rumbler.apply(true), new WaitTimeCommand(RUMBLE_TIME),
				rumbler.apply(false));
	}

	public void addSensorFailurePoint(DigitalIn... sensorFailFlags) {
		sensorFailure = sensorFailure.combineOR(sensorFailFlags);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return super.getSubWatchables(stem).put(intakeGearButton.getWatchable("intake gear button"),
				sensorFailure.getWatchable("sensor state"), armVoltage.getWatchable("gear arm voltage"),
				armManualAxis.getWatchable("controler arm axis"));
	}
}
