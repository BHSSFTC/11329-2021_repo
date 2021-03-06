package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.libs.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.concurrent.Callable;

@TeleOp

public class AutoNavTest extends LinearOpMode {
    public HardwareInterface hardwareInterface;
    public OperatorInterface operatorInterface;
    public AutoNav autoNav;
    
    public Callable<Boolean> opModeActiveCallable = new Callable<Boolean>() {
        public Boolean call() {
            return opModeIsActive();
        }
    };
    
    @Override
    public void runOpMode() {
        operatorInterface = new OperatorInterface(gamepad1, gamepad2);
        hardwareInterface = new HardwareInterface(hardwareMap);
        autoNav = new AutoNav(opModeActiveCallable, telemetry, hardwareInterface);
        autoNav.update();

        waitForStart();
        
        //autoNav.goToAbsoluteRotation(0, 1f);
        
        //autoNav.goLongitudinalDist(-500, 0.2f);
        
        // seconds, speed (-1.0 to 1.0)
        //autoNav.goForTime(2f, 1f, 0f, 0f, -0.4f);
        // seconds, longitudinal, transverse, rotational, speed
        //autoNav.goForTime(2f, 0f, 1f, 1f, 0.4f);
        
        while (opModeIsActive()) {
            //hardwareInterface.mecanumDrive(
            //    -operatorInterface.getDriveVertical(),
            //    -operatorInterface.getDriveHorizontal(),
            //    operatorInterface.getDriveRotational(),
            //    operatorInterface.getDriveSpeed()
            //);
            autoNav.update();
            telemetry.update();
        }
    }
}
