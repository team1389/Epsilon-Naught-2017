package systems;

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

    DigitalIn mecanumBtn;
    DigitalOut shiftToMecanum;

    TwinStickMecanumSystem twinstick;

    public PneumaticTwinMecanum(PercentIn leftStickY, PercentIn rightStickX, FourDriveOut<Percent> drive,
            DigitalIn mecanumBtn, DigitalOut shiftToMecanum) {
        twinstick = new TwinStickMecanumSystem(leftStickY, rightStickX, drive);
        this.mecanumBtn = mecanumBtn;
        this.shiftToMecanum = shiftToMecanum;
    }

    @Override
    public void init() {
        // doesn't do anything
    }

    @Override
    public void update() {
        twinstick.update();
        if (mecanumBtn.get())
            shiftToMecanum.set(true);
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