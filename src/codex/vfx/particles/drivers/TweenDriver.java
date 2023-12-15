/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers;

import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.utils.Value;
import com.jme3.anim.tween.Tween;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 *
 * @author codex
 */
public class TweenDriver implements ParticleDriver {
    
    private Value<Float> lifetime = Value.value(1f);
    private Value<Vector3f> linearVelocity = Value.value(Vector3f.ZERO);
    private Value<Vector3f> angularVelocity = Value.value(Vector3f.ZERO);
    private Value<ColorRGBA> color = Value.value(ColorRGBA.White);
    private Value<Float> size = Value.value(1f);
    private Value<Integer> particlesPerEmission = Value.value(1);
    
    private float delay = 0;
    private Tween tween;
    
    @Override
    public void updateGroup(ParticleGroup group, float tpf) {
        
    }
    @Override
    public void updateParticle(ParticleData particle, float tpf) {}
    @Override
    public void particleAdded(ParticleData particle) {}
    
}
