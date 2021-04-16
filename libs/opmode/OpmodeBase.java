package org.firstinspires.ftc.teamcode.libs.opmode;

import org.firstinspires.ftc.teamcode.libs.*;
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

public abstract class OpmodeBase extends LinearOpMode {
    public HardwareInterface hardwareInterface;
    public AutoNav autoNav;
    
    public Callable<Boolean> opModeActiveCallable = new Callable<Boolean>() {
        public Boolean call() {
            return opModeIsActive();
        }
    };

    @Override
    public void runOpMode() {
        hardwareInterface = new HardwareInterface(hardwareMap);
        hardwareInterface.startRingPresenceCheckerThread();
    
        autoNav = new AutoNav(opModeActiveCallable, telemetry, hardwareInterface);
        autoNav.update();

        run();

        hardwareInterface.stopAll();
        hardwareInterface.stopRingPresenceCheckerThread();
    }

    public abstract void run();
}
