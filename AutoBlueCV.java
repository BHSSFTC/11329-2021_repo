package org.firstinspires.ftc.teamcode;

import  org.firstinspires.ftc.teamcode.libs.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.concurrent.Callable;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.List;

@Autonomous

public class AutoBlueCV extends LinearOpMode {
    public HardwareInterface hardwareInterface;
    public ComputerVision computerVision;
    public AutoNav autoNav;
    
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
    
        autoNav = new AutoNav(opModeActiveCallable, telemetry, hardwareInterface);
        autoNav.update();
        
        computerVision = new ComputerVision(hardwareMap);
        computerVision.start();
        
        waitForStart();
        if (opModeIsActive()) {
            hardwareInterface.stopAll();
            
            (new TimedAutoTask(new Runnable() { public void run() { }}, opModeActiveCallable, "Wait for TensorFlow", telemetry, 0)).run();
            
            List<Recognition> updatedRecognitions = computerVision.getUpdatedRecognitions();
            if (updatedRecognitions == null || updatedRecognitions.size() < 1) {
                telemetry.log().add("No objects detected, ringn't auto");
                
                ringntAuto();
            } else if (updatedRecognitions.get(0).getLabel() == computerVision.getFirstLabel()) {
                telemetry.log().add("Quad stack detected, quad auto");
                
                quadAuto();
            } else if (updatedRecognitions.get(0).getLabel() == computerVision.getSecondLabel()) {
                telemetry.log().add("Single ring detected, single auto");
                
                singleAuto();
            } else {
                telemetry.log().add("What did you do seriously what the heck");
            }
            //singleAuto();
            
            telemetry.update();
            
            //3000 ms @ 0.35 power to first square
            //4000 ms @ 0.35 power to line
                
            telemetry.addData("Status", "Done!");
            telemetry.update();
        }
    }
    
    public void ringntAuto() {
        globalAutoBegin();
        
        // drive forward from the shooter line to be in front of the wobble goal square
        autoNav.goForTime(1000, 1, 0, 0, 1f);
        
        // rotate to face the right side of the field
        autoNav.goForTime(800, 0, 0, 1, 1f);
        
        // give the robot a moment to come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 250)).run();
        
        // drive forward away from the wall slightly
        autoNav.goForTime(350, 1, 0, 0, 1f);
        
        // put the wobble goal down and bring the arm back in
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 250)).run();
        
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, true);
        }}, opModeActiveCallable, "Lower Wobble Goal", telemetry, 1200)).run();
        
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        }}, opModeActiveCallable, "Drop Wobble Goal", telemetry, 500)).run();
        
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalUpEncoder, false);
        }}, opModeActiveCallable, "Raise Wobble Goal", telemetry, 600)).run();
        
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalUpEncoder, true);
        
        // go on a diagonal to the back-right corner of the field
        autoNav.goForTime(3100, 0.58f, -1, 0, 1f);
        
        // lower and open the wobble goal arm
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        }}, opModeActiveCallable, "Lower and Open Wobble Goal Arm", telemetry, 1200)).run();
        
        // come backwards towards the second wobble goal
        autoNav.goForTime(1600, -1, 0, 0, 0.25f);
        
        // grab the wobble goal
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, true);
        }}, opModeActiveCallable, "Close Wobble Goal Claw", telemetry, 500)).run();
        
        // raise the wobble goal to half-way up
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleEncoder, true);
        }}, opModeActiveCallable, "Raise Wobble Goal Arm", telemetry, 500)).run();
        
        // drive diagonally back to where the wobble goals go, with the second wobble goal
        autoNav.goForTime(2600, -0.35f, 1, 0, 1f);
        
        // lower the wobble goal arm
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, true);
        }}, opModeActiveCallable, "Lower and Open Wobble Goal Arm", telemetry, 1200)).run();
        
        // open the wobble goal claw
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        }}, opModeActiveCallable, "Lower and Open Wobble Goal Arm", telemetry, 1200)).run();
        
        // drive slightly away from the wobble goals so we are not touching them
        autoNav.goForTime(1500, 1, -1, 0, 0.3f);
        
        globalAutoEnd();
    }
    
    public void singleAuto() {
        //Goes to B
        globalAutoBegin();
        
        // keep holding on to the wobble goal
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalUpEncoder, true);
        
        // drive sideways to be in front of the rings
        autoNav.goForTime(1000, 0, -1, 0, 0.7f);
        
        // let the robot come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 100)).run();
        
        // get the intake going and drive backwards to pick up 3 rings
        hardwareInterface.fullIntake();
        autoNav.goForTime(2000, -1, 0, 0, 0.5f);
        
        // let the robot come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 100)).run();
        
        // don't hold on to the 4th ring -- run the intake roller backwards
        hardwareInterface.reverseIntake();
        
        // spin the shooter back up
        hardwareInterface.runShooter(-0.67f);
        
        // drive diagonally to shooting spot
        autoNav.goForTime(2250, 1, 0.5f, 0, 0.5f);
        
        // open the shooter gate
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(0.2f);
        }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();
        
        // push rings into the shooter -- shoot!
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.fullIntake();
        }}, opModeActiveCallable, "Shoot", telemetry, 750)).run();
        
        // stop shooting
        hardwareInterface.stopIntake();
        hardwareInterface.setPusherPosition(-1);
        hardwareInterface.runShooter(0f);
        
        // rotate to face the left side of the field
        autoNav.goForTime(800, 0, 0, 1, -1f);
        
        // drive to the wobble goal spot
        autoNav.goForTime(1400, 0, -1, 0, 1);
        
        // lower wobble goal arm
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, true);
        }}, opModeActiveCallable, "Lower Wobble Goal", telemetry, 1200)).run();
        
        // open the wobble goal claw
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        }}, opModeActiveCallable, "Drop Wobble Goal", telemetry, 500)).run();
        
        // raise the wobble goal arm
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleEncoder, false);
        
        // gently bump wall
        
        // move off wall
        autoNav.goForTime(750, 1, 0, 0, 0.3f);
        
        // move to the second wobble goal
        autoNav.goForTime(2800, -0.8f, 0.73f, 0.3f, 1f);
        
        // put the wobble goal arm down
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        
        // drive back to grab wobble goal
        autoNav.goForTime(2000, -1, 0, 0, 0.25f);
        
        // close the wobble goal claw
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, true);
        }}, opModeActiveCallable, "Grab Wobble Goal", telemetry, 500)).run();
        
        // undo wobble goal translation
        autoNav.goForTime(2900, -0.8f, 0.73f, 0.3f, -1f);
        
        // drop the wobble goal
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        }}, opModeActiveCallable, "Drop Wobble Goal", telemetry, 500)).run();
        
        // move on to the line
        autoNav.goForTime(1000, 0.20f, 1, 0, 0.55f);
        
        globalAutoEnd();
    }
    
    public void quadAuto() {
        //Goes to C
        globalAutoBegin();
        
        // keep holding on to the wobble goal
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalUpEncoder, true);
        
        // drive sideways to be in front of the rings
        autoNav.goForTime(1000, 0, -1, 0, 0.7f);
        
        // let the robot come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 100)).run();
        
        // get the intake going and drive backwards to pick up 3 rings
        hardwareInterface.fullIntake();
        autoNav.goForTime(2000, -1, 0, 0, 0.5f);
        
        // let the robot come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 100)).run();
        
        // don't hold on to the 4th ring -- run the intake roller backwards
        hardwareInterface.reverseIntake();
        
        // spin the shooter back up
        hardwareInterface.runShooter(-0.67f);
        
        // drive diagonally to shooting spot
        autoNav.goForTime(2250, 1, 0.5f, 0, 0.5f);
        
        // open the shooter gate
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(0.2f);
        }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();
        
        // push rings into the shooter -- shoot!
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.fullIntake();
        }}, opModeActiveCallable, "Shoot", telemetry, 750)).run();
        
        // stop shooting
        hardwareInterface.stopIntake();
        hardwareInterface.setPusherPosition(-1);
        hardwareInterface.runShooter(0f);
        
        // rotate to face the right side of the field
        autoNav.goForTime(800, 0, 0, 1, 1f);
        
        // give the robot a moment to come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 250)).run();
        
        // drive left, towards the goal, to get to where the wobble goal goes
        autoNav.goForTime(2500, 0, 1, 0, 1);
        
        // start lowering the wobble goal arm
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, true);
        
        // drive forward away from the right wall slightly
        autoNav.goForTime(500, 1, 0, 0, 0.5f);
        
        // open the wobble goal claw
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        }}, opModeActiveCallable, "Drop Wobble Goal", telemetry, 500)).run();
        
        // raise the wobble goal arm
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleEncoder, false);
        //(new TimedAutoTask(new Runnable() { public void run() {
        //    hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleEncoder, false);
        //}}, opModeActiveCallable, "Raise Wobble Goal", telemetry, 600)).run();
        
        // go on a diagonal to the back-right corner, where the 2nd wobble goal is
        autoNav.goForTime(4250, 0.45f, -1, 0, 1f);
        
        // start lowering the wobble goal arm
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        
        // drive backwards to get to the second wobble goal
        autoNav.goForTime(2250, -1, 0, 0, 0.25f);
        
        // grab the wobble goal
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, true);
        }}, opModeActiveCallable, "Close Wobble Goal Claw", telemetry, 500)).run();
        
        // raise the wobble goal arm
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleEncoder, true);
        
        //move back to the front-left corner of the field
        autoNav.goForTime(4000, -0.4f, 1, 0.06f, 1f);
        
        // start lowering the wobble goal arm
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, true);
        
        // open the wobble goal claw
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        }}, opModeActiveCallable, "Drop Wobble Goal", telemetry, 500)).run();
        
        // drive forward-right to be over the line
        autoNav.goForTime(1750, 0.5f, -1f, 0, 1f);
        
        globalAutoEnd();
    }
    
    public void globalAutoBegin() {
        // take out the backlash in the drivetrain
        autoNav.goForTime(500, 1, 0, 0, 0.02f);
        
        // start the shooter, so it is ready when we get to the line
        hardwareInterface.runShooter(-0.67f);
        
        // Drive forward to shooting position
        autoNav.goForTime(1750, 1, 0, 0, 1f);
        
        // let the robot come to a halt and the shooter settle on its speed
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 500)).run();
        
        // open the shooter gate
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(0.2f);
        }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();
        
        // push rings into the shooter -- shoot!
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.fullIntake();
        }}, opModeActiveCallable, "Shoot", telemetry, 750)).run();
        
        // stop shooting
        hardwareInterface.stopIntake();
        hardwareInterface.setPusherPosition(-1);
        hardwareInterface.runShooter(0f);
    }
    
    public void globalAutoEnd() {
        
    }
}
