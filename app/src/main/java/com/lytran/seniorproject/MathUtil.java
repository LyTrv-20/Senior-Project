package com.lytran.seniorproject;

public class MathUtil {
    public static double clamp(double max, double min, double value){
        return Math.min(max, Math.max(min, value));
    }

    public static double interpolate(double start, double end, double t){
        return start + (end - start) * clamp(1, 0 , t);
    }
}
