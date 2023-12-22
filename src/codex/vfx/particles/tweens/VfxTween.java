/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.tweens;

import com.jme3.anim.tween.AbstractTween;
import com.jme3.anim.tween.Tweens;
import com.jme3.math.EaseFunction;
import com.jme3.math.Easing;

/**
 * Animation and cinematic tween for VFX applications.
 * <p>
 * This is designed to write results to {@link Value}, so
 * this tween can basically be used for anything using {@link Value}.
 * 
 * @author codex
 * @param <T>
 */
public class VfxTween <T> extends AbstractTween {

    private Value<T> output;
    private final Interpolator<T> interpolator;
    private EaseFunction ease;
    private T a, b;
    
    public VfxTween(Value<T> output, Interpolator<T> interpolator, T a, T b, double length) {
        this(output, interpolator, Easing.linear, a, b, length);
    }
    public VfxTween(Value<T> output, Interpolator<T> interpolator, EaseFunction ease, T a, T b, double length) {
        super(length);
        this.output = output;
        this.interpolator = interpolator;
        this.ease = ease;
        this.a = a;
        this.b = b;
    }

    @Override
    protected void doInterpolate(double t) {
        output.set(interpolator.interpolate(ease.apply((float)t), a, b));
    }

    public void setOutput(Value<T> output) {
        this.output = output;
    }
    public void setEase(EaseFunction ease) {
        this.ease = ease;
    }
    public void setA(T a) {
        this.a = a;
    }
    public void setB(T b) {
        this.b = b;
    }
    
    public Value<T> getOutput() {
        return output;
    }
    public Interpolator<T> getInterpolator() {
        return interpolator;
    }
    public EaseFunction getEase() {
        return ease;
    }
    public T getA() {
        return a;
    }
    public T getB() {
        return b;
    }
    
}
