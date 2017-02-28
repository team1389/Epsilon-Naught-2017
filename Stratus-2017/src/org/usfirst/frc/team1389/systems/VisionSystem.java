package org.usfirst.frc.team1389.systems;

import java.util.Optional;
import java.util.function.Supplier;

import org.usfirst.frc.team1389.systems.GearIntakeSystem.State;
import org.usfirst.frc.team1389.vision.PiVisionManager;
import org.usfirst.frc.team1389.vision.VisionAnalyst;

import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.value_types.Position;
import com.team1389.system.Subsystem;
import com.team1389.trajectory.Translation2d;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class VisionSystem extends Subsystem {
	private DigitalOut visionLight;
	private PiVisionManager pi;
	private Supplier<GearIntakeSystem.State> intakeState;
	private RangeIn<Position> distanceSensor;

	public VisionSystem(DigitalOut visionLight, PiVisionManager pi, Supplier<GearIntakeSystem.State> intakeState,
			RangeIn<Position> distanceSensor) {
		this.visionLight = visionLight;
		this.pi = pi;
		this.distanceSensor = distanceSensor;
		this.intakeState = intakeState;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return pi.getSubWatchables();
	}

	@Override
	public String getName() {
		return "vision";
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {
		if (intakeState.get() == State.CARRYING) {
			enableVisionTracking();
		}
	}

	public Translation2d getTargetPose() {
		Optional<Double> angle = pi.getRawAngle();
		double dist = distanceSensor.get();
		if (angle.isPresent()) {
			return VisionAnalyst.getDesiredMotion(angle.get(), dist);
		} else {
			return new Translation2d();
		}
	}

	public void enableVisionTracking() {
		pi.setProcessing(true);
		visionLight.set(true);
	}

	public void disableVisionTracking() {
		pi.setProcessing(false);
		visionLight.set(false);
	}

}
