package org.firstinspires.ftc.teamcode.libs;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DualLED {
    private DigitalChannel redLED;
    private DigitalChannel greenLED;
    
    public static enum Color {
        OFF,
        RED,
        GREEN,
        AMBER
    }
    
    public DualLED(HardwareMap hardwareMap, String redName, String greenName) {
        redLED = hardwareMap.get(DigitalChannel.class, redName);
        greenLED = hardwareMap.get(DigitalChannel.class, greenName);
        
        redLED.setMode(DigitalChannel.Mode.OUTPUT);
        greenLED.setMode(DigitalChannel.Mode.OUTPUT);
    }
    
    public void set(DualLED.Color color) {
        boolean red = (color == DualLED.Color.RED) || (color == DualLED.Color.AMBER);
        boolean green = (color == DualLED.Color.GREEN) || (color == DualLED.Color.AMBER);
        
        this.set(red, green);
    }
    
    public void set(boolean red, boolean green) {
        redLED.setState(!red);
        greenLED.setState(!green);
    }
}