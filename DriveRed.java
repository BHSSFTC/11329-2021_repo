package org.firstinspires.ftc.teamcode;

import  org.firstinspires.ftc.teamcode.libs.*;
import com.qualcomm.robotcore.robot.Robot;
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

public class DriveRed extends Drive {
    @Override
    public void runPowerShots(){
        // start shooter
        (new TimedAutoTask(new Runnable() { public void run() {
            //hardwareInterface.setShooterRPM(RobotConfig.RPMInts[1]);
            hardwareInterface.runShooter(1);
        }}, opModeActiveCallable, "Shoot", telemetry, 1400)).run();

        // drive diagonally to shooting spot
        if (runningPowerShots) autoNav.goForTime(750, 0, 1, 0, 0.5f);

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
        
        // bump up shooter power
        hardwareInterface.runShooter(2);

        // drive diagonally to shooting spot
        if (runningPowerShots) autoNav.goForTime(750, 0, 1, 0, 0.5f);

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
        if (runningPowerShots) autoNav.goForTime(700, 0, 1, 0, 0.5f);

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
    
    @Override
    public void runPowerShotsInPlace(){
        hardwareInterface.runShooter(1);
        //// Back up a few inches.
        ////autoNav.goForTime(500, -1, 0, 0, 0.4f);
        
        // Go diagonally backward and to the right from the wall to our shooting spot.
        autoNav.goForTime(1500, 0.1f, -1, 0.03f, -0.6f);
        
        // Rotate clockwise to line up the first shot.
        autoNav.goForTime(1800, 0, 0, 1, -0.14f);
        
        // Wait for the shooter to get up to speed
        while(!hardwareInterface.shooterReady()){
            try{java.lang.Thread.sleep(1);}
            catch (Exception e){}
        }
        
        // open the shooter gate
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(0.2f);
        }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();

        // push rings into the shooter -- shoot!
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.fullIntake();
        }}, opModeActiveCallable, "Shoot", telemetry, 100)).run();
        
        // Now that the first ring has been shot, slow the shooter down for the next 2 shots.
        hardwareInterface.runShooter(2);
        
        // Close the gate.
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(-1);
        }}, opModeActiveCallable, "Wait", telemetry, 600)).run();
        
        // Rotate clockwise to line up the second shot.
        autoNav.goForTime(500, 0, 0, 1, -0.14f);
        
        // open the shooter gate
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(0.2f);
        }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();

        // push rings into the shooter -- shoot!
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.fullIntake();
        }}, opModeActiveCallable, "Shoot", telemetry, 100)).run();
        
        // Close the gate.
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(-1);
        }}, opModeActiveCallable, "Wait", telemetry, 600)).run();
        
        // Rotate clockwise to line up the third shot.
        autoNav.goForTime(550, 0, 0, 1, -0.12f);
        
        // open the shooter gate
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(0.2f);
        }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();

        // push rings into the shooter -- shoot!
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.fullIntake();
        }}, opModeActiveCallable, "Shoot", telemetry, 100)).run();
            
        // Stop shooting.
        hardwareInterface.setPusherPosition(-1);
        hardwareInterface.stopIntake();
        hardwareInterface.stopShooter();
        
        // We are done running the Power Shots, don't do it again!
        runningPowerShots = false;
    }
}
