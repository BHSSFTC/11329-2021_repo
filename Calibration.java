package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.libs.*;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

@TeleOp(name = "Calibration")
public class Calibration extends LinearOpMode {
    BNO055IMU imu;
    Orientation angles;
    RevColorSensorV3 firstRingSensor;
    RevColorSensorV3 secondRingSensor;
    RevColorSensorV3 thirdRingSensor;
    ColorSensorCalibration colorSensor1Calibration;
    ColorSensorCalibration colorSensor2Calibration;
    ColorSensorCalibration colorSensor3Calibration;
    boolean pushingA = false;
    boolean pushingB = false;

    @Override
    public void runOpMode() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.mode = BNO055IMU.SensorMode.NDOF;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        
        firstRingSensor = hardwareMap.get(RevColorSensorV3.class, "firstRingSensor");
        secondRingSensor = hardwareMap.get(RevColorSensorV3.class, "secondRingSensor");
        thirdRingSensor = hardwareMap.get(RevColorSensorV3.class, "thirdRingSensor");
        colorSensor1Calibration = new ColorSensorCalibration();
        colorSensor2Calibration = new ColorSensorCalibration();
        colorSensor3Calibration = new ColorSensorCalibration();

        writeTelemetry();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                if (!pushingA) {
                    BNO055IMU.CalibrationData calibrationData = imu.readCalibrationData();

                    writeFile(RobotConfig.imuConfigFileName, calibrationData.serialize());
                }
                pushingA = true;
            } else pushingA = false;
            
            if (gamepad1.b) {
                if (!pushingB) {
                    writeFile(RobotConfig.colorSensor1ConfigFileName, colorSensor1Calibration.serialize());
                    writeFile(RobotConfig.colorSensor2ConfigFileName, colorSensor2Calibration.serialize());
                    writeFile(RobotConfig.colorSensor3ConfigFileName, colorSensor3Calibration.serialize());
                }
                pushingB = true;
            } else pushingB = false;
            
            if (gamepad1.x) {
                colorSensor1Calibration.setAbsentRGB(firstRingSensor.red(), firstRingSensor.green(), firstRingSensor.blue());
                colorSensor2Calibration.setAbsentRGB(secondRingSensor.red(), secondRingSensor.green(), secondRingSensor.blue());
                colorSensor3Calibration.setAbsentRGB(thirdRingSensor.red(), thirdRingSensor.green(), thirdRingSensor.blue());
            }
            
            if (gamepad1.y) {
                colorSensor1Calibration.setPresentRGB(firstRingSensor.red(), firstRingSensor.green(), firstRingSensor.blue());
                colorSensor2Calibration.setPresentRGB(secondRingSensor.red(), secondRingSensor.green(), secondRingSensor.blue());
                colorSensor3Calibration.setPresentRGB(thirdRingSensor.red(), thirdRingSensor.green(), thirdRingSensor.blue());
            }

            writeTelemetry();
        }
    }
    
    void writeFile(String filename, String data) {
        File file = AppUtil.getInstance().getSettingsFile(filename);
        ReadWriteFile.writeFile(file, data);
        telemetry.log().add("saved: " + filename);
    }

    void writeTelemetry() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

        telemetry.addData("status", imu.getSystemStatus().toShortString());
        telemetry.addData("calib", imu.getCalibrationStatus().toString());

        telemetry.addData("pitch", formatAngle(angles.angleUnit, angles.firstAngle));
        telemetry.addData("roll", formatAngle(angles.angleUnit, angles.secondAngle));
        telemetry.addData("heading", formatAngle(angles.angleUnit, angles.thirdAngle));
        
        telemetry.update();
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format("%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
