package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class VoltageGear extends Subsystem {
	private PercentIn axis;
	private PercentOut voltage;

	public VoltageGear(PercentIn axis, PercentOut voltage) {
		this.axis = axis;
		this.voltage = voltage;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(voltage.getWatchable("lastSetVoltage"),axis.getWatchable("axis val"));
	}

	@Override
	public String getName() {
		return "voltage gear";
	}

	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		voltage.set(axis.get());
	}

}
