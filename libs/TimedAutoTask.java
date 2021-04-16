package org.firstinspires.ftc.teamcode.libs;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.util.concurrent.Callable;


public class TimedAutoTask {
    public Runnable taskCode;
    public Callable<Boolean> opModeIsActive;
    public String taskName;
    public Telemetry telemetry;
    public long milliseconds;
    
    public TimedAutoTask(Runnable tc, Callable<Boolean> oma, String tn, Telemetry tm, long ms) {
        taskCode = tc;
        opModeIsActive = oma;
        taskName = tn;
        telemetry = tm;
        milliseconds = ms;
    }
    
    public void run() {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + milliseconds;
        
        try {
            while (System.currentTimeMillis() <= endTime && opModeIsActive.call()) {
                taskCode.run();
        
                //telemetry.addData("Auto Stage", taskName);
                //telemetry.addData("Timing", (System.currentTimeMillis() - startTime) + " ms/" + (endTime - startTime) + " ms");
                //telemetry.update();
            }
        } catch (Exception e) { }
    }
}
