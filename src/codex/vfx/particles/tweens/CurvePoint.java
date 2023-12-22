/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.tweens;

import com.jme3.math.EaseFunction;

/**
 * Defines one section of a curve.
 * 
 * @author codex
 * @param <T>
 */
public class CurvePoint <T> {
    
    private T value;
    private float position;
    private EaseFunction easing;

    public CurvePoint(T value, float position) {
        this(value, position, Ease.inLinear);
    }
    public CurvePoint(T value, float position, EaseFunction easing) {
        this.value = value;
        this.position = position;
        this.easing = easing;
    }

    public T getValue() {
        return value;
    }
    public float getPosition() {
        return position;
    }
    public EaseFunction getEasing() {
        return easing;
    }
    
}
