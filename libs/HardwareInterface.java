package org.firstinspires.ftc.teamcode.libs;

import com.qualcomm.robotcore.hardware.Gyroscope;
import java.io.File;
import com.qualcomm.robotcore.util.ReadWriteFile;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.robot.Robot;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HardwareInterface {
    public HardwareMap hardwareMap;
    
    public BNO055IMU imu;
    
    public RevBlinkinLedDriver blinkin;
    
    public DualLED firstRingLED;
    public DualLED secondRingLED;
    public DualLED thirdRingLED;
    
    public RevColorSensorV3 firstRingSensor;
    public RevColorSensorV3 secondRingSensor;
    public RevColorSensorV3 thirdRingSensor;
    
    public DcMotor frontRightMotor;
    public DcMotor backRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor frontLeftMotor;
    
    public DcMotor intake;
    public DcMotor conveyor;
    public DcMotor wobbleGoal;
    public DcMotorEx shooter;
    
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
        
        firstRingLED = new DualLED(hardwareMap, "firstRingLEDRed", "firstRingLEDGreen");
        secondRingLED = new DualLED(hardwareMap, "secondRingLEDRed", "secondRingLEDGreen");
        thirdRingLED = new DualLED(hardwareMap, "thirdRingLEDRed", "thirdRingLEDGreen");
        
        firstRingSensor = hardwareMap.get(RevColorSensorV3.class, "firstRingSensor");
        secondRingSensor = hardwareMap.get(RevColorSensorV3.class, "secondRingSensor");
        thirdRingSensor = hardwareMap.get(RevColorSensorV3.class, "thirdRingSensor");
        
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        intake = hardwareMap.dcMotor.get("intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        conveyor = hardwareMap.dcMotor.get("conveyor");
        conveyor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        shooter.setDirection(DcMotorEx.Direction.REVERSE);
        shooter.setVelocityPIDFCoefficients(4f, 0.8f, 1f, 0f);
        
        wobbleGoal = hardwareMap.dcMotor.get("wobbleGoal");
        wobbleGoal.setTargetPosition(RobotConfig.wobbleGoalUpEncoder);
        wobbleGoal.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wobbleGoal.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wobbleGoal.setPower(0);

        wobbleGoal.setPower(0);
        
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
    
    public double TPStoRPM(double tps) {
        return (tps / 28) * 60;
    }
    
    public double RPMtoTPS(double rpm) {
        return (rpm / 60) * 28;
    }
    
    float shooterTargetRPM = 0;
    
    public void runShooter(int mode) {
        shooter.setVelocity(RPMtoTPS(RobotConfig.RPMInts[mode]));
        shooterTargetRPM = RobotConfig.RPMInts[mode];
    }
    
    public int getShooterRPM() {
        return (int) Math.round(TPStoRPM(shooter.getVelocity()));
    }
    
    public void stopShooter() {
        //setShooterRPM(0);
        shooter.setVelocity(0);
        shooter.setPower(0);
        shooterTargetRPM = 0;
    }
    
    public boolean shooterReady() {
        return shooterTargetRPM != 0 && Math.abs(getShooterRPM() - shooterTargetRPM) <= Math.abs(shooterTargetRPM * RobotConfig.RPMTolerancePercent);
    }
    
    public void tankDrive(float leftPower, float rightPower) {
        frontRightMotor.setPower(-leftPower);
        backRightMotor.setPower(-leftPower);
        frontLeftMotor.setPower(rightPower);
        backLeftMotor.setPower(rightPower);
    }
    
    // rotational: + couter clockwise, - clockwise
    public void mecanumDrive(float vertical, float horizontal, float rotational, float maxSpeed) {
        frontRightMotor.setPower((vertical + horizontal - rotational) * maxSpeed);
        backRightMotor.setPower((vertical - horizontal - rotational) * maxSpeed);
        frontLeftMotor.setPower(-(vertical + horizontal + rotational) * maxSpeed);
        backLeftMotor.setPower(-(vertical - horizontal + rotational) * maxSpeed);
    }
    
    public void fullIntake() {
        //conveyor.setPower(0.80f);
        conveyor.setPower(0.50f);
        intake.setPower(-0.60f);
    }
    
    public void reverseIntake() {
        conveyor.setPower(0);
        intake.setPower(0.5);
    }
    
    public void stopIntake() {
        conveyor.setPower(0);
        intake.setPower(0);
    }
    
    public void setPusherPosition(float angle) {
        pusher.setPosition(angle);
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
    
    private ColorSensorCalibration colorSensor1Calibration = new ColorSensorCalibration();
    private ColorSensorCalibration colorSensor2Calibration = new ColorSensorCalibration();
    private ColorSensorCalibration colorSensor3Calibration = new ColorSensorCalibration();
    
    private static Thread ringPresenceCheckerThread = null;
    private static boolean stopRingPresenceCheckerThread = false;
    
    public static boolean ring1Present = false;
    public static boolean ring2Present = false;
    public static boolean ring3Present = false;
    
    void loadColorSensorFiles() {
        File file = AppUtil.getInstance().getSettingsFile(RobotConfig.colorSensor1ConfigFileName);
        colorSensor1Calibration.deserialize(ReadWriteFile.readFile(file));
        
        file = AppUtil.getInstance().getSettingsFile(RobotConfig.colorSensor2ConfigFileName);
        colorSensor2Calibration.deserialize(ReadWriteFile.readFile(file));
        
        file = AppUtil.getInstance().getSettingsFile(RobotConfig.colorSensor3ConfigFileName);
        colorSensor3Calibration.deserialize(ReadWriteFile.readFile(file));
    }
    
    public void startRingPresenceCheckerThread() {
        loadColorSensorFiles();
        
        stopRingPresenceCheckerThread = false;
        if(ringPresenceCheckerThread == null){
            ringPresenceCheckerThread = new Thread(
                new Runnable(){
                    @Override
                    public void run(){
                        while(!stopRingPresenceCheckerThread){
                            ring1Present = colorSensor1Calibration.isPresent(firstRingSensor.red(), firstRingSensor.green(), firstRingSensor.blue());
                            ring2Present = colorSensor2Calibration.isPresent(secondRingSensor.red(), secondRingSensor.green(), secondRingSensor.blue());
                            ring3Present = colorSensor3Calibration.isPresent(thirdRingSensor.red(), thirdRingSensor.green(), thirdRingSensor.blue());
                            
                            //thirdRingLED.set(!ring3Present, ring3Present);
                            //secondRingLED.set(!ring2Present || !ring3Present, ring2Present);
                            //firstRingLED.set(!ring1Present || !ring2Present, ring1Present);
                            if (ring3Present && ring2Present && ring1Present) {
                                setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
                            } else if (ring3Present && ring2Present) {
                                setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
                            } else if (ring3Present) {
                                setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
                            } else {
                                setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                            }
                            
                            try { Thread.sleep(100); }
                            catch(Exception e) { };
                        }
                    }
                }
            );
            
            ringPresenceCheckerThread.start();
        } else if (!ringPresenceCheckerThread.isAlive()) ringPresenceCheckerThread.start();
    }
    
    public void stopRingPresenceCheckerThread() {
        stopRingPresenceCheckerThread = true;
    }
    
    private String getRGB(RevColorSensorV3 colorSensor) {
        return "(" + colorSensor.red() + "," + colorSensor.green() + "," + colorSensor.blue() + ")";
    }
    
    public String getTopSensorRGB() {
        return getRGB(firstRingSensor);
    }
    
    public String getMiddleSensorRGB() {
        return getRGB(secondRingSensor);
    }
    
    public String getBottomSensorRGB() {
        return getRGB(thirdRingSensor);
    }
}
