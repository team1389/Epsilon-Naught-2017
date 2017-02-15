package org.usfirst.frc.team1389.systems;

import java.util.function.Supplier;

import org.usfirst.frc.team1389.systems.GearIntakeSystem.State;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class BallIntakeSystem extends Subsystem{
	DigitalIn intakeButton;
	RangeOut<Percent> intakeVoltage;
	GearIntakeSystem.State state;
	public BallIntakeSystem(DigitalIn intakeButton,Supplier <GearIntakeSystem.State> state,RangeOut<Percent> intakeVoltage ){
		this.intakeButton = intakeButton.getLatched();
		this.intakeVoltage = intakeVoltage;
		this.state = state.get();
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		
		return stem.put(intakeButton.getWatchable("intakeButton"), intakeVoltage.getWatchable("BallIntakeMotor"));
	}

	@Override
	public String getName() {
		return "BallIntakeSystem";
	}

	@Override
	public void init() {
		
		
	}

	@Override
	public void update() {
		if(state == State.INTAKING || intakeButton.get()){
		intakeVoltage.set(0);
		}
		else{
			intakeVoltage.set(1);
		}

	}

		
		
		
	}

