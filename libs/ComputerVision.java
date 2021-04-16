package org.firstinspires.ftc.teamcode.libs;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import java.util.List;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import java.util.logging.Logger;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaSkyStone;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gyroscope;

public class ComputerVision {
    private static final String tfodModelAsset = "UltimateGoal.tflite";
    private static final String firstElementLabel = "Quad";
    private static final String secondElementLabel = "Single";
    private static final String vuforiaKey = "Aa3OIg3/////AAABmdLmLx3lSUTHpk9Nvqpb1V6KHl5igGGW89pt95cHqOSXiW0Y/3crX/Iu6MO4twnhyJqTcQ+sWadUeiIaUlArjbhkTbmC1CikrD8l66v9vTsePjIk9EmJZ/Ebs2mtHWhgG5IkdIGLrPWUT/0QYYZFIQ52M5oVftUnDDb6jYzjD2wyyOZlALCsBZaZX2MpDIJZmd+d2xcSkAZWBtXqR4So2qpSxgSsR+KVWP2b+iJY2ySuaQH1rGkyxH+xbNOqoiWSFLcReWjq7qVP64vyzDRfL5FYyAj7z56mjLlCI4Tmpw6D28wWstMxUkkO0DWNQ6sC0ACmGVFRQRsVJI7Tf3ALPGcX0YphDRJjiawU3JxpVQTX";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private HardwareMap hardwareMap;
    
    public ComputerVision(HardwareMap hm) {
        hardwareMap = hm;
    }

    public boolean start() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = vuforiaKey;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(tfodModelAsset, firstElementLabel, secondElementLabel);

        if (tfod != null) {
            tfod.activate();
            //tfod.setZoom(1, 16.0/9.0);
        } else return false;
        
        return true;
    }
    
    public String getFirstLabel() {
        return firstElementLabel;
    }
    
    public String getSecondLabel() {
        return secondElementLabel;
    }
    
    public List<Recognition> getUpdatedRecognitions() {
        if (tfod != null) return tfod.getUpdatedRecognitions();
        else return null;
    }
        
    public void stop() {
        if (tfod != null) tfod.shutdown();
    }
}
