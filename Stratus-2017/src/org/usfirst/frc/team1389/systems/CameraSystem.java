package org.usfirst.frc.team1389.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.watch.info.StringInfo;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;

public class CameraSystem extends Subsystem {

	DigitalIn switchingCamerasButton;
	UsbCamera camera0, camera1;
	MjpegServer switcher;
	UsbCamera camAtMoment;
	

	public CameraSystem(DigitalIn switchingCamerasButton) {
		this.switchingCamerasButton = switchingCamerasButton;
	}

	@Override
	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
		// TODO Auto-generated method stub
		return stem.put(switchingCamerasButton.getWatchable("button"),
				new StringInfo("current cam", () -> camAtMoment.getName()));
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Camera controller";
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		UsbCamera camera0 = new UsbCamera("cam0", 0);
		UsbCamera camera1 = new UsbCamera("cam1", 1);
		switcher = new MjpegServer("switcher", 5801);
		camera0.setResolution(640, 480);
		camera1.setResolution(640, 480);
	}

	@Override
	public void update() {
		if (switchingCamerasButton.get()) {	
			camAtMoment=((camAtMoment == camera1) ? camera1 : camera0);
			switcher.setSource(camAtMoment);
		}

	}
}