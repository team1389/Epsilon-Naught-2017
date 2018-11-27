package systems;

import com.team1389.system.drive.TwinStickMecanumSystem;
import com.team1389.hardware.inputs.software.PercentIn;
import com.team1389.system.drive.FourDriveOut;
import com.team1389.hardware.value_types.Percent;
import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.outputs.software.DigitalOut;

public class PneumaticTwinMecanum extends TwinStickMecanumSystem {

    PercentIn leftStickY;
    PercentIn leftStickX;
    PercentIn rightStickX;

    FourDriveOut<Percent> drive;

    DigitalIn mecanumBtn;
    DigitalOut shiftToMecanum;

    public PneumaticTwinMecanum(PercentIn leftStickY, PercentIn rightStickX, FourDriveOut<Percent> drive,
            DigitalIn mecanumBtn, DigitalOut shiftToMecanum) {
        super(leftStickY, rightStickX, drive);
        this.mecanumBtn = mecanumBtn;
        this.shiftToMecanum = shiftToMecanum;
    }

    @Override
    public void update() {
        super.update();
        if (mecanumBtn.get())
            shiftToMecanum.set(true);
    }
}