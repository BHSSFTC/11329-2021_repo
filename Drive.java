package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.libs.*;
import org.firstinspires.ftc.teamcode.libs.opmode.*;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

public abstract class Drive extends OpmodeBase {
    public long prevTime = 0;
    public long deltaTime = 0;
    
    public float shooterTimer = 0;
    public boolean shootingOne = false;
    
    public int matchTimer = 0;
    
    public boolean runningPowerShots = false;
    
    public int wobbleGoalPosition = RobotConfig.wobbleGoalUp;
    public boolean wobbleGoalGrabbing = false;
    
    public OperatorInterface operatorInterface;
    
    public void runPowerShots(){}
    public void runPowerShotsInPlace(){}
    
    public void manipulateWobbleGoal(){
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
        
        if ((wobbleGoalPosition == RobotConfig.wobbleGoalUp || wobbleGoalPosition == RobotConfig.wobbleGoalDown) && hardwareInterface.wobbleGoal.getCurrentPosition() == hardwareInterface.wobbleGoal.getTargetPosition()){
            hardwareInterface.wobbleGoal.setPower(0);
        }
    }
    
    public void setLEDPattern(){
        //hardwareInterface.setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.CONFETTI);
        
        /*if (matchTimer > 122000) hardwareInterface.setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
        else if (matchTimer > 92000) hardwareInterface.setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.BREATH_RED);
        else if (matchTimer > 87000) hardwareInterface.setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
        else hardwareInterface.setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.SKY_BLUE);*/
    }

    @Override
    public void run() {
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
            
            int closest = 0;
            
            // On startup, move the wobble goal arm to the nearest programmed position.
            for (int i = 0; i < RobotConfig.positions.length; i++) {
                float distance1 = Math.abs(hardwareInterface.wobbleGoal.getCurrentPosition() - RobotConfig.positions[i]);
                float distance2 = Math.abs(hardwareInterface.wobbleGoal.getCurrentPosition() - RobotConfig.positions[closest]);
                if (distance1 < distance2){
                    closest = i;
                }
            }
            hardwareInterface.setWobbleGoalArm(RobotConfig.positions[closest], true);
            
            while (opModeIsActive()) {
                deltaTime = System.currentTimeMillis() - prevTime;
                prevTime = System.currentTimeMillis();
                matchTimer += deltaTime;
                
                setLEDPattern();
                
                hardwareInterface.mecanumDrive(-operatorInterface.getDriveVertical(), -operatorInterface.getDriveHorizontal(), operatorInterface.getDriveRotational(), operatorInterface.getDriveSpeed());
                
                if (operatorInterface.intakeForward()){
                    hardwareInterface.fullIntake();
                } else if (operatorInterface.intakeBackward()){
                    hardwareInterface.reverseIntake();
                } else {
                    hardwareInterface.stopIntake();
                }
                
                if (operatorInterface.shooterSpin()){
                    hardwareInterface.runShooter(0);
                }
                else hardwareInterface.stopShooter();
                
                if (operatorInterface.gateDown() && operatorInterface.intakeForward()){
                    hardwareInterface.setPusherPosition(0.2f);
                } else {
                    hardwareInterface.setPusherPosition(-1);
                }
                
                if (operatorInterface.powerShots() && !runningPowerShots) runningPowerShots = true;
                if (runningPowerShots) runPowerShotsInPlace();
                
                if (operatorInterface.shootOne() && !shootingOne) shootingOne = true;
                if (shootingOne){
                    shooterTimer += deltaTime;
                    if (shooterTimer < 250) hardwareInterface.setPusherPosition(0.2f);
                    else if (shooterTimer < 350) {
                        hardwareInterface.setPusherPosition(0.2f);
                        hardwareInterface.fullIntake();
                    } else if (shooterTimer < 800) {
                        hardwareInterface.setPusherPosition(-1f);
                        hardwareInterface.fullIntake();
                    } else {
                        hardwareInterface.setPusherPosition(-1f);
                        hardwareInterface.stopIntake();
                        shootingOne = false;
                    }
                } else shooterTimer = 0;
                
                manipulateWobbleGoal();
                
                telemetry.addData("Shooter RPM", hardwareInterface.getShooterRPM());
                telemetry.addData("Wobble Goal Encoder", hardwareInterface.wobbleGoal.getCurrentPosition());
                telemetry.addData("Top Sensor", HardwareInterface.ring1Present ? "Present" : "Absent");
                telemetry.addData("Middle Sensor", HardwareInterface.ring2Present ? "Present" : "Absent");
                telemetry.addData("Bottom Sensor", HardwareInterface.ring3Present ? "Present" : "Absent");
                telemetry.addData("Status", "Running!");
                telemetry.update();
            }
        }
        // The robot isn't driving, so bring the shooter
        // (which is controlled by a separate thread!) to a halt.
        hardwareInterface.shooter.setPower(0);
    }
}
