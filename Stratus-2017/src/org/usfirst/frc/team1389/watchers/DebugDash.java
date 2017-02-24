package org.usfirst.frc.team1389.watchers;

import com.team1389.watch.Watcher;

public class DebugDash extends Watcher {
	static DebugDash instance = new DebugDash();
	static final boolean IS_DEBUGGING = true;

	public static DebugDash getInstance() {
		return instance;
	}

	public DebugDash() {
		outputToDashboard();
	}

}
