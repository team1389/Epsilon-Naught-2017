package systems;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

/**
 * System responsible for Climber System
 * 
 * @author Quunii
 *
 */
public class ClimberSystem extends Subsystem
{

	protected PercentOut climberVoltageOut;
	protected RangeIn<Percent> rightTrigger;

	/**
	 * 
	 * @param trigger
	 *            governs how much voltage is assigned to climber
	 * @param climberCurrent
	 *            current draw of climber
	 * @param climberVoltageOut
	 *            applies voltage to climber motors
	 */
	public ClimberSystem(RangeIn<Percent> trigger, PercentOut climberVoltageOut)
	{
		this.rightTrigger = trigger;
		this.climberVoltageOut = climberVoltageOut;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return stem.put(climberVoltageOut.getWatchable("climber voltage"));
	}

	@Override
	public String getName()
	{
		return "climber";
	}

	@Override
	public void init()
	{

	}

	/**
	 * apply RangeIn Values to motorsS
	 */
	@Override
	public void update()
	{
		climberVoltageOut.set(rightTrigger.get());
		/*
		 * if(climberButton.get()){ climberVoltageOut.set(1); } else{
		 * climberVoltageOut.set(0); }
		 */
	}
}