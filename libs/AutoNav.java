package org.firstinspires.ftc.teamcode.libs;

import org.firstinspires.ftc.teamcode.math.*;
import java.util.Vector;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import java.util.concurrent.Callable;
import com.qualcomm.hardware.bosch.BNO055IMU;


public class AutoNav {
    public Callable<Boolean> opModeIsActive;
    public Telemetry telemetry;
    public HardwareInterface hardwareInterface;
    
    public Position position;
    public Orientation angles;
    public Acceleration gravity;
    public Acceleration acceleration;
    public Orientation lastAngles = new Orientation();
    double correctedAngle = 0;
    
    private int frontLeftEncoder = 0;
    private int backLeftEncoder = 0;
    private int backRightEncoder = 0;
    private int frontRightEncoder = 0;
    
    private int frontLeftPrevEncoder = 0;
    private int backLeftPrevEncoder = 0;
    private int backRightPrevEncoder = 0;
    private int frontRightPrevEncoder = 0;
    
    long deltaTime = 0;
    long prevTime = System.currentTimeMillis();
    
    public PIDController translationController;
    public PIDController rotationController;
    
    public final double wheelDiameterMM = 96;
    public final double ticksPerRot = 537.6;
    
    public final double ticksPerMM = ticksPerRot / (wheelDiameterMM * Math.PI);
    
    public AutoNav(Callable<Boolean> oma, Telemetry tm, HardwareInterface hi) {
        opModeIsActive = oma;
        telemetry = tm;
        hardwareInterface = hi;
        
        position = new Position();
        
        translationController = new PIDController(0.002, 0.00006, 0.001);
        
        translationController.setInputRange(0, mmToTicks(5000));
        translationController.setTolerance(0.008);
        
        rotationController = new PIDController(0.006, 0.00006, 0.006);
        
        rotationController.setInputRange(0, 360);
        rotationController.setContinuous();
        rotationController.setTolerance(0.1);
        
        pollEncoders();
    }
    
    private void pollEncoders() {
        frontLeftEncoder = hardwareInterface.frontLeftMotor.getCurrentPosition();
        backLeftEncoder = hardwareInterface.backLeftMotor.getCurrentPosition();
        backRightEncoder = -hardwareInterface.backRightMotor.getCurrentPosition();
        frontRightEncoder = -hardwareInterface.frontRightMotor.getCurrentPosition();
    }
    
    private int[] computeEncoderDerivitives() {
        pollEncoders();
        
        int[] encoderDerivatives = { 
            frontLeftEncoder - frontLeftPrevEncoder, 
            backLeftEncoder - backLeftPrevEncoder, 
            backRightEncoder - backRightPrevEncoder, 
            frontRightEncoder - frontRightPrevEncoder 
        };
        
        frontLeftPrevEncoder = frontLeftEncoder;
        backLeftPrevEncoder = backLeftEncoder;
        backRightPrevEncoder = backRightEncoder;
        frontRightPrevEncoder = frontRightEncoder;
        
        return encoderDerivatives;
    }
    
    public void update() {
        long currentTime = System.currentTimeMillis();
        deltaTime = currentTime - prevTime;
        prevTime = currentTime;
                
        angles = hardwareInterface.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        gravity = hardwareInterface.imu.getGravity();
        acceleration = hardwareInterface.imu.getLinearAcceleration();
        
        // convert angle from -180:180 (from Compass) to 0:360 (for PID library)
        correctedAngle = (angles.thirdAngle + 360) % 360;
        
        pollEncoders();
        
        /* Update mecanum odometry -- WIP as of 1 March 2021
        int[] encoders = computeEncoderDerivitives();
        Position odoPos = mecanumOdometry(encoders[0], encoders[1], encoders[2], encoders[3], deltaTime);
        position.x += odoPos.x;
        position.y += odoPos.y;
        position.z += odoPos.z;
        */
        

        telemetry.addData("status", hardwareInterface.imu.getSystemStatus().toShortString());
        telemetry.addData("calib", hardwareInterface.imu.getCalibrationStatus().toString());

        //telemetry.addData("position", position.toString());
        telemetry.addData("angle", "(" + angles.firstAngle + ", " + angles.secondAngle + ", " + angles.thirdAngle + ")deg");
        telemetry.addData("acceleration", acceleration.toString());
        
        telemetry.addData("corrected angle", correctedAngle);
    }
    
    /*public void goToHorizontal(float distance, float speed) {
        double ticks = mmToTicks(distance);
        
        try {
            while (opModeIsActive.call()) {
                
                
                telemetry.addData("Horizontal Translation", distance + "mm");
                telemetry.update();
            }
        } catch (Exception e) {}
    }*/
    
    public void goForTime(long millis, float vertical, float horizontal, float rotational, float speed) {
        speed = Math.max(-1.0f, Math.min(1.0f, speed));
        
        telemetry.log().add("Driving for " + millis + " milliseconds at " + speed + " power.");
        
        long now = java.lang.System.currentTimeMillis();
        
        long startTime = now;
        long stopTime = startTime + millis;
        
        long rampDuration = (long) (0.4 * millis);
        long rampUpTime = startTime + rampDuration;
        long rampDownTime = stopTime - rampDuration;
        
        telemetry.log().add("Starting at " + startTime);
        
        hardwareInterface.tankDrive(0, 0);
        
        telemetry.log().add("Ramping up for " + (rampUpTime - startTime));
        
        do {
            now = java.lang.System.currentTimeMillis();
            
            hardwareInterface.mecanumDrive(vertical, horizontal, rotational, (float) (speed * Math.min(((float)(now-startTime)/(float)(rampUpTime-startTime)), 1)));
        } while(now < rampUpTime);
        
        telemetry.log().add("Driving at speed for " + (rampDownTime - rampUpTime));
        
        hardwareInterface.mecanumDrive(vertical, horizontal, rotational, speed);
        
        do {
            now = java.lang.System.currentTimeMillis();
        } while(now < rampDownTime);
        
        telemetry.log().add("Slowing down for " + (stopTime - rampDownTime));
        
        do {
            now = java.lang.System.currentTimeMillis();
            
            hardwareInterface.mecanumDrive(vertical, horizontal, rotational, (float) (speed * (1 - ((float)(now - rampDownTime)/(float)(stopTime - rampDownTime)))));
        } while(now < stopTime);
        
        telemetry.log().add("Stopping at " + stopTime);
        
        hardwareInterface.mecanumDrive(vertical, horizontal, rotational, 0f);
        
        telemetry.log().add("Done!");
    }
    
    /*public void goToVertical(float distance, float speed) {
        float startTicks = frontLeftEncoder;
        
        double ticks = mmToTicks(distance) + startTicks;
        int sign = MathUtils.Sign((float) ticks);
        
        double correctedTicks = Math.abs(ticks);
        
        telemetry.log().add("Starting at time " + java.lang.System.currentTimeMillis());
        telemetry.log().add("going to " + ticks + " ticks");
        
        translationController.reset();
        translationController.setSetpoint(correctedTicks);
        translationController.setOutputRange(0, speed);
        translationController.enable();
        
        telemetry.log().add("starting at " + startTicks + " ticks");
        
        try {
            do {
                update();
                
                float power = (float) translationController.performPID(Math.abs(frontLeftEncoder));
                
                hardwareInterface.mecanumDrive(-sign, 0, 0, power);
                
                telemetry.addData("Target", ticks + " ticks");
                telemetry.addData("Corrected Target", correctedTicks + " ticks");
                telemetry.addData("Current", frontLeftEncoder + " ticks");
                telemetry.update();
            } while (opModeIsActive.call() && !translationController.onTarget());
        } catch (Exception e) {
            telemetry.log().add(e.toString());
        }
        
        hardwareInterface.tankDrive(0, 0);
        
        telemetry.log().add("stopped at " + frontLeftEncoder + " ticks");
        
        translationController.disable();
    }*/
    
    public void goToRelativeRotation(float rotation, float speed) {
        float correctedRotation = (rotation + 360) % 360;
        
        float targetAngle = (float) ((correctedAngle + correctedRotation + 360) % 360);
        
        goToAbsoluteRotation(targetAngle, speed);
    }
    
    public void goToAbsoluteRotation(float rotation, float speed) {
        update();
        
        float targetAngle = (rotation + 360) % 360;
        
        double startAngle = correctedAngle;
        
        telemetry.log().add("starting at " + startAngle);
        telemetry.log().add("going to " + targetAngle);
        
        rotationController.reset();
        rotationController.setSetpoint(targetAngle);
        rotationController.setOutputRange(0, speed);
        rotationController.enable();
        
        telemetry.log().add("start");
        
        try {
            do {
                update();
                
                float power = (float) rotationController.performPID(correctedAngle);
                
                hardwareInterface.mecanumDrive(0, 0, -1, power);
                
                telemetry.addData("Target", rotation + "deg");
                telemetry.addData("Corrected Target", targetAngle + "deg");
                telemetry.addData("Current", correctedAngle + "deg");
                telemetry.update();
            } while (opModeIsActive.call() && !rotationController.onTarget());
        } catch (Exception e) {
            telemetry.log().add(e.toString());
        }
        
        
        hardwareInterface.tankDrive(0, 0);
        
        telemetry.log().add("stop");
        
        rotationController.disable();
    }
    
    public double mmToTicks(float distance) {
        return distance * ticksPerMM;
    }
    
    // WIP, doesn't work as of 1 March 2021
    // public Position mecanumOdometry(float frontLeft, float backLeft, float backRight, float frontRight, long deltaTime) {
    //     float frontLeftFixed = frontLeft / 20;
    //     float backLeftFixed = backLeft / 20;
    //     float backRightFixed = backRight / 20;
    //     float frontRightFixed = frontRight / 20;
    //    
    //     float[] fixedWheels = {
    //         frontLeftFixed,
    //         backLeftFixed,
    //         backRightFixed,
    //         frontRightFixed
    //     };
    //    
    //     float rollerAxisAngle = (float) (Math.PI / 4);
    //    
    //     // Paper defining this math: https://arxiv.org/pdf/1211.2323.pdf
    //    
    //     /*
    //                    4
    //        sin(α)cos(α)Σ(-1)ⁱsgn(wᵢ)Fᵢ
    //                   i=1
    //     */
    //     Vector3 xForce = new Vector3(0, 0, 0);
    //    
    //     /*
    //                4
    //        sin^2(α)Σsgn(wᵢ)Fᵢ
    //               i=1
    //     */
    //     Vector3 yForce = new Vector3(0, 0, 0);
    //    
    //     for (int i = 0; i < 4; i++) {
    //         float xForceDerivative = (float) (Math.pow(-1, i + 1) * MathUtils.Sign(fixedWheels[i]) * Math.abs(fixedWheels[i]));
    //         float yForceDerivative = (float) (MathUtils.Sign(fixedWheels[i]) * Math.abs(fixedWheels[i]));
    //        
    //         xForce = Vector3.Add(xForce, new Vector3(xForceDerivative, 0, 0));
    //         yForce = Vector3.Add(xForce, new Vector3(0, yForceDerivative, 0));
    //     }
    //    
    //     xForce = Vector3.Multiply(xForce, (float) (Math.sin(rollerAxisAngle) * Math.sin(rollerAxisAngle)));
    //     yForce = Vector3.Multiply(xForce, (float) (Math.pow(Math.sin(rollerAxisAngle), 2)));
    //    
    //     float theta = angles.thirdAngle;
    //     /*
    //         (cos(Ø))(xForce)+(-sin(Ø))(xForce)
    //     */
    //     Vector3 XForce = Vector3.Add(Vector3.Multiply(xForce, (float) (Math.cos(theta))), Vector3.Multiply(xForce, (float) (-Math.sin(theta))));
    //    
    //     /*
    //         (sin(Ø))(yForce)+(cos(Ø))(yForce)
    //     */
    //     Vector3 YForce = Vector3.Add(Vector3.Multiply(yForce, (float) (Math.sin(theta))), Vector3.Multiply(yForce, (float) (Math.cos(theta))));
    //    
    //     /*
    //         (XForce)uₓ+(YForce)uᵧ
    //     */
    //     //Vector3 LForce = Vector3.Add(Vector3.Scale(XForce, new Vector3(1, 0, 0)), Vector3.Scale(YForce, new Vector3(0, 1, 0)));
    //    
    //     Vector3 translation = Vector3.Add(XForce, YForce);
    //    
    //     return Vector3.ToPosition(translation, DistanceUnit.METER);
    // }
}