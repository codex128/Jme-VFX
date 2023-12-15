/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.particles.ParticleData;
import codex.vfx.utils.Value;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;

/**
 * Creates basic particles.
 * 
 * @author codex
 */
public class LegacyParticleFactory implements ParticleFactory {

    private Value<Float> lifetime = Value.value(1f);
    private Value<Float> impulse = Value.value(1f);
    private Value<ColorRGBA> color = Value.value(ColorRGBA.White);
    private Value<Vector3f> angularVelocity = Value.value(Vector3f.ZERO);
    
    @Override
    public ParticleData createParticle(Transform transform, EmissionVolume volume) {
        ParticleData p = new ParticleData();
        p.setPosition(volume.getNextPosition());
        p.linearVelocity.set(p.getPosition()).normalizeLocal().multLocal(impulse.get());
        p.getPosition().addLocal(transform.getTranslation());
        p.angularVelocity.set(angularVelocity.get());
        p.setLife(lifetime.get());
        p.color.set(color.get());
        return p;
    }

    public void setLifetime(Value<Float> lifetime) {
        this.lifetime = lifetime;
    }
    public void setImpulse(Value<Float> impulse) {
        this.impulse = impulse;
    }
    public void setColor(Value<ColorRGBA> color) {
        this.color = color;
    }
    public void setAngularVelocity(Value<Vector3f> angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public Value<Float> getLifetime() {
        return lifetime;
    }
    public Value<Float> getImpulse() {
        return impulse;
    }
    public Value<ColorRGBA> getColor() {
        return color;
    }
    public Value<Vector3f> getAngularVelocity() {
        return angularVelocity;
    }
    
}
