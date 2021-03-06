package org.firstinspires.ftc.teamcode.libs;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.util.concurrent.Callable;


public class TimedAutoTaskNonBlocking {
    public Runnable taskCode;
    public String taskName;
    public Telemetry telemetry;
    public long milliseconds;
    public long startTime;
    public long endTime;
    
    public TimedAutoTaskNonBlocking(Runnable tc, String tn, Telemetry tm, long ms) {
        taskCode = tc;
        taskName = tn;
        telemetry = tm;
        milliseconds = ms;
        
        startTime = System.currentTimeMillis();
        endTime = startTime + milliseconds;
    }
    
    public void run() {
        try {
            if (System.currentTimeMillis() <= endTime) {
                taskCode.run();
        
                telemetry.addData("Auto Stage", taskName);
                telemetry.addData("Timing", (System.currentTimeMillis() - startTime) + " ms/" + (endTime - startTime) + " ms");
                telemetry.update();
            }
        } catch (Exception e) { }
    }
}
