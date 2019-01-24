package frc.systems;

import com.team1389.system.drive.TwinStickMecanumSystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.system.drive.FourDriveOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;
import com.team1389.system.Subsystem;

public class PneumaticTwinMecanum extends Subsystem {

    DigitalIn mecanumUp;
    DigitalIn mecanumDown;
    DigitalOut shiftToMecanum;

    TwinStickMecanumSystem twinstick;

    public PneumaticTwinMecanum(PercentIn leftStickY, PercentIn leftStickX, PercentIn rightStickX,
            FourDriveOut<Percent> drive, DigitalIn mecanumUp, DigitalIn mecanumDown, DigitalOut shiftToMecanum) {
        twinstick = new TwinStickMecanumSystem(leftStickY, leftStickX, rightStickX, drive);
        this.mecanumUp = mecanumUp;
        this.mecanumDown = mecanumDown;
        this.shiftToMecanum = shiftToMecanum;
    }

    @Override
    public void init() {
        // doesn't do anything
    }

    @Override
    public void update() {
        twinstick.update();
        if (mecanumUp.get())
            shiftToMecanum.set(true);
        if (mecanumDown.get())
            shiftToMecanum.set(false);
    }

    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
        return stem.put(twinstick);
    }

    @Override
    public String getName() {
        return "Pneumatic Twin Mecanum";
    }
}