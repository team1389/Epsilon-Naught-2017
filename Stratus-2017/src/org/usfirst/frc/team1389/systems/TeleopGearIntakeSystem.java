package org.usfirst.frc.team1389.systems;

import java.util.function.Function;
import java.util.function.Supplier;

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
import com.team1389.system.drive.OctoMecanumSystem.DriveMode;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

import edu.wpi.first.wpilibj.Preferences;

public class TeleopGearIntakeSystem extends GearIntakeSystem
{
	public static final boolean USE_MANUAL = false;
	public static final double RUMBLE_TIME = 1.5;

	private DigitalIn intakeGearButton;
	private DigitalIn prepareGearButton;
	private DigitalIn placeGearButton;
	private DigitalIn stowGearButton;
	private PercentOut armVoltage;
	private PercentIn armManualAxis;
	private DigitalIn sensorFailure;
	private DigitalOut rumble;
	private DigitalIn useManualButton;
	private PercentIn outtakeManualAxis;

	private boolean intakeRunning, userManualTrigger, failure, currentlyInManual;
	

	public TeleopGearIntakeSystem(AngleIn<Position> armAngle, AngleIn<Speed> armVel, PercentOut armVoltage,
			PercentOut intakeVoltage, DigitalIn beamBreak, RangeIn<Value> intakeCurrent, Supplier<DriveMode> driveMode, DigitalIn intakeGearButton,
			DigitalIn prepareGearButton, DigitalIn placeGearButton, DigitalIn stowGearButton, PercentIn armManualAxis,
			PercentIn outtakeManualAxis, DigitalOut rumble, DigitalIn useManualButton, DigitalIn sensorFailure)
	{
		super(armAngle, armVel, armVoltage, intakeVoltage, beamBreak, intakeCurrent,driveMode);
		this.intakeGearButton = intakeGearButton;
		this.prepareGearButton = prepareGearButton;
		this.placeGearButton = placeGearButton;
		this.stowGearButton = stowGearButton;
		this.armVoltage = armVoltage;
		this.armManualAxis = armManualAxis;
		this.sensorFailure = sensorFailure;
		this.rumble = rumble;
		this.useManualButton = useManualButton;
		this.outtakeManualAxis = outtakeManualAxis;
		failure = false;
		userManualTrigger = false; 
		intakeRunning = false;
		currentlyInManual = false;
	}

	public TeleopGearIntakeSystem(AngleIn<Position> armAngle, AngleIn<Speed> armVel, PercentOut armVoltage,
			PercentOut intakeVoltage, DigitalIn beamBreak, RangeIn<Value> intakeCurrent, Supplier<DriveMode> driveMode, DigitalIn intakeGearButton,
			DigitalIn prepareGearButton, DigitalIn placeGearButton, DigitalIn stowGearButton, PercentIn armManualAxis,
			PercentIn outtakeManualAxis, DigitalOut rumble, DigitalIn manualTrigger)
	{
		this(armAngle, armVel, armVoltage, intakeVoltage, beamBreak, intakeCurrent, driveMode,intakeGearButton, prepareGearButton,
				placeGearButton, stowGearButton, armManualAxis, outtakeManualAxis, rumble, manualTrigger,
				new DigitalIn(() -> false));

	}

	@SuppressWarnings("unused")
	@Override
	public void update()
	{
		System.out.println("updating thing");
		userManualTrigger = userManualTrigger ^ useManualButton.get();

		if (sensorFailure.get())
		{
			failure = true;
		}
		currentlyInManual = Preferences.getInstance().getBoolean("manual", false) || USE_MANUAL || userManualTrigger
				|| failure;
		if (currentlyInManual)
		{
			updateManualMode();

		} else
		{
			updateAdvancedMode();
		}

	}

	public DigitalIn getIsManualMode()
	{
		return new DigitalIn(() -> currentlyInManual);
	}

	private void updateManualMode()
	{
		armVoltage.set(armManualAxis.get());
		intakeRunning = intakeGearButton.get() ^ intakeRunning;
		if (intakeRunning)
		{
			setState(State.INTAKING);
		} else
		{
			setState(State.CARRYING);
		}
		rumble.set(intakeRunning && hasGear());

		intakeVoltageOut.set(intakeRunning ? -1 : outtakeManualAxis.get() / 2);

	}

	private void updateAdvancedMode()
	{
		if (intakeGearButton.get())
		{
			enterState(State.INTAKING);
		}
		if (prepareGearButton.get())
		{
			enterState(State.ALIGNING);
		}
		if (placeGearButton.get())
		{
			enterState(State.PLACING);
		}
		if (stowGearButton.get())
		{
			enterState(State.STOWED);
		}
		super.update();
	}

	private Command rumbleCommand(double duration)
	{
		Function<Boolean, Command> rumbler = val -> CommandUtil.createCommand(() -> rumble.set(val));
		return CommandUtil.combineSequential(rumbler.apply(true), new WaitTimeCommand(duration), rumbler.apply(false));
	}

	public void addSensorFailurePoint(DigitalIn... sensorFailFlags)
	{
		sensorFailure = sensorFailure.combineOR(sensorFailFlags);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return super.getSubWatchables(stem).put(sensorFailure.copy().invert().getWatchable("sensor state"),
				getIsManualMode().getWatchable("manual mode"));
	}
}
