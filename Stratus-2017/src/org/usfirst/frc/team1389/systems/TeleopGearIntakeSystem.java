package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;

public class TeleopGearIntakeSystem extends GearIntakeSystem {
	private DigitalIn intakeGearButton;
	private DigitalIn prepareGearButton;
	private DigitalIn placeGearButton;
	private DigitalIn stowGearButton;

	public TeleopGearIntakeSystem(AngleIn<Position> armAngle, AngleIn<Speed> armVel, PercentOut armVoltage,
			PercentOut intakeVoltage, RangeIn<Value> intakeCurrent, DigitalIn intakeGearButton,
			DigitalIn prepareGearButton, DigitalIn placeGearButton, DigitalIn stowGearButton) {
		super(armAngle, armVel, armVoltage, intakeVoltage, intakeCurrent);
		this.intakeGearButton = intakeGearButton;
		this.prepareGearButton = prepareGearButton;
		this.placeGearButton = placeGearButton;
		this.stowGearButton = stowGearButton;
	}

	@Override
	public void update() {
		if (intakeGearButton.get()) {
			enterState(state == State.INTAKING ? State.CARRYING : State.INTAKING);
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
