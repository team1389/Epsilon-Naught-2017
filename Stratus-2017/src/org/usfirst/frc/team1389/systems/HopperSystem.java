package org.usfirst.frc.team1389.systems;

import java.util.function.Supplier;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.StringInfo;

public class HopperSystem extends Subsystem
{
	private DigitalOut gate, dumperLeft, dumperRight;
	private DigitalIn dumperEndstop;
	private State hopperState;

	public HopperSystem(DigitalOut dumperLeft, DigitalOut dumperRight, DigitalOut gate, DigitalIn dumperEndstop)
	{
		this.dumperLeft = dumperLeft;
		this.dumperRight = dumperRight;
		this.gate = gate;
		this.dumperEndstop = dumperEndstop;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem)
	{
		return stem.put(dumperEndstop.getWatchable("endstop hit"), new StringInfo("state", () -> hopperState.name()));
	}

	public Supplier<State> getStateSupplier()
	{
		return () -> hopperState;
	}

	@Override
	public String getName()
	{
		return "Hopper";
	}

	@Override
	public void init()
	{

	}

	@Override
	public void update()
	{

	}

	public enum State
	{
		DUMPING, COLLECTING
	}

	public void enterState(State state)
	{
		if (state == hopperState)
			return;
		switch (state)
		{
		case COLLECTING:
			schedule(resetDumperCommand());
			break;
		case DUMPING:
			schedule(new DumpCommand());
			break;
		default:
			break;

		}
	}

	public enum DumperPosition
	{
		DUMP(true), STORE(false);
		public final boolean pos;

		private DumperPosition(boolean pos)
		{
			this.pos = pos;
		}
	}

	private enum GatePosition
	{
		OPEN(true), CLOSED(false);
		public final boolean pos;

		private GatePosition(boolean pos)
		{
			this.pos = pos;
		}
	}

	private void resetDumper()
	{
		gate.set(GatePosition.CLOSED.pos);
		dumperLeft.set(DumperPosition.STORE.pos);
		dumperRight.set(DumperPosition.STORE.pos);
		hopperState = State.COLLECTING;
	}

	public Command resetDumperCommand()
	{
		return CommandUtil.createCommand(this::resetDumper);
	}

	public class DumpCommand extends Command
	{
		@Override
		protected void initialize()
		{
			dumperLeft.set(DumperPosition.DUMP.pos);
			dumperRight.set(DumperPosition.DUMP.pos);
			gate.set(GatePosition.OPEN.pos);
		}

		@Override
		protected boolean execute()
		{
			return dumperEndstop.get();
		}

		@Override
		protected void done()
		{
			hopperState = State.DUMPING;
		}
	}

}
