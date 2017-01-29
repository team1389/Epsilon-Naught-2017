package org.usfirst.frc.team1389.systems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.usfirst.frc.team1389.util.FourDriveOut;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.outputs.software.RangeOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.Subsystem;
import com.team1389.system.drive.CurvatureDriveAlgorithm;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class OctoMecanumSystem extends Subsystem {
	private DriveMode currentMode;
	private CurvatureDriveAlgorithm tankCalc;
	
	private DigitalOut switcherPistons;
	private DigitalIn switcherSensors;
	private FourDriveOut<Percent> voltageDrive;
	private FourDriveOut<Speed> speedDrive;
	private RangeOut<Value> airPressure;

	private DigitalIn switchTrigger;
	private PercentIn xAxis;
	private PercentIn yAxis;
	private PercentIn twistAxis;

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return null;
	}

	public enum DriveMode {
		TANK(false), MECANUM(true);
		public final boolean solenoidVal;

		DriveMode(boolean solenoidVal) {
			this.solenoidVal = solenoidVal;
		}
	}

	@Override
	public String getName() {
		return "Drive";
	}

	@Override
	public void init() {

	}

	private Supplier<Boolean> isInDesiredMode(DriveMode desiredMode) {
		return () -> switcherSensors.get() == desiredMode.solenoidVal;
	}

	private void enterMode(DriveMode mode) {
		// TODO add air pressure tracking
		if (currentMode != mode) {
			Runnable sendSwitchRequest = () -> switcherPistons.set(mode.solenoidVal);
			Command waitForSwitch = CommandUtil.createCommand(isInDesiredMode(mode));
			Runnable updateCurrentMode = () -> currentMode = mode;
			CompletableFuture.runAsync(sendSwitchRequest).thenRun(() -> CommandUtil.executeCommand(waitForSwitch, 15))
					.thenRun(updateCurrentMode);
		}
	}

	@Override
	public void update() {
		if (switchTrigger.get()) {
			DriveMode desired = currentMode == DriveMode.TANK ? DriveMode.MECANUM : DriveMode.TANK;
			enterMode(desired);
		}
	}

}
