/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.tweens;

import codex.vfx.utils.VfxUtils;

/**
 *
 * @author codex
 */
public class Random <T> implements Value<T> {

    private T a, b;
    private T value;
    private Interpolator<T> interpolator;
    private float pickTime = 0;    
    private float time = 0;

    public Random(T a, T b, Interpolator<T> interpolator) {
        this(a, b, interpolator, 0);
    }
    public Random(T a, T b, Interpolator<T> interpolator, float pickTime) {
        this.a = a;
        this.b = b;
        this.interpolator = interpolator;
        update(0, 0);
        this.pickTime = pickTime;
    }
    
    @Override
    public T get() {
        return value;
    }
    @Override
    public void set(T val) {}
    @Override
    public final void update(float time, float tpf) {
        this.time += tpf;
        if (this.time >= pickTime) {
            value = interpolator.interpolate(VfxUtils.gen.nextFloat(), a, b);
            this.time = 0;
        }
    }
    
}
