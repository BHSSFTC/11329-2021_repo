package org.firstinspires.ftc.teamcode.math;

public class MathUtils {
    public static float Clamp(float value, float min, float max) {
        boolean flag = value < min;
        if (flag) {
            value = min;
        } else {
            boolean flag2 = value > max;
            if (flag2) {
                value = max;
            }
        }
        return value;
    }

    public static int Clamp(int value, int min, int max) {
        boolean flag = value < min;
        if (flag) {
            value = min;
        } else {
            boolean flag2 = value > max;
            if (flag2) {
                value = max;
            }
        }
        return value;
    }

    public static float Clamp01(float value) {
        return MathUtils.Clamp(value, 0, 1);
    }
    
    public static final float Epsilon = Float.MIN_VALUE;

    public static int Sign(float value) {
        if (value < 0) return -1;
        if (value > 0) return 1;
        return 0;
    }
}
