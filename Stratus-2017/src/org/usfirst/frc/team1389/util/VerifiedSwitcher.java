package org.usfirst.frc.team1389.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.team1389.command_framework.CommandUtil;
import com.team1389.command_framework.command_base.Command;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;

public class VerifiedSwitcher {
	public static final long POLL_PERIOD_MILLIS = 15;
	private DigitalIn verifier;
	private DigitalOut switcher;
	private ExecutorService service;
	private Consumer<Boolean> onSwitch;

	public VerifiedSwitcher(DigitalIn verifier, DigitalOut switcher, ExecutorService service,
			Consumer<Boolean> onSwitch) {
		this.verifier = verifier;
		this.switcher = switcher;
		this.service = service;
		this.onSwitch = onSwitch;
	}

	public void addSwitchListener(Consumer<Boolean> onSwitch) {
		Consumer<Boolean> oldSwitchListener = this.onSwitch;
		this.onSwitch = b -> {
			oldSwitchListener.accept(b);
			onSwitch.accept(b);
		};
	}

	public Supplier<Boolean> isInDesired(boolean desired) {
		return () -> get() == desired;
	}

	public void set(boolean val) {
		if (get() != val) {
			Runnable sendSwitchRequest = () -> switcher.set(val);
			Command waitForSwitch = CommandUtil.createCommand(isInDesired(val));
			Runnable triggerListeners = () -> onSwitch.accept(val);
			CompletableFuture
					.runAsync(sendSwitchRequest, service)
				//TODO		.thenRun(() -> CommandUtil.executeCommand(waitForSwitch, POLL_PERIOD_MILLIS))
						.thenRun(triggerListeners);
		}

	}

	public DigitalOut getSwitcherOutput() {
		return new DigitalOut(this::set);
	}

	public DigitalIn getSwitcherInput() {
		return verifier;
	}

	public boolean get() {
		return verifier.get();
	}
}
