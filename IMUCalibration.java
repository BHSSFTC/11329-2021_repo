package org.firstinspires.ftc.teamcode;

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

@TeleOp(name = "IMU Calibration")
public class IMUCalibration extends LinearOpMode {
    BNO055IMU imu;
    Orientation angles;
    boolean pushingA = false;

    @Override
    public void runOpMode() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.mode = BNO055IMU.SensorMode.NDOF;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        writeTelemetry();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                if (!pushingA) {
                    BNO055IMU.CalibrationData calibrationData = imu.readCalibrationData();

                    String filename = "IMU_CALIBRATION.json";
                    File file = AppUtil.getInstance().getSettingsFile(filename);
                    ReadWriteFile.writeFile(file, calibrationData.serialize());
                    telemetry.log().add("saved: " + filename);
                }
                pushingA = true;
            } else pushingA = false;

            writeTelemetry();
        }
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
