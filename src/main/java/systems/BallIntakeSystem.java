package systems;;

import java.util.function.Supplier;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.BooleanInfo;

/**
 * system responsible for ball intake
 * 
 * @author Quunii
 *
 */
public class BallIntakeSystem extends Subsystem
{
	DigitalIn intakeButton;
	RangeOut<Percent> intakeVoltage;
	Supplier<GearIntakeSystem.State> gearIntakeState;
	private boolean intaking = false;

	/**
	 * 
	 * @param intakeButton
	 *            controls when ball intake is running
	 * @param state
	 *            Supplier of GearIntakeSystem state
	 * @param intakeVoltage
	 *            voltage setter for ball intake motors
	 */
	public BallIntakeSystem(DigitalIn intakeButton, Supplier<GearIntakeSystem.State> state,
			RangeOut<Percent> intakeVoltage)
	{
		this.intakeButton = intakeButton;
		this.intakeVoltage = intakeVoltage;
		this.gearIntakeState = state;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return stem.put(intakeVoltage.getWatchable("BallIntakeMotor"), new BooleanInfo("intaking", () -> intaking));
	}

	@Override
	public String getName()
	{
		return "BallIntakeSystem";
	}

	@Override
	public void init()
	{
	}

	/**
	 * toggles ball intake motors on and off based on button, disables it when
	 * GearIntake is in State Intake
	 */
	@Override
	public void update()
	{
		if (intakeButton.get())
		{
			intaking = !intaking;
		}
		if (gearIntakeState.get() == GearIntakeSystem.State.INTAKING)
		{
			intaking = false;
		}
		intakeVoltage.set(intaking ? 1 : 0);
	}

}
