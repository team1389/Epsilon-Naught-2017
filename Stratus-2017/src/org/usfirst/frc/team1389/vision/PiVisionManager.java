package org.usfirst.frc.team1389.vision;

import java.util.Optional;

import com.team1389.util.Pair;
import com.team1389.util.list.AddList;
import com.team1389.watch.CompositeWatchable;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.BooleanInfo;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class PiVisionManager implements CompositeWatchable {
	private static PiVisionManager instance = new PiVisionManager();

	public PiVisionManager getInstance() {
		return instance;
	}

	private PiVisionManager() {
		initPi();
	}

	private static final String piAddress = "169.254.168.93"; // TODO arbitrary
	private static final String tableKey = "piVision";

	private enum PiTableKeys {
		PROCESS_FLAG("isProcessing"), TARGET_VISIBLE("dataAvailable"), TIMESTAMP("timestamp"), ANGLE_TO_TARGET(
				"angle"), TARGET_DIST("dist");
		protected final String key;

		private PiTableKeys(String key) {
			this.key = key;
		}
	}

	private NetworkTable piTable;

	private void initPi() {
		// TODO https://github.com/WPIRoboticsProjects/GRIP/wiki/Running-GRIP-on-a-Raspberry-Pi-2
		System.out.println("attempting to initialize Raspberry pi at adddress " + piAddress);
		piTable = NetworkTable.getTable(tableKey);
	}

	/**
	 * the robot is intended to control the state of this value, using the processing flag to tell
	 * the pi to initiate vision calculations. This method is intended to allow the user to verify
	 * that the pi is being told to do processing
	 * @return true if
	 */
	public boolean isProcessing() {
		return piTable.getBoolean(PiTableKeys.PROCESS_FLAG.key, false);
	}

	/**
	 * @return true if the pi has adequate vision of the target to complete vision calculations
	 */
	public boolean isTargetVisible() {
		return piTable.getBoolean(PiTableKeys.TARGET_VISIBLE.key, false);
	}

	/**
	 * @return true if vision data is available on the table
	 */
	public boolean isDataAvailable() {
		return piTable.containsKey(PiTableKeys.ANGLE_TO_TARGET.key);
	}

	public void setProcessing(boolean processing) {
		piTable.putBoolean(PiTableKeys.PROCESS_FLAG.key, processing);
	}

	/**
	 * queries the network table for the latest angle calculation
	 * @return an angle value, if one is available
	 */
	@SuppressWarnings("deprecation")
	public Optional<Double> getRawAngle() {
		if (isProcessing() && isTargetVisible() && isDataAvailable()) {
			double angle = piTable.getNumber(PiTableKeys.ANGLE_TO_TARGET.key);
			return Optional.of(angle);
		}
		return Optional.empty();

	}

	/**
	 * queries the network table for the latest angle calculation and the time stamp it was
	 * observed, for latency compensation
	 * @return a pair that contains the time stamp and angle, if available
	 */
	@SuppressWarnings("deprecation")
	public Optional<Pair<Double, Double>> requestLatestAngleToTarget() {
		if (isProcessing() && isTargetVisible() && isDataAvailable()) {
			double timestamp = piTable.getNumber(PiTableKeys.TIMESTAMP.key);
			double angle = piTable.getNumber(PiTableKeys.ANGLE_TO_TARGET.key);
			return Optional.of(new Pair<>(timestamp, angle));
		}
		return Optional.empty();
	}

	@Override
	public String getName() {
		return "pi vision";
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(new BooleanInfo("processing", this::isProcessing),
				new BooleanInfo("targetVis", this::isTargetVisible), new BooleanInfo("data", this::isDataAvailable));
	}

}