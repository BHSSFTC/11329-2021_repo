package org.firstinspires.ftc.teamcode.libs;

import org.firstinspires.ftc.robotcore.external.navigation.Position;


public class RobotConfig {
    public static final String[] RPMStrings = { "High Goal", "1st Power Shot", "2nd and 3rd Power Shot" };
    public static final int[] RPMInts = { 3400, 2800, 2700 };
    public static final float RPMTolerancePercent = 0.05f;
    
    public static class WobbleGoal {
    
        public static enum Position {
            up(927),
            middle(500),
            middleDown(250),
            down(0);
            
            public static Position[] positions = { Position.up, Position.middle, Position.middleDown, Position.down };
            
            private final int encoderPosition;
        
            Position(int encoderPosition){
                this.encoderPosition = encoderPosition;
            }
        }
        
    }
    
    public static final int wobbleGoalUp = 2;
    public static final int wobbleGoalUpEncoder = 927;
    
    public static final int wobbleGoalMiddle = 1;
    public static final int wobbleGoalMiddleEncoder = 500;
    
    public static final int wobbleGoalMiddleDownEncoder = 250;
    
    public static final int wobbleGoalDown = 0;
    public static final int wobbleGoalDownEncoder = 0;
    
    public static final int[] positions = { wobbleGoalUpEncoder, wobbleGoalMiddleEncoder, wobbleGoalDownEncoder };

    public static final String imuConfigFileName = "IMU_CALIBRATION.json";
    public static final String colorSensor1ConfigFileName = "COLOR_SENSOR_CALIBRATION1.json";
    public static final String colorSensor2ConfigFileName = "COLOR_SENSOR_CALIBRATION2.json";
    public static final String colorSensor3ConfigFileName = "COLOR_SENSOR_CALIBRATION3.json";
    
    public static final float ringLightFlashInterval = 250;
}