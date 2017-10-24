package org.usfirst.frc.team1389.autonomous;

import java.util.Optional;
import java.util.function.Function;

import org.usfirst.frc.team1389.autonomous.routines.CrossBaselineOpenLoop;
import org.usfirst.frc.team1389.autonomous.routines.ball.BallBaselineBlue;
import org.usfirst.frc.team1389.autonomous.routines.ball.BallBaselineRed;
import org.usfirst.frc.team1389.autonomous.routines.open_loop_gear.VoltCenterGear;
import org.usfirst.frc.team1389.autonomous.routines.open_loop_gear.VoltSideGear;
import org.usfirst.frc.team1389.robot.RobotSoftware;

import com.team1389.auto.AutoModeBase;

/**
 * all autons
 * 
 * @author Quunii
 *
 */
public enum AutonOption
{
	CROSS_BASELINE(CrossBaselineOpenLoop::new), CENTER_GEAR(VoltCenterGear::new), SIDE_GEAR(
			VoltSideGear::new), BAll_BASELINE_BLUE(BallBaselineBlue::new), BALL_BASELINE_RED(BallBaselineRed::new); /* GEAR_CENTER(CenterGear::new); */
	public final Optional<Function<RobotSoftware, AutoModeBase>> autoConstructor;

	AutonOption(Function<RobotSoftware, AutoModeBase> autoConstructor)
	{
		this.autoConstructor = Optional.of(autoConstructor);
	}

	AutonOption()
	{
		this.autoConstructor = Optional.empty();
	}

	public AutoModeBase setupAutoModeBase(RobotSoftware robot)
	{
		if (autoConstructor.isPresent())
		{
			return autoConstructor.get().apply(robot);
		} else
		{
			throw new RuntimeException("cannot auto insantiate a complex style auto option!");
		}
	}
}
