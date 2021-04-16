package org.firstinspires.ftc.teamcode;

import  org.firstinspires.ftc.teamcode.libs.*;
import  org.firstinspires.ftc.teamcode.libs.opmode.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.concurrent.Callable;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.List;

@Autonomous

public class JugRoxAutoBlueCV extends AutoBase {
    @Override
    public void ringntAuto() {
        // drive forward from the shooter line to be in front of the wobble goal square
        autoNav.goForTime(800, 1, 0, 0, 1f);
        
        // rotate to face the right side of the field
        autoNav.goForTime(800, 0, 0, 1, 1f);
        
        // give the robot a moment to come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 250)).run();
        
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
        
        autoNav.goForTime(1500, 1, 0, 0, 0.75f);
        
        autoNav.goForTime(750, 0, -1, 0, 0.75f);
        
        //autoNav.goForTime(1000, -1, 0, 0, 0.3f);
        // drive slightly away from the wobble goals so we are not touching them
        //autoNav.goForTime(1500, 1, 0, 0, 0.3f);
    }
    
    @Override
    public void singleAuto() {
        // keep holding on to the wobble goal
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalUpEncoder, true);
        
        // drive sideways to be in front of the rings
        autoNav.goForTime(850/*1000*/, 0, -1, 0, 0.7f);
        
        // let the robot come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 100)).run();
        
        // get the intake going and drive backwards to pick up 3 rings
        hardwareInterface.fullIntake();
        autoNav.goForTime(2250/*2000*/, -1, 0, 0, 0.4f/*0.5f*/);
        
        // let the robot come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 100)).run();
        
        // don't hold on to stuck rings -- run the intake roller backwards
        hardwareInterface.reverseIntake();
        
        // spin the shooter back up
        //hardwareInterface.runShooter(shooterPower);
        //hardwareInterface.setShooterRPM(RobotConfig.RPMInts[0]);
        hardwareInterface.runShooter(0);
        
        // drive diagonally to shooting spot
        autoNav.goForTime(2000/*2250*/, 1, 0.5f, 0, 0.5f);
        
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 500/*500*/)).run();
        
        // open the shooter gate
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(0.2f);
        }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();
        
        // push rings into the shooter -- shoot!
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.fullIntake();
        }}, opModeActiveCallable, "Shoot", telemetry, 1250/*750*/)).run();
        
        // stop shooting
        hardwareInterface.stopIntake();
        hardwareInterface.setPusherPosition(-1);
        //hardwareInterface.runShooter(0f);
        //hardwareInterface.setShooterRPM(0);
        hardwareInterface.stopShooter();
        
        // rotate to face the left side of the field
        autoNav.goForTime(1400, 0, 0, 1, -1f);
        
        // lower wobble goal arm
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, true);
        }}, opModeActiveCallable, "Lower Wobble Goal", telemetry, 1200)).run();
        
        // drive to the wobble goal spot
        autoNav.goForTime(1000, -1, 0, 0, 0.5f);
        
        // open the wobble goal claw
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalDownEncoder, false);
        }}, opModeActiveCallable, "Drop Wobble Goal", telemetry, 500)).run();
        
        // raise the wobble goal arm
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleEncoder, false);
        
        // drive to the wobble goal spot
        autoNav.goForTime(700, 1, 0, 0, 0.5f);
    }
    
    @Override
    public void quadAuto() {
        // keep holding on to the wobble goal
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalUpEncoder, true);
        
        // drive sideways to be in front of the rings
        autoNav.goForTime(850/*1000*/, 0, -1, 0, 0.7f);
        
        // let the robot come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 100)).run();
        
        // get the intake going and drive backwards to pick up 3 rings
        hardwareInterface.fullIntake();
        autoNav.goForTime(2250/*2000*/, -1, 0, 0, 0.4f/*0.5f*/);
        
        // let the robot come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 100)).run();
        
        // don't hold on to the 4th ring -- run the intake roller backwards
        hardwareInterface.reverseIntake();
        
        // spin the shooter back up
        //hardwareInterface.runShooter(shooterPower);
        //hardwareInterface.setShooterRPM(RobotConfig.RPMInts[0]);
        hardwareInterface.runShooter(0);
        
        // drive diagonally to shooting spot
        autoNav.goForTime(2000/*2250*/, 1, 0.5f, 0, 0.5f);
        
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 500/*500*/)).run();
        
        // open the shooter gate
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(0.2f);
        }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();
        
        // push rings into the shooter -- shoot!
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.fullIntake();
        }}, opModeActiveCallable, "Shoot", telemetry, 1250/*750*/)).run();
        
        // stop shooting
        hardwareInterface.stopIntake();
        hardwareInterface.setPusherPosition(-1);
        //hardwareInterface.runShooter(0f);
        //hardwareInterface.setShooterRPM(0);
        hardwareInterface.stopShooter();
        
        // rotate to face the right side of the field
        autoNav.goForTime(800, 0, 0, 1, 1f);
        
        // give the robot a moment to come to a halt
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 250)).run();
        
        // partially lower the wobble goal arm while moving
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleEncoder, true);
        
        // drive left, towards the goal, to get to where the wobble goal goes
        autoNav.goForTime(2500, 0.2f, 1, 0, 1);
        
        // open the wobble goal claw
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleDownEncoder, false);
        }}, opModeActiveCallable, "Drop Wobble Goal", telemetry, 500)).run();
        
        // drive forward away from the right wall slightly
        autoNav.goForTime(500, 1, 0, 0, 0.5f);
        
        // raise the wobble goal arm
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalMiddleEncoder, false);
        
        // drive forward-right to be over the line
        autoNav.goForTime(2250, -0.5f, -1f, 0, 1f);
    }
    
    @Override
    public void globalAutoBegin() {
        hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalUpEncoder, true);
        
        // take out the backlash in the drivetrain
        autoNav.goForTime(500, 1, 0, 0, 0.02f);
        
        // start the shooter, so it is ready when we get to the line
        //hardwareInterface.runShooter(shooterPower);
        //hardwareInterface.setShooterRPM(RobotConfig.RPMInts[0]);
        hardwareInterface.runShooter(0);
        
        // Drive forward to shooting position
        autoNav.goForTime(1900, 1, 0, 0, 1f);
        
        // let the robot come to a halt and the shooter settle on its speed
        (new TimedAutoTask(new Runnable() { public void run() {
        }}, opModeActiveCallable, "Wait >:(", telemetry, 250/*500*/)).run();
        
        // open the shooter gate
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.setPusherPosition(0.2f);
        }}, opModeActiveCallable, "Gate down", telemetry, 250)).run();
        
        // push rings into the shooter -- shoot!
        (new TimedAutoTask(new Runnable() { public void run() {
            hardwareInterface.fullIntake();
        }}, opModeActiveCallable, "Shoot", telemetry, 1350/*750*/)).run();
        
        // stop shooting
        hardwareInterface.stopIntake();
        hardwareInterface.setPusherPosition(-1);
        //hardwareInterface.runShooter(0f);
        //hardwareInterface.setShooterRPM(0);
        hardwareInterface.stopShooter();
    }
    
    @Override
    public void globalAutoEnd() {
        // The robot isn't driving, so bring the shooter
        // (which is controlled by a separate thread!) to a halt.
        //hardwareInterface.stopShooterRPMControl();
        //hardwareInterface.stopRingPresenceCheckerThread();
        //hardwareInterface.shooter.setPower(0);
        hardwareInterface.stopShooter();
        
        (new TimedAutoTask(new Runnable() { public void run() {
            // raise the wobble goal arm
            hardwareInterface.setWobbleGoalArm(RobotConfig.wobbleGoalUpEncoder, true);
        }}, opModeActiveCallable, "Arm Up", telemetry, 1000)).run();
    }
}
