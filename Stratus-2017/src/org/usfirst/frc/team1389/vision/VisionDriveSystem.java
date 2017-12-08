package org.usfirst.frc.team1389.vision;


import com.team1389.auto.command.WaitTimeCommand;
import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.software.AngleIn;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.value_types.Position;
import com.team1389.hardware.value_types.Speed;
import com.team1389.hardware.value_types.Value;
import com.team1389.system.drive.FourDriveOut;
import com.team1389.system.drive.OctoMecanumSystem;
import com.team1389.system.drive.OctoMecanumSystem.DriveMode;
import com.team1389.trajectory.Translation2d;

public class VisionDriveSystem extends OctoMecanumSystem
{
	private RangeIn<Value> robotAccel;
	private AngleIn<Speed> robotTurnRate;
	private DigitalIn manualCancel;
	private DigitalOut notifyCompletion;
	private State alignState;
	// private RangeIn<Position> leftPos, rightPos;

	public VisionDriveSystem(FourDriveOut<Percent> voltageDrive, DigitalOut octoShifter, AngleIn<Position> gyro,
			PercentIn xAxis, PercentIn yAxis, PercentIn twist, PercentIn trim, DigitalIn switchModes, DigitalIn trigger)
	{
		super(voltageDrive, octoShifter, gyro, xAxis, yAxis, twist, trim, switchModes, trigger);
	}

	@Override
	public void update()
	{
		switch (alignState)
		{
		case AUTO_ALIGNING:

			break;
		case MANUAL:
			super.update();
			break;
		default:
			break;

		}
	}

	public enum State
	{
		MANUAL, AUTO_ALIGNING;
	}

	public State getAlignState()
	{
		return alignState;
	}

	public void enableAutoAlign(Translation2d desiredPos)
	{
		schedule(getAutoAlignCommand(desiredPos));
	}

	private Command getAutoAlignCommand(Translation2d desired)
	{
		return CommandUtil.combineSequential(setState(State.AUTO_ALIGNING), setModeCommand(DriveMode.MECANUM),
				getAlignMoveCommand(desired), setState(State.MANUAL), notifyAlignExit());
	}

	private Command notifyAlignExit()
	{
		return CommandUtil.combineSequential(CommandUtil.createCommand(() -> notifyCompletion.set(true)),
				new WaitTimeCommand(3), CommandUtil.createCommand(() -> notifyCompletion.set(false)));
	}

	private Command getAlignMoveCommand(Translation2d desired)
	{
		/*
		 * return new DriveStraightCommand(new PIDConstants(.25, 0, 0), leftPos,
		 * rightPos, desired.getY(), RobotConstants.MaxAcceleration,
		 * RobotConstants.MaxVelocity) .until(this::shouldCancelAutoAlign);TODO
		 */
		return null;
	}

	private boolean shouldCancelAutoAlign()
	{
		return robotAccel.get() > 10 || robotTurnRate.get() > 30 || manualCancel.get();
	}

	private Command setState(State state)
	{
		return CommandUtil.createCommand(() -> alignState = state);
	}

}
