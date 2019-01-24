package frc.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.BooleanInfo;

public class PassiveGearIntakeSystem extends Subsystem
{
	private DigitalOut flap;
	private DigitalIn flapButton;
	private boolean toggled;

	public PassiveGearIntakeSystem(DigitalOut flapControl, DigitalIn flapButton)
	{
		this.flap = flapControl;
		this.flapButton = flapButton;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return stem.put(new BooleanInfo("Toggled", () -> toggled));
	}

	@Override
	public String getName()
	{
		return "Passive gear system";
	}

	@Override
	public void init()
	{
		toggled = false;
	}

	@Override
	public void update()
	{
		toggled = toggled ^ flapButton.get();
		flap.set(toggled);
	}
}
