package org.usfirst.frc.team1389.autonomous;

import java.util.Optional;
import java.util.function.Function;

import org.usfirst.frc.team1389.autonomous.routines.CrossBaselineOpenLoop;
import org.usfirst.frc.team1389.autonomous.routines.active_gear.ActivePlaceGearCenter;
import org.usfirst.frc.team1389.autonomous.routines.active_gear.ActivePlaceGearLeft;
import org.usfirst.frc.team1389.autonomous.routines.active_gear.ActivePlaceGearRight;
import org.usfirst.frc.team1389.autonomous.routines.active_gear.GearArmTest;
import org.usfirst.frc.team1389.autonomous.routines.open_loop_gear.OpenLoopPlaceGear;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;

/**
 * all autons
 * 
 * @author Quunii
 *
 */
public enum AutonOption {
	CROSS_BASELINE(CrossBaselineOpenLoop::new), TEST_GEAR_ARM(GearArmTest::new), ACTIVE_GEAR_LEFT(
			ActivePlaceGearLeft::new), ACTIVE_GEAR_CENTER(
					ActivePlaceGearCenter::new), ACTIVE_GEAR_RIGHT(ActivePlaceGearRight::new),
	PLACE_GEAR_OPEN_LOOP(OpenLoopPlaceGear::new);

	public final Optional<Function<RobotSoftware, AutoModeBase>> autoConstructor;

	AutonOption(Function<RobotSoftware, AutoModeBase> autoConstructor) {
		this.autoConstructor = Optional.of(autoConstructor);
	}

	AutonOption() {
		this.autoConstructor = Optional.empty();
	}

	public AutoModeBase setupAutoModeBase(RobotSoftware robot) {
		if (autoConstructor.isPresent()) {
			return autoConstructor.get().apply(robot);
		} else {
			throw new RuntimeException("cannot auto insantiate a complex style auto option!");
		}
	}
}
