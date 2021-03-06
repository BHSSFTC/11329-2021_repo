package org.firstinspires.ftc.teamcode;

import  org.firstinspires.ftc.teamcode.libs.*;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import java.util.concurrent.Callable;

@TeleOp

public class Drive extends LinearOpMode {
    public long prevTime = 0;
    public long deltaTime = 0;
    
    public String[] powerStrings = { "High/Low Goal", "Power Shots/Middle Goal" };
    public float[] powerFloats = { 67, 54 };
    public int selectedPower = 0;
    public boolean pressingPowerChange = false;
    
    public float intakeTimer = 0;
    public float shooterTimer = 0;
    public boolean shootingOne = false;
    
    public int matchTimer = 0;
    
    public boolean runningPowerShots = false;
    
    public int wobbleGoalPosition = RobotConfig.wobbleGoalUp;
    public boolean wobbleGoalGrabbing = false;
    
    public OperatorInterface operatorInterface;
    public HardwareInterface hardwareInterface;
    public AutoNav autoNav;
    
    public Callable<Boolean> opModeActiveCallable = new Callable<Boolean>() {
        public Boolean call() {
            if (operatorInterface.cancelPowerShots()) runningPowerShots = false;
            return opModeIsActive() && runningPowerShots;
        }
    };

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initalizing...");
        telemetry.update();
        prevTime = System.currentTimeMillis();
        
        operatorInterface = new OperatorInterface(gamepad1, gamepad2);
        hardwareInterface = new HardwareInterface(hardwareMap);
    
        autoNav = new AutoNav(opModeActiveCallable, telemetry, hardwareInterface);
        autoNav.update();
           
        waitForStart();
        if (opModeIsActive()) {
            hardwareInterface.stopAll();
            
            while (opModeIsActive()) {
                deltaTime = System.currentTimeMillis() - prevTime;
                prevTime = System.currentTimeMillis();
                
                matchTimer += deltaTime;
                
                if (matchTimer > 90000) hardwareInterface.setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.BREATH_RED);
                else if (matchTimer > 85000) hardwareInterface.setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
                else hardwareInterface.setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.SKY_BLUE);
                
                hardwareInterface.mecanumDrive(-operatorInterface.getDriveVertical(), -operatorInterface.getDriveHorizontal(), operatorInterface.getDriveRotational(), operatorInterface.getDriveSpeed());
                
                if (operatorInterface.intakeForward()){
                    if (operatorInterface.shooterSpin()) {
                        hardwareInterface.setPusherPosition(0.2f);
                        
                        intakeTimer += deltaTime;
                        
                        if (intakeTimer >= 250) {
                            hardwareInterface.fullIntake();
                        }
                    } else {
                        hardwareInterface.fullIntake();
                        
                        intakeTimer = 0;
                    }
                } else if (operatorInterface.intakeBackward()){
                    hardwareInterface.reverseIntake();
                    
                    hardwareInterface.setPusherPosition(-1);
                    
                    intakeTimer = 0;
                } else intakeTimer = 0;
                
                if(!operatorInterface.shootOne() && !shootingOne && !operatorInterface.intakeForward() && !operatorInterface.intakeBackward()) {
                    hardwareInterface.stopIntake();
                    hardwareInterface.setPusherPosition(-1);
                }
                
                if (operatorInterface.shooterPowerHigher()) {
                    if (!pressingPowerChange) selectedPower--;
                    pressingPowerChange = true;
                } else if (operatorInterface.shooterPowerLower()) {
                    if (!pressingPowerChange) selectedPower++;
                    pressingPowerChange = true;
                } else pressingPowerChange = false;
                    
                if (selectedPower < 0) selectedPower = powerFloats.length - 1;
                else if (selectedPower >= powerFloats.length) selectedPower = 0;
                
                if (operatorInterface.shooterSpin()) hardwareInterface.runShooter(-powerFloats[selectedPower] / 100);
                //else if (operatorInterface.directorRunBackward()) hardwareInterface.runShooter(0.17);
                else hardwareInterface.stopShooter();
                
                if (operatorInterface.powerShots() && !runningPowerShots) runningPowerShots = true;
                
                if (runningPowerShots) {
                    // start shooter
                    (new TimedAutoTask(new Runnable() { public void run() {
                        hardwareInterface.runShooter(-powerFloats[1] / 100);
                    }}, opModeActiveCallable, "Shoot", telemetry, 1000)).run();
        
                    // drive diagonally to shooting spot
                    if (runningPowerShots) autoNav.goForTime(450, 0, -1, 0, 0.5f);
        
                    // open the shooter gate
                    (new TimedAutoTask(new Runnable() { public void run() {
                        hardwareInterface.setPusherPosition(0.2f);
                    }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();
        
                    // push rings into the shooter -- shoot!
                    (new TimedAutoTask(new Runnable() { public void run() {
                        hardwareInterface.fullIntake();
                    }}, opModeActiveCallable, "Shoot", telemetry, 100)).run();
                    
                    (new TimedAutoTask(new Runnable() { public void run() {
                        hardwareInterface.setPusherPosition(-1);
                    }}, opModeActiveCallable, "Wait", telemetry, 600)).run();
            
                    // stop shooting
                    hardwareInterface.stopIntake();
        
                    // drive diagonally to shooting spot
                    if (runningPowerShots) autoNav.goForTime(700, 0, -1, 0, 0.5f);
        
                    // open the shooter gate
                    (new TimedAutoTask(new Runnable() { public void run() {
                        hardwareInterface.setPusherPosition(0.2f);
                    }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();
        
                    // push rings into the shooter -- shoot!
                    (new TimedAutoTask(new Runnable() { public void run() {
                        hardwareInterface.fullIntake();
                    }}, opModeActiveCallable, "Shoot", telemetry, 100)).run();
                    
                    (new TimedAutoTask(new Runnable() { public void run() {
                        hardwareInterface.setPusherPosition(-1);
                    }}, opModeActiveCallable, "Wait", telemetry, 600)).run();
            
                    // stop shooting
                    hardwareInterface.stopIntake();
        
                    // drive diagonally to shooting spot
                    if (runningPowerShots) autoNav.goForTime(700, 0, -1, 0, 0.5f);
        
                    // open the shooter gate
                    (new TimedAutoTask(new Runnable() { public void run() {
                        hardwareInterface.setPusherPosition(0.2f);
                    }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();
        
                    // push rings into the shooter -- shoot!
                    (new TimedAutoTask(new Runnable() { public void run() {
                        hardwareInterface.fullIntake();
                    }}, opModeActiveCallable, "Shoot", telemetry, 1000)).run();
            
                    // stop shooting
                    hardwareInterface.setPusherPosition(-1);
                    hardwareInterface.stopIntake();
                    hardwareInterface.stopShooter();
                    
                    runningPowerShots = false;
                }
                
                if (operatorInterface.shootOne() && !shootingOne) shootingOne = true;
                
                if (shootingOne){
                    shooterTimer += deltaTime;
                    if (shooterTimer < 250) hardwareInterface.setPusherPosition(0.2f);
                    else if (shooterTimer < 350) hardwareInterface.fullIntake();
                    else if (shooterTimer < 800) {
                        hardwareInterface.setPusherPosition(-1f);
                        hardwareInterface.fullIntake();
                    } else {
                        hardwareInterface.stopIntake();
                        shootingOne = false;
                    }
                } else shooterTimer = 0;
                
                if (operatorInterface.wobbleGoalUp()) wobbleGoalPosition = RobotConfig.wobbleGoalUp;
                else if (operatorInterface.wobbleGoalMiddle()) wobbleGoalPosition = RobotConfig.wobbleGoalMiddle;
                else if (operatorInterface.wobbleGoalDown()) wobbleGoalPosition = RobotConfig.wobbleGoalDown;
                
                if (operatorInterface.wobbleGrab()) wobbleGoalGrabbing = true;
                else if (operatorInterface.wobbleRelease()) wobbleGoalGrabbing = false;
                
                if (operatorInterface.wobbleGoalChange()) {
                    switch (wobbleGoalPosition) {
                        case RobotConfig.wobbleGoalUp:
                            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalUpEncoder, true);
                            break;
                        case RobotConfig.wobbleGoalMiddle:
                            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleEncoder, wobbleGoalGrabbing);
                            break;
                        case RobotConfig.wobbleGoalDown:
                            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, wobbleGoalGrabbing);
                            break;
                    }
                }
                
                if ((wobbleGoalPosition == RobotConfig.wobbleGoalUp || wobbleGoalPosition == RobotConfig.wobbleGoalDown) && hardwareInterface.wobbleGoal.getCurrentPosition() == hardwareInterface.wobbleGoal.getTargetPosition()) hardwareInterface.wobbleGoal.setPower(0);
                
                telemetry.addData("Shooter Mode", powerStrings[selectedPower]);
                telemetry.addData("\t\t\t\tPower", powerFloats[selectedPower] + "%");
                telemetry.addData("Wobble Goal Encoder", hardwareInterface.wobbleGoal.getCurrentPosition());
                telemetry.addData("Status", "Running!");
                telemetry.update();
            }
        }
    }
}
