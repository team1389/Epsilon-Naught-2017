package org.usfirst.frc.team1389.autonomous.active_gear;

import org.usfirst.frc.team1389.robot.RobotSoftware;
import org.usfirst.frc.team1389.systems.GearIntakeSystem;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class GearArmTest extends AutoModeBase {
	private GearIntakeSystem gearArm;

	public GearArmTest(RobotSoftware robot) {
		this.gearArm = new GearIntakeSystem(robot.armAngle, robot.armVel, robot.armElevator.getVoltageOutput(),
				robot.gearIntake.getVoltageOutput(), robot.gearIntakeCurrent);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(gearArm);
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		runCommand(gearArm.pairWithBackgroundCommand(gearArm.intakeGear()));
	}

	@Override
	public String getIdentifier() {
		return "Test Arm";
	}

}
