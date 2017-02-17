package org.usfirst.frc.team1389.systems;



import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class ClimberSystem extends Subsystem {

	protected RangeIn<Value> climberCurrent;
	protected PercentOut climberVoltageOut;
	protected RangeIn<Percent> rightTrigger;

	public ClimberSystem(RangeIn<Percent> trigger, RangeIn<Value> climberCurrent, PercentOut climberVoltageOut) {
		this.rightTrigger = trigger;
		this.climberCurrent = climberCurrent;
		this.climberVoltageOut = climberVoltageOut;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(rightTrigger.getWatchable("climberButton"), climberCurrent.getWatchable("climberCurrent"),
				climberVoltageOut.getWatchable("climberVoltage"));
	}

	@Override
	public String getName() {
		return "climber";
	}

	@Override
	public void init() {
	
	}
	


	@Override
	public void update() {
		climberVoltageOut.set(rightTrigger.get());
		/*if(climberButton.get()){
			climberVoltageOut.set(1);
		}
		else{
			climberVoltageOut.set(0);
		}
	*/
}
}