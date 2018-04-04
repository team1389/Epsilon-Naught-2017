package autonomous;

import java.util.Optional;
import java.util.function.Function;

import com.team1389.auto.AutoModeBase;

import robot.RobotSoftware;

/**
 * all autons
 * 
 * @author Quunii
 *
 */
public enum AutonOption
{
	CROSS_BASELINE();
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
