package org.firstinspires.ftc.teamcode.math;

import java.util.Set;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import java.util.Vector;


public class Vector3 { // Unity's Vector3 library decompiled through dnSpy

    /*// Token: 0x06000FD2 RID: 4050 RVA: 0x0001781A File Offset: 0x00015A1A
    public static void OrthoNormalize(Vector3 normal, Vector3 tangent) {
        Vector3.OrthoNormalize2(normal, tangent);
    }

    // Token: 0x06000FD4 RID: 4052 RVA: 0x00017825 File Offset: 0x00015A25
    public static void OrthoNormalize(Vector3 normal, Vector3 tangent, Vector3 binormal) {
        Vector3.OrthoNormalize3(normal, tangent, binormal);
    }*/

    // Token: 0x06000FD6 RID: 4054 RVA: 0x00017850 File Offset: 0x00015A50
    public static Vector3 Lerp(Vector3 a, Vector3 b, float t) {
        t = MathUtils.Clamp01(t);
        return new Vector3(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t, a.z + (b.z - a.z) * t);
    }

    // Token: 0x06000FD7 RID: 4055 RVA: 0x000178B4 File Offset: 0x00015AB4
    public static Vector3 LerpUnclamped(Vector3 a, Vector3 b, float t) {
        return new Vector3(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t, a.z + (b.z - a.z) * t);
    }

    // Token: 0x06000FD8 RID: 4056 RVA: 0x00017910 File Offset: 0x00015B10
    public static Vector3 MoveTowards(Vector3 current, Vector3 target, float maxDistanceDelta) {
        float num = target.x - current.x;
        float num2 = target.y - current.y;
        float num3 = target.z - current.z;
        float num4 = num * num + num2 * num2 + num3 * num3;
        boolean flag = num4 == 0f || (maxDistanceDelta >= 0f && num4 <= maxDistanceDelta * maxDistanceDelta);
        Vector3 result;
        if (flag) {
            result = target;
        } else {
            float num5 = (float) Math.sqrt((double) num4);
            result = new Vector3(current.x + num / num5 * maxDistanceDelta, current.y + num2 / num5 * maxDistanceDelta, current.z + num3 / num5 * maxDistanceDelta);
        }
        return result;
    }

    // Token: 0x06000FDA RID: 4058 RVA: 0x000179E0 File Offset: 0x00015BE0
    public static Vector3 SmoothDamp(Vector3 current, Vector3 target, Vector3 currentVelocity, float smoothTime, float deltaTime) {
        float positiveInfinity = Float.POSITIVE_INFINITY;
        return Vector3.SmoothDamp(current, target, currentVelocity, smoothTime, positiveInfinity, deltaTime);
    }

    // Token: 0x06000FDB RID: 4059 RVA: 0x00017A0C File Offset: 0x00015C0C
    public static Vector3 SmoothDamp(Vector3 current, Vector3 target, Vector3 currentVelocity, float smoothTime, float maxSpeed, float deltaTime) {
        smoothTime = Math.max(0.0001f, smoothTime);
        float num = 2f / smoothTime;
        float num2 = num * deltaTime;
        float num3 = 1f / (1f + num2 + 0.48f * num2 * num2 + 0.235f * num2 * num2 * num2);
        float num4 = current.x - target.x;
        float num5 = current.y - target.y;
        float num6 = current.z - target.z;
        Vector3 vector = target;
        float num7 = maxSpeed * smoothTime;
        float num8 = num7 * num7;
        float num9 = num4 * num4 + num5 * num5 + num6 * num6;
        boolean flag = num9 > num8;
        if (flag) {
            float num10 = (float) Math.sqrt((double) num9);
            num4 = num4 / num10 * num7;
            num5 = num5 / num10 * num7;
            num6 = num6 / num10 * num7;
        }
        target.x = current.x - num4;
        target.y = current.y - num5;
        target.z = current.z - num6;
        float num11 = (currentVelocity.x + num * num4) * deltaTime;
        float num12 = (currentVelocity.y + num * num5) * deltaTime;
        float num13 = (currentVelocity.z + num * num6) * deltaTime;
        currentVelocity.x = (currentVelocity.x - num * num11) * num3;
        currentVelocity.y = (currentVelocity.y - num * num12) * num3;
        currentVelocity.z = (currentVelocity.z - num * num13) * num3;
        float num14 = target.x + (num4 + num11) * num3;
        float num15 = target.y + (num5 + num12) * num3;
        float num16 = target.z + (num6 + num13) * num3;
        float num17 = vector.x - current.x;
        float num18 = vector.y - current.y;
        float num19 = vector.z - current.z;
        float num20 = num14 - vector.x;
        float num21 = num15 - vector.y;
        float num22 = num16 - vector.z;
        boolean flag2 = num17 * num20 + num18 * num21 + num19 * num22 > 0f;
        if (flag2) {
            num14 = vector.x;
            num15 = vector.y;
            num16 = vector.z;
            currentVelocity.x = (num14 - vector.x) / deltaTime;
            currentVelocity.y = (num15 - vector.y) / deltaTime;
            currentVelocity.z = (num16 - vector.z) / deltaTime;
        }
        return new Vector3(num14, num15, num16);
    }

    // Token: 0x06000FDE RID: 4062 RVA: 0x00017D2A File Offset: 0x00015F2A
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Token: 0x06000FDF RID: 4063 RVA: 0x00017D42 File Offset: 0x00015F42
    public Vector3(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = 0f;
    }

    // Token: 0x06000FE0 RID: 4064 RVA: 0x00017D2A File Offset: 0x00015F2A
    public void Set(float newX, float newY, float newZ) {
        this.x = newX;
        this.y = newY;
        this.z = newZ;
    }

    // Token: 0x06000FE1 RID: 4065 RVA: 0x00017D60 File Offset: 0x00015F60
    public static Vector3 Scale(Vector3 a, Vector3 b) {
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    // Token: 0x06000FE2 RID: 4066 RVA: 0x00017D9E File Offset: 0x00015F9E
    public void Scale(Vector3 scale) {
        this.x *= scale.x;
        this.y *= scale.y;
        this.z *= scale.z;
    }

    // Token: 0x06000FE3 RID: 4067 RVA: 0x00017DDC File Offset: 0x00015FDC
    public static Vector3 Cross(Vector3 lhs, Vector3 rhs) {
        return new Vector3(lhs.y * rhs.z - lhs.z * rhs.y, lhs.z * rhs.x - lhs.x * rhs.z, lhs.x * rhs.y - lhs.y * rhs.x);
    }

    // Token: 0x06000FE4 RID: 4068 RVA: 0x00017E44 File Offset: 0x00016044
    public int GetHashCode() {
        return (new Float(this.x)).hashCode() ^ (new Float(this.y)).hashCode() << 2 ^ (new Float(this.z)).hashCode() >> 2;
    }

    // Token: 0x06000FE6 RID: 4070 RVA: 0x00017EB4 File Offset: 0x000160B4
    public boolean Equals(Vector3 other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    public static boolean Equals(Vector3 first, Vector3 other) {
        return first.x == other.x && first.y == other.y && first.z == other.z;
    }

    // Token: 0x06000FE7 RID: 4071 RVA: 0x00017EF4 File Offset: 0x000160F4
    public static Vector3 Reflect(Vector3 inDirection, Vector3 inNormal) {
        float num = -2f * Vector3.Dot(inNormal, inDirection);
        return new Vector3(num * inNormal.x + inDirection.x, num * inNormal.y + inDirection.y, num * inNormal.z + inDirection.z);
    }

    // Token: 0x06000FE8 RID: 4072 RVA: 0x00017F48 File Offset: 0x00016148
    public static Vector3 Normalize(Vector3 value) {
        float num = Vector3.Magnitude(value);
        boolean flag = num > 1E-05f;
        Vector3 result;
        if (flag) {
            result = Vector3.Divide(value, num);
        } else {
            result = Vector3.zero;
        }
        return result;
    }

    // Token: 0x06000FE9 RID: 4073 RVA: 0x00017F7C File Offset: 0x0001617C
    public void Normalize() {
        float num = Vector3.Magnitude(this);
        boolean flag = num > 1E-05f;
        if (flag) {
            this.Set(Vector3.Divide(this, num));
        } else {
            this.Set(Vector3.zero);
        }
    }

    // Token: 0x06000FEB RID: 4075 RVA: 0x00017FE4 File Offset: 0x000161E4
    public static float Dot(Vector3 lhs, Vector3 rhs) {
        return lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z;
    }

    // Token: 0x06000FEC RID: 4076 RVA: 0x00018020 File Offset: 0x00016220
    public static Vector3 Project(Vector3 vector, Vector3 onNormal) {
        float num = Vector3.Dot(onNormal, onNormal);
        boolean flag = num < MathUtils.Epsilon;
        Vector3 result;
        if (flag) {
            result = Vector3.zero;
        } else {
            float num2 = Vector3.Dot(vector, onNormal);
            result = new Vector3(onNormal.x * num2 / num, onNormal.y * num2 / num, onNormal.z * num2 / num);
        }
        return result;
    }

    // Token: 0x06000FED RID: 4077 RVA: 0x0001807C File Offset: 0x0001627C
    public static Vector3 ProjectOnPlane(Vector3 vector, Vector3 planeNormal) {
        float num = Vector3.Dot(planeNormal, planeNormal);
        boolean flag = num < MathUtils.Epsilon;
        Vector3 result;
        if (flag) {
            result = vector;
        } else {
            float num2 = Vector3.Dot(vector, planeNormal);
            result = new Vector3(vector.x - planeNormal.x * num2 / num, vector.y - planeNormal.y * num2 / num, vector.z - planeNormal.z * num2 / num);
        }
        return result;
    }

    // Token: 0x06000FEE RID: 4078 RVA: 0x000180E8 File Offset: 0x000162E8
    public static float Angle(Vector3 from, Vector3 to) {
        float num = (float) Math.sqrt((double)(from.SqrMagnitude() * to.SqrMagnitude()));
        boolean flag = num < 1E-15f;
        float result;
        if (flag) {
            result = 0f;
        } else {
            float num2 = MathUtils.Clamp(Vector3.Dot(from, to) / num, -1f, 1f);
            result = (float) Math.acos((double) num2) * 57.29578f;
        }
        return result;
    }

    // Token: 0x06000FEF RID: 4079 RVA: 0x0001814C File Offset: 0x0001634C
    public static float SignedAngle(Vector3 from, Vector3 to, Vector3 axis) {
        float num = Vector3.Angle(from, to);
        float num2 = from.y * to.z - from.z * to.y;
        float num3 = from.z * to.x - from.x * to.z;
        float num4 = from.x * to.y - from.y * to.x;
        float num5 = Math.signum(axis.x * num2 + axis.y * num3 + axis.z * num4);
        return num * num5;
    }

    // Token: 0x06000FF0 RID: 4080 RVA: 0x000181E4 File Offset: 0x000163E4
    public static float Distance(Vector3 a, Vector3 b) {
        float num = a.x - b.x;
        float num2 = a.y - b.y;
        float num3 = a.z - b.z;
        return (float) Math.sqrt((double)(num * num + num2 * num2 + num3 * num3));
    }

    // Token: 0x06000FF1 RID: 4081 RVA: 0x00018234 File Offset: 0x00016434
    public static Vector3 ClampMagnitude(Vector3 vector, float maxLength) {
        float sqrMagnitude = vector.SqrMagnitude();
        boolean flag = sqrMagnitude > maxLength * maxLength;
        Vector3 result;
        if (flag) {
            float num = (float) Math.sqrt((double) sqrMagnitude);
            float num2 = vector.x / num;
            float num3 = vector.y / num;
            float num4 = vector.z / num;
            result = new Vector3(num2 * maxLength, num3 * maxLength, num4 * maxLength);
        } else {
            result = vector;
        }
        return result;
    }

    // Token: 0x06000FF2 RID: 4082 RVA: 0x00018298 File Offset: 0x00016498
    public static float Magnitude(Vector3 vector) {
        return (float) Math.sqrt((double)(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z));
    }

    public float Magnitude() {
        return (float) Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
    }

    // Token: 0x06000FF4 RID: 4084 RVA: 0x00018320 File Offset: 0x00016520
    public static float SqrMagnitude(Vector3 vector) {
        return vector.x * vector.x + vector.y * vector.y + vector.z * vector.z;
    }

    public float SqrMagnitude() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    // Token: 0x06000FF6 RID: 4086 RVA: 0x00018398 File Offset: 0x00016598
    public static Vector3 Min(Vector3 lhs, Vector3 rhs) {
        return new Vector3(Math.min(lhs.x, rhs.x), Math.min(lhs.y, rhs.y), Math.min(lhs.z, rhs.z));
    }

    // Token: 0x06000FF7 RID: 4087 RVA: 0x000183E4 File Offset: 0x000165E4
    public static Vector3 Max(Vector3 lhs, Vector3 rhs) {
        return new Vector3(Math.max(lhs.x, rhs.x), Math.max(lhs.y, rhs.y), Math.max(lhs.z, rhs.z));
    }

    // Token: 0x06001002 RID: 4098 RVA: 0x00018520 File Offset: 0x00016720
    public static Vector3 Add(Vector3 a, Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    // Token: 0x06001003 RID: 4099 RVA: 0x00018560 File Offset: 0x00016760
    public static Vector3 Subtract(Vector3 a, Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    // Token: 0x06001004 RID: 4100 RVA: 0x000185A0 File Offset: 0x000167A0
    public static Vector3 Invert(Vector3 a) {
        return new Vector3(-a.x, -a.y, -a.z);
    }

    // Token: 0x06001005 RID: 4101 RVA: 0x000185CC File Offset: 0x000167CC
    public static Vector3 Multiply(Vector3 a, float d) {
        return new Vector3(a.x * d, a.y * d, a.z * d);
    }

    // Token: 0x06001007 RID: 4103 RVA: 0x0001862C File Offset: 0x0001682C
    public static Vector3 Divide(Vector3 a, float d) {
        return new Vector3(a.x / d, a.y / d, a.z / d);
    }
    
    public void Set(Vector3 a) {
        this.x = a.x;
        this.x = a.y;
        this.x = a.z;
    }

    // Token: 0x0600100B RID: 4107 RVA: 0x00018718 File Offset: 0x00016918
    public String ToString() {
        return String.format("(%f, %f, %f)",
            this.x,
            this.y,
            this.z
        );
    }
    
    public static Position ToPosition(Vector3 vector, DistanceUnit unit) {
        return new Position(unit, vector.x, vector.y, vector.z, System.nanoTime());
    }
    
    public static Vector3 FromPosition(Position position) {
        return new Vector3((float) position.x, (float) position.y, (float) position.z);
    }

    // Token: 0x06001010 RID: 4112
    //private static extern void Slerp(ref Vector3 a, ref Vector3 b, float t, out Vector3 ret);

    // Token: 0x06001011 RID: 4113
    //private static extern void SlerpUnclamped(ref Vector3 a, ref Vector3 b, float t, out Vector3 ret);

    // Token: 0x06001012 RID: 4114
    //private static extern void RotateTowards(ref Vector3 current, ref Vector3 target, float maxRadiansDelta, float maxMagnitudeDelta, out Vector3 ret);

    // Token: 0x0400059D RID: 1437
    public final float kEpsilon = 1E-05f;

    // Token: 0x0400059E RID: 1438
    public final float kEpsilonNormalSqrt = 1E-15f;

    // Token: 0x0400059F RID: 1439
    public float x;

    // Token: 0x040005A0 RID: 1440
    public float y;

    // Token: 0x040005A1 RID: 1441
    public float z;

    // Token: 0x040005A2 RID: 1442
    public static final Vector3 zero = new Vector3(0f, 0f, 0f);

    // Token: 0x040005A3 RID: 1443
    public static final Vector3 oneVector = new Vector3(1f, 1f, 1f);

    // Token: 0x040005A4 RID: 1444
    public static final Vector3 upVector = new Vector3(0f, 1f, 0f);

    // Token: 0x040005A5 RID: 1445
    public static final Vector3 downVector = new Vector3(0f, -1f, 0f);

    // Token: 0x040005A6 RID: 1446
    public static final Vector3 leftVector = new Vector3(-1f, 0f, 0f);

    // Token: 0x040005A7 RID: 1447
    public static final Vector3 rightVector = new Vector3(1f, 0f, 0f);

    // Token: 0x040005A8 RID: 1448
    public static final Vector3 forwardVector = new Vector3(0f, 0f, 1f);

    // Token: 0x040005A9 RID: 1449
    public static final Vector3 backVector = new Vector3(0f, 0f, -1f);

    // Token: 0x040005AA RID: 1450
    public static final Vector3 positiveInfinityVector = new Vector3(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

    // Token: 0x040005AB RID: 1451
    public static final Vector3 negativeInfinityVector = new Vector3(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
}
