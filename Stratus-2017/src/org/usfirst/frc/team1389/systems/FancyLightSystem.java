package org.usfirst.frc.team1389.systems;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.team1389.system.Subsystem;
import com.team1389.util.Color;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.StringInfo;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class FancyLightSystem extends Subsystem {
	private Consumer<Color> lightController;
	private Supplier<GearIntakeSystem.State> intakeState;
	private Alliance alliance;
	private Color lastSetColor;

	public FancyLightSystem(Consumer<Color> lightController, Supplier<GearIntakeSystem.State> intakeState) {
		this.lightController = lightController;
		this.intakeState = intakeState;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem.put(new StringInfo("Color", lastSetColor::toString));
	}

	@Override
	public String getName() {
		return "Lights";
	}

	@Override
	public void init() {
		alliance = DriverStation.getInstance().getAlliance();
		lastSetColor = getAllianceColor(alliance);
		lightController.accept(lastSetColor);
	}

	@Override
	public void update() {
		lastSetColor = getAllianceColor(alliance);
		switch (intakeState.get()) {
		case CARRYING:
			lastSetColor = Color.green;
			break;
		case INTAKING:
			lastSetColor = Color.orange;
			break;
		default:
			break;
		}
		lightController.accept(lastSetColor);
	}

	public static Color getAllianceColor(Alliance alliance) {
		return alliance == Alliance.Blue ? Color.blue : Color.red;
	}

}
