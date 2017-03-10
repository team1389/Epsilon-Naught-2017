package org.usfirst.frc.team1389.autonomous;
public enum AutonOption {
    CROSS_BASELINE("Baseline"), 
    PLACE_GEAR_LEFT_PASSIVE("Gear Left Passive"),//
    PLACE_GEAR_CENTER_PASSIVE("Gear Center Passive"),//
    PLACE_GEAR_RIGHT_PASSIVE("Gear Right Passive"); //


    public final String name;

    AutonOption(String name) {
        this.name = name;
    }
}
