package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;

public class TeleopHopperSystem extends HopperSystem
{
	private DigitalIn triggerDump, resetHopper;

	public TeleopHopperSystem(DigitalOut dumper, DigitalOut gate, DigitalIn dumperEndstop, DigitalIn triggerDump,
			DigitalIn resetHopper)
	{
		super(dumper, gate, dumperEndstop);
		this.triggerDump = triggerDump;
		this.resetHopper = resetHopper;
	}

	@Override
	public void update()
	{
		super.update();
		if (triggerDump.get())
		{
			enterState(State.DUMPING);
		}
		if (resetHopper.get())
		{
			enterState(State.COLLECTING);
		}
	}
}
