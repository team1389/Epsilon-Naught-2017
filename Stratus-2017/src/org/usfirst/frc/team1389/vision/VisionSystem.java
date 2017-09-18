package org.usfirst.frc.team1389.vision;

import java.util.Optional;
import java.util.function.Supplier;

import org.usfirst.frc.team1389.systems.GearIntakeSystem;
import org.usfirst.frc.team1389.systems.GearIntakeSystem.State;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.Subsystem;
import com.team1389.trajectory.Translation2d;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class VisionSystem extends Subsystem
{
	private DigitalOut visionLight;
	private PiVisionManager pi;
	private Supplier<GearIntakeSystem.State> intakeState;
	private RangeIn<Position> distanceSensor;
	private DigitalIn visionEnableButton;

	public VisionSystem(DigitalOut visionLight, PiVisionManager pi, Supplier<GearIntakeSystem.State> intakeState,
			RangeIn<Position> distanceSensor, DigitalIn visionEnable)
	{
		this.visionLight = visionLight;
		this.pi = pi;
		this.distanceSensor = distanceSensor;
		this.intakeState = intakeState;
		this.visionEnableButton = visionEnable;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return pi.getSubWatchables();
	}

	@Override
	public String getName()
	{
		return "Vision";
	}

	@Override
	public void init()
	{

	}

	@Override
	public void update()
	{
		State state = intakeState.get();
		boolean visEnabled = true;
		if (visionEnableButton.get())
		{
			visEnabled = true;
		} else if (state == State.CARRYING)
		{
			visEnabled = true;
		} else if (state == State.STOWED || state == State.INTAKING)
		{
			visEnabled = false;
		}
		setVisionTracking(visEnabled);
	}

	public Translation2d getTargetPose()
	{
		Optional<Double> angle = pi.getRawAngle();
		double dist = distanceSensor.get();
		if (angle.isPresent())
		{
			return VisionAnalyst.getDesiredMotion(angle.get(), dist);
		} else
		{
			return new Translation2d();
		}
	}

	public void setVisionTracking(boolean val)
	{
		pi.setProcessing(val);
		visionLight.set(val);
	}

	public void enableVisionTracking()
	{
		setVisionTracking(true);
	}

	public void disableVisionTracking()
	{
		setVisionTracking(false);
	}

}
