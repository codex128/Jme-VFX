/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.tweens;

import com.jme3.math.EaseFunction;
import com.jme3.math.FastMath;
import codex.vfx.particles.tweens.Interpolator;

/**
 * Represents a range of values between two objects.
 * 
 * @author codex
 * @param <T>
 */
public class Range <T> implements Value<T> {
    
    private T a, b;
    private T value;
    private Interpolator<T> interpolator;
    private EaseFunction ease;

    public Range(T a, T b, Interpolator<T> interpolator, EaseFunction ease) {
        this.a = a;
        this.b = b;
        this.interpolator = interpolator;
        this.ease = ease;
        this.value = this.a;
    }
    
    @Override
    public void update(float time, float tpf) {
        value = interpolator.interpolate(ease.apply(FastMath.clamp(time, 0, 1)), a, b);
    }
    @Override
    public T get() {
        return value;
    }
    @Override
    public void set(T val) {}

    public void setA(T a) {
        this.a = a;
    }
    public void setB(T b) {
        this.b = b;
    }
    public void setInterpolator(Interpolator<T> interpolator) {
        this.interpolator = interpolator;
    }
    public void setEase(EaseFunction ease) {
        this.ease = ease;
    }
    
    public T getA() {
        return a;
    }
    public T getB() {
        return b;
    }
    public Interpolator<T> getInterpolator() {
        return interpolator;
    }
    public EaseFunction getEase() {
        return ease;
    }
    
}
