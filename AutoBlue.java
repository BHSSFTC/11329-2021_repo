package org.firstinspires.ftc.teamcode;

import  org.firstinspires.ftc.teamcode.libs.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.concurrent.Callable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous

public class AutoBlue extends LinearOpMode {
    public HardwareInterface hardwareInterface;
    
    public Callable<Boolean> opModeActiveCallable = new Callable<Boolean>() {
        public Boolean call() {
            return opModeIsActive();
        }
    };
    
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initalizing...");
        telemetry.update();
        
        hardwareInterface = new HardwareInterface(hardwareMap);
        
        waitForStart();
        if (opModeIsActive()) {
            hardwareInterface.stopAll();
            
            //3000 ms @ 0.35 power to first square
            //4000 ms @ 0.35 power to line
            
            (new TimedAutoTask(new Runnable() { public void run() {
                hardwareInterface.mecanumDrive(-0.35f, 0, 0, 1);
            }}, opModeActiveCallable, "Move forward", telemetry, 4000)).run();
            
            (new TimedAutoTask(new Runnable() { public void run() {
                hardwareInterface.stopAll();
            }}, opModeActiveCallable, "Stop", telemetry, 500)).run();
            
            (new TimedAutoTask(new Runnable() { public void run() {
                hardwareInterface.mecanumDrive(0.25f, 0.33f, 0, 1);
            }}, opModeActiveCallable, "Move backward", telemetry, 650)).run();
            
            (new TimedAutoTask(new Runnable() { public void run() {
                hardwareInterface.stopAll();
            }}, opModeActiveCallable, "Stop", telemetry, 500)).run();
            
            (new TimedAutoTask(new Runnable() { public void run() {
                hardwareInterface.shooter.setPower(0.65f);
            }}, opModeActiveCallable, "Shoot", telemetry, 4000)).run();
            
            (new TimedAutoTask(new Runnable() { public void run() {
                hardwareInterface.fullIntake();
            }}, opModeActiveCallable, "Feed Ring", telemetry, 10000)).run();
            
            (new TimedAutoTask(new Runnable() { public void run() {
                hardwareInterface.mecanumDrive(-0.25f, 0.4f, 0, 1);
            }}, opModeActiveCallable, "Move forward", telemetry, 1200)).run();
            
            (new TimedAutoTask(new Runnable() { public void run() {
                hardwareInterface.stopAll();
            }}, opModeActiveCallable, "Stop", telemetry, 500)).run();
                
            telemetry.addData("Status", "Done!");
            telemetry.update();
        }
    }
}
