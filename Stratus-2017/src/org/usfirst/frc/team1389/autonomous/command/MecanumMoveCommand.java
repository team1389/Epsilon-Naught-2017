package org.usfirst.frc.team1389.autonomous.command;

import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.value_types.Percent;
import com.team1389.system.drive.FourDriveOut;
import com.team1389.system.drive.FourWheelSignal;
import com.team1389.system.drive.MecanumAlgorithm;
import com.team1389.util.Timer;

public class MecanumMoveCommand extends Command
{
	FourDriveOut<Percent> drive;
	private double xPwr, yPwr, turnPwr, time;
	Timer timer;

	public MecanumMoveCommand(FourDriveOut<Percent> drive, double xPwr, double yPwr, double turnPwr, double time)
	{
		timer = new Timer();
		this.xPwr = xPwr;
		this.yPwr = yPwr;
		this.drive = drive;
		this.turnPwr = turnPwr;
		this.time = time;
	}

	@Override
	protected boolean execute()
	{
		drive.set(MecanumAlgorithm.mecanumCalc(xPwr, yPwr, turnPwr, false, 0));
		return timer.get() >= time;
	}

	@Override
	protected void done()
	{
		drive.set(FourWheelSignal.NEUTRAL);
	}

}
