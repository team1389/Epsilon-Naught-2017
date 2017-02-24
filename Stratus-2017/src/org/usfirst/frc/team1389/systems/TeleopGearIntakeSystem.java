package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class TeleopGearIntakeSystem extends GearIntakeSystem {
	private DigitalIn intakeGearButton;
	private DigitalIn prepareGearButton;
	private DigitalIn placeGearButton;
	private DigitalIn stowGearButton;
	private PercentOut armVoltage;
	private PercentIn armManualAxis;
	private DigitalIn sensorFailure;
	private boolean intakeRunning;

	public TeleopGearIntakeSystem(AngleIn<Position> armAngle, AngleIn<Speed> armVel, PercentOut armVoltage,
			PercentOut intakeVoltage, RangeIn<Value> intakeCurrent, DigitalIn intakeGearButton,
			DigitalIn prepareGearButton, DigitalIn placeGearButton, DigitalIn stowGearButton, PercentIn armManualAxis,
			DigitalIn sensorFailure) {
		super(armAngle, armVel, armVoltage, intakeVoltage, intakeCurrent);
		this.intakeGearButton = intakeGearButton;
		this.prepareGearButton = prepareGearButton;
		this.placeGearButton = placeGearButton;
		this.stowGearButton = stowGearButton;
		this.armVoltage = armVoltage;
		this.armManualAxis = armManualAxis;
		this.sensorFailure = sensorFailure;
	}

	public TeleopGearIntakeSystem(AngleIn<Position> armAngle, AngleIn<Speed> armVel, PercentOut armVoltage,
			PercentOut intakeVoltage, RangeIn<Value> intakeCurrent, DigitalIn intakeGearButton,
			DigitalIn prepareGearButton, DigitalIn placeGearButton, DigitalIn stowGearButton, PercentIn armManualAxis) {
		this(armAngle, armVel, armVoltage, intakeVoltage, intakeCurrent, intakeGearButton, prepareGearButton,
				placeGearButton, stowGearButton, armManualAxis, new DigitalIn(() -> false));

	}

	@Override
	public void update() {
		if (sensorFailure.get()) {
			armVoltage.set(armManualAxis.get());
			intakeRunning = intakeGearButton.get() ^ intakeRunning;
			if(intakeRunning){
				setState(State.INTAKING);
			}
			else{
				setState(State.CARRYING);
			}
			intakeVoltageOut.set(intakeRunning ? 1 : 0.0);
		} else {
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
	}

	public void addSensorFailurePoint(DigitalIn... sensorFailFlags) {
		sensorFailure = sensorFailure.combineOR(sensorFailFlags);
	}
	
	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return super.getSubWatchables(stem).put(intakeGearButton.getWatchable("intake gear button"),sensorFailure.getWatchable("sensor state"));
	}
}
