package org.usfirst.frc.team1389.autonomous;

import java.util.function.Function;

import org.usfirst.frc.team1389.robot.RobotSoftware;
import org.usfirst.frc.team1389.systems.GearIntakeSystem;
import org.usfirst.frc.team1389.systems.GearIntakeSystem.State;

import com.team1389.auto.AutoModeBase;
import com.team1389.auto.AutoModeEndedException;
import com.team1389.auto.command.DriveStraightCommand;
import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.configuration.PIDConstants;
import com.team1389.hardware.inputs.software.PositionEncoderIn;
import com.team1389.system.SystemManager;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class DriveStraightDropOffGear extends AutoModeBase {
	RobotSoftware robot;
	GearIntakeSystem gearSystem;
	SystemManager manager;

	DriveStraightDropOffGear(RobotSoftware robot) {
		PositionEncoderIn.setGlobalWheelDiameter(4);
		this.robot = robot;
		gearSystem = new GearIntakeSystem(robot.armAngle, robot.armVel, robot.armElevator.getVoltageOutput(),
				robot.gearIntake.getVoltageOutput(), robot.gearIntakeCurrent);
		manager = new SystemManager(gearSystem);
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		return stem;
	}

	@Override
	public String getName() {
		return "Drive straight and drop off the gear";
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		Command driving = new DriveStraightCommand(new PIDConstants(.25, 0, 0), robot.voltageDrive.getAsTank(),
				robot.flPos, robot.frPos, robot.gyroInput, 1, 1, 1, .2);
		Function<State, Command> gearState = s -> gearSystem
				.pairWithBackgroundCommand(gearSystem.getEnterStateCommand(s));
		// Function<Double, Command> distanceChecker = d -> new WaitForBooleanCommand(() ->
		// (robot.frontLeft.getPositionInput().get() > d));
		Function<Double, Command> distanceChecker = WaitTimeCommand::new;
		Command gearPlacing = CommandUtil.combineSequential(gearState.apply(State.CARRYING), distanceChecker.apply(5.0),
				gearState.apply(State.ALIGNING), distanceChecker.apply(2.0), gearState.apply(State.PLACING));
		runCommand(CommandUtil.combineSimultaneous(driving));
	}

}
