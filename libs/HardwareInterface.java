package org.firstinspires.ftc.teamcode.libs;

import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.robot.Robot;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class HardwareInterface {
    public HardwareMap hardwareMap;
    
    public BNO055IMU imu;
    
    public RevBlinkinLedDriver blinkin;
    
    public DcMotor frontRightMotor;
    public DcMotor backRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor frontLeftMotor;
    
    public DcMotor intake;
    public DcMotor conveyor;
    public DcMotor wobbleGoal;
    public DcMotor shooter;
    
    public Servo pusher;
    
    public Servo wobbleGoalGrabber;
    
    public HardwareInterface(HardwareMap hm) {
        hardwareMap = hm;
        
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.mode = BNO055IMU.SensorMode.NDOF;
        parameters.calibrationDataFile = "IMU_CALIBRATION.json";
        imu.initialize(parameters);
        
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        
        backRightMotor = hardwareMap.dcMotor.get("motor0");
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        frontRightMotor = hardwareMap.dcMotor.get("motor1");
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        frontLeftMotor = hardwareMap.dcMotor.get("motor2");
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        backLeftMotor = hardwareMap.dcMotor.get("motor3");
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        intake = hardwareMap.dcMotor.get("intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        conveyor = hardwareMap.dcMotor.get("conveyor");
        conveyor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        shooter = hardwareMap.dcMotor.get("shooter");
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        wobbleGoal = hardwareMap.dcMotor.get("wobbleGoal");
        wobbleGoal.setTargetPosition(RobotConfig.wobbleGoalUpEncoder);
        wobbleGoal.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleGoal.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wobbleGoal.setPower(0);
        
        /*while (wobbleGoal.getCurrentPosition() != RobotConfig.wobbleGoalUpEncoder) { 
            
        }

        wobbleGoal.setPower(0);*/
        
        wobbleGoalGrabber = hardwareMap.servo.get("wobbleGoalGrabber");
        
        pusher = hardwareMap.servo.get("pusher");
    }
    
    public void stopAll() {
        stopAllButShooter();
        stopShooter();
    }
    
    public void stopAllButShooter() {
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);  
        backLeftMotor.setPower(0);
        frontLeftMotor.setPower(0);
        intake.setPower(0);
        conveyor.setPower(0);
    }
    
    public void stopShooter() {
        shooter.setPower(0);
    }
    
    public void tankDrive(float leftPower, float rightPower) {
        frontRightMotor.setPower(-leftPower);
        backRightMotor.setPower(-leftPower);
        frontLeftMotor.setPower(rightPower);
        backLeftMotor.setPower(rightPower);
    }
    
    public void mecanumDrive(float vertical, float horizontal, float rotational, float maxSpeed) {
        frontRightMotor.setPower((vertical + horizontal - rotational) * maxSpeed);
        backRightMotor.setPower((vertical - horizontal - rotational) * maxSpeed);
        frontLeftMotor.setPower(-(vertical + horizontal + rotational) * maxSpeed);
        backLeftMotor.setPower(-(vertical - horizontal + rotational) * maxSpeed);
    }
    
    public void fullIntake() {
        conveyor.setPower(1);
        intake.setPower(-1);
    }
    
    public void reverseIntake() {
        conveyor.setPower(0);
        intake.setPower(1);
    }
    
    public void stopIntake() {
        conveyor.setPower(0);
        intake.setPower(0);
    }
    
    public void setPusherPosition(float angle) {
        pusher.setPosition(angle);
    }
    
    public void runShooter(float power) {
        shooter.setPower(power);
    }
    
    public void setWobbleGoalArm(int angle, boolean gripping) {
        wobbleGoal.setTargetPosition(angle);
        wobbleGoal.setPower(-0.5);
        
        if (gripping) wobbleGoalGrabber.setPosition(0);
        else wobbleGoalGrabber.setPosition(0.8);
    }
    
    public void setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern pattern) {
        blinkin.setPattern(pattern);
    }
}
