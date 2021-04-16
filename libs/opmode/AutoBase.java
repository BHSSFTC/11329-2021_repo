package org.firstinspires.ftc.teamcode.libs.opmode;

import org.firstinspires.ftc.teamcode.libs.*;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
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
import java.util.List;

public abstract class AutoBase extends OpmodeBase {
    public ComputerVision computerVision;
    
    public String tfStatus = "Waiting for activation...";
    
    @Override
    public void run() {
        telemetry.addData("Status", "Initalizing...");
        telemetry.addData("Tensorflow Status", tfStatus);
        telemetry.update();
        
        computerVision = new ComputerVision(hardwareMap);
        computerVision.start();
        
        waitForStart();
        if (opModeIsActive()) {
            hardwareInterface.stopAll();
            
            (new TimedAutoTask(new Runnable() { public void run() { }}, opModeActiveCallable, "Wait for TensorFlow", telemetry, 1000)).run();
            
            List<Recognition> updatedRecognitions = computerVision.getUpdatedRecognitions();
            if (updatedRecognitions == null || updatedRecognitions.size() < 1) {
                tfStatus = "No objects detected, ringn't auto";
                
                globalAutoBegin();
                ringntAuto();
                globalAutoEnd();
            } else if (updatedRecognitions.get(0).getLabel() == computerVision.getFirstLabel()) {
                tfStatus = "Quad stack detected, quad auto";
                
                globalAutoBegin();
                quadAuto();
                globalAutoEnd();
            } else if (updatedRecognitions.get(0).getLabel() == computerVision.getSecondLabel()) {
                tfStatus = "Single ring detected, single auto";
                
                globalAutoBegin();
                singleAuto();
                globalAutoEnd();
            } else {
                tfStatus = "What did you do seriously what the heck";
            }
            //singleAuto();
            
            telemetry.update();
                
            telemetry.addData("Status", "Done!");
            telemetry.addData("Tensorflow Status", tfStatus);
            telemetry.update();
        }

        hardwareInterface.stopAll();
    }
    
    public abstract void ringntAuto();

    public abstract void singleAuto();

    public abstract void quadAuto();

    public abstract void globalAutoBegin();

    public abstract void globalAutoEnd();
}
