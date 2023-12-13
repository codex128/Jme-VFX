/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers;

import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import com.jme3.math.ColorRGBA;
import com.jme3.math.EaseFunction;
import com.jme3.math.Easing;

/**
 *
 * @author codex
 */
public class ColorOverLifetime implements ParticleDriver {
    
    private final ColorRGBA lower, upper;
    private EaseFunction ease = Easing.linear;
    
    public ColorOverLifetime(ColorRGBA lower, ColorRGBA upper) {
        this.lower = lower;
        this.upper = upper;
    }
    public ColorOverLifetime(ColorRGBA lower, ColorRGBA upper, EaseFunction ease) {
        this.lower = lower;
        this.upper = upper;
        this.ease = ease;
    }
    
    @Override
    public void updateGroup(ParticleGroup group, float tpf) {}
    @Override
    public void updateParticle(ParticleData particle, float tpf) {
        particle.color = getBlendedColor(ease.apply(particle.getLifePercent()));
    }
    
    private ColorRGBA getBlendedColor(float blend) {
        return new ColorRGBA(
            extrapolate(blend, lower.r, upper.r),
            extrapolate(blend, lower.g, upper.g),
            extrapolate(blend, lower.b, upper.b),
            extrapolate(blend, lower.a, upper.a)
        );
    }
    private static float extrapolate(float scale, float a, float b) {
        return (b-a)*scale+a;
    }
    
}
