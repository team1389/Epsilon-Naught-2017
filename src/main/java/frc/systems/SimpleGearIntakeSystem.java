package frc.systems;

import com.team1389.hardware.inputs.software.DigitalIn;
import com.team1389.hardware.inputs.software.RangeIn;
import com.team1389.hardware.outputs.software.PercentOut;
import com.team1389.system.Subsystem;
import com.team1389.util.list.AddList;
import com.team1389.watch.Watchable;

public class SimpleGearIntakeSystem extends Subsystem{
    private PercentOut armVolt;
    private PercentOut intakeVolt;
    private DigitalIn intakeBtn;
    private RangeIn armAxis;
    private RangeIn outtakeManualAxis;
    private boolean runningIntake;

    public SimpleGearIntakeSystem(DigitalIn intakeBtn, RangeIn armAxis, RangeIn outtakeManualAxis, PercentOut armVolt, PercentOut intakeVolt){
        this.intakeBtn =  intakeBtn;
        this.armAxis = armAxis;
        this.outtakeManualAxis = outtakeManualAxis;
        this.armVolt = armVolt;
        this.intakeVolt = intakeVolt;
    }
    @Override
    public void init() {
        
    }
    @Override
    public void update() {
        armVolt.set(armAxis.get() * .3);
        runningIntake = runningIntake ^ intakeBtn.get();
        intakeVolt.set(runningIntake ? -1 : outtakeManualAxis.get() / 2);
        
    }
    @Override
    public AddList<Watchable> getSubWatchables(AddList<Watchable> arg0) {
        return arg0;
    }
    @Override
    public String getName() {
        return null;
    }
}