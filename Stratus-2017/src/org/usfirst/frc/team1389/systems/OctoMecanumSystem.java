package org.usfirst.frc.team1389.systems;

import java.util.Arrays;

import org.usfirst.frc.team1389.robot.controls.ControlMap;
import org.usfirst.frc.team1389.util.VerifiedSwitcher;

import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.Subsystem;
import com.team1389.system.drive.CurvatureDriveSystem;
import com.team1389.system.drive.DriveOut;
import com.team1389.system.drive.FourDriveOut;
import com.team1389.system.drive.MecanumDriveSystem;
import com.team1389.util.bezier.BezierCurve;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.StringInfo;

public class OctoMecanumSystem extends Subsystem {
	private DriveMode currentMode;
	private CurvatureDriveSystem tank;
	private MecanumDriveSystem mecanum;
	private VerifiedSwitcher octoShifter;
	private DigitalIn switchModes;
	private FourDriveOut<Percent> voltageDrive;
	private FourDriveOut<Speed> speedDrive;
	private RangeIn<Value> airPressure;

	public OctoMecanumSystem(FourDriveOut<Percent> voltageDrive, FourDriveOut<Speed> speedDrive,
			VerifiedSwitcher octoShifter, RangeIn<Value> airPressure, AngleIn<Position> gyro, PercentIn xAxis,
			PercentIn yAxis, PercentIn twist, PercentIn trim, DigitalIn switchModes, DigitalIn trigger) {
		this.airPressure = airPressure;
		this.voltageDrive = voltageDrive;
		this.speedDrive = speedDrive;
		this.octoShifter = octoShifter;
		this.switchModes = switchModes;
		setupTankDriveSystem(voltageDrive.getAsTank(), xAxis, yAxis, trim, trigger);
		setupMecanumDriveSystem(voltageDrive, xAxis, yAxis, twist, trigger, gyro);
	}

	private void setupTankDriveSystem(DriveOut<Percent> drive, PercentIn xAxis, PercentIn yAxis, PercentIn trim,
			DigitalIn quickTurn) {
		BezierCurve xCurve = new BezierCurve(0, .5, .79, -0.06);
		BezierCurve yCurve = new BezierCurve(.0, 0.54, 0.45, -0.07);
		xAxis.map(d -> xCurve.getPoint(d).getY());
		yAxis.map(d -> yCurve.getPoint(d).getY());
		tank = new CurvatureDriveSystem(drive, yAxis, xAxis, quickTurn, ControlMap.turnSensitivity,
				ControlMap.turnSensitivity);
	}

	private void setupMecanumDriveSystem(FourDriveOut<Percent> wheels, PercentIn xAxis, PercentIn yAxis,
			PercentIn twistAxis, DigitalIn toggleFOD, AngleIn<Position> gyro) {
		mecanum = new MecanumDriveSystem(xAxis, yAxis, twistAxis, wheels, gyro, toggleFOD);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(voltageDrive, airPressure.getWatchable("air pressure"),
				octoShifter.getSwitcherInput().getWatchable("pistonState"),
				new StringInfo("drive mode", currentMode::name));
	}

	public enum DriveMode {
		TANK(false), MECANUM(true);
		public final boolean solenoidVal;

		DriveMode(boolean solenoidVal) {
			this.solenoidVal = solenoidVal;
		}

		public DriveMode getFromBoolean(boolean value) {
			return Arrays.stream(DriveMode.values()).filter(m -> m.solenoidVal).findFirst().orElse(TANK);
		}
	}

	@Override
	public String getName() {
		return "Drive";
	}

	@Override
	public void init() {
		setMode(DriveMode.TANK);
	}

	private void switchModes() {
		octoShifter.set(!octoShifter.get());
	}

	private void setMode(DriveMode mode) {
		octoShifter.set(mode.solenoidVal);
	}

	@Override
	public void updateTeleop() {
		if (switchModes.get()) {
			switchModes();
		}
		switch (currentMode) {
		case MECANUM:
			mecanum.updateTeleop();
			break;
		case TANK:
			tank.updateTeleop();
			break;
		default:
			break;

		}
	}

}
