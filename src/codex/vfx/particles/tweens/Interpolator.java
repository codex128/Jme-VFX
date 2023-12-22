/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx.particles.tweens;

import com.jme3.math.ColorRGBA;
import com.jme3.math.EaseFunction;
import com.jme3.math.Easing;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * Performs linear interpolation between two objects.
 * 
 * @author codex
 * @param <T> type of object to interpolate
 */
public interface Interpolator <T> {
    
    /**
     * Performs linear interpolation between two objects and returns the result.
     * 
     * @param t time, scale, or blend value between 0 and 1 (inclusive)
     * @param a first object, fully returned at 0
     * @param b second object, fully returned at 1
     * @return interpolated object
     */
    public T interpolate(float t, T a, T b);
    
    /**
     * Interpolates between two integers.
     */
    public static final Interpolator<Integer> Integer = (float t, Integer a, Integer b) -> {
        return (int)(t*(b-a)+a);
    };
    
    /**
     * Interpolates between two floats.
     */
    public static final Interpolator<Float> Float = (float t, Float a, Float b) -> {
        return (b-a)*t+a;
    };
    
    /**
     * Interpolates between two {@link Vector3f} objects.
     */
    public static final Interpolator<Vector3f> Vector3f = (float t, Vector3f a, Vector3f b) -> {
        return b.subtract(a).multLocal(t).addLocal(a);
    };
    
    public static final Interpolator<Vector2f> Vector2f = (float t, Vector2f a, Vector2f b) -> {
        return b.subtract(a).multLocal(t).addLocal(a);
    };
    
    public static final Interpolator<ColorRGBA> Color = (float t, ColorRGBA a, ColorRGBA b) -> {
        return new ColorRGBA().interpolateLocal(a, b, t);
    };
    
}
