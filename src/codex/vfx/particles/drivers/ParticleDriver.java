/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers;

import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * 
 * 
 * @author codex
 * @param <T> type of particle
 */
public interface ParticleDriver <T extends ParticleData> {
    
    /**
     * Performs an update on the particle group this is driving.
     * 
     * @param group
     * @param tpf 
     */
    public void updateGroup(ParticleGroup<T> group, float tpf);
    
    /**
     * Performs an update on a particle belonging to the particle group being driven.
     * 
     * @param particle
     * @param tpf 
     */
    public void updateParticle(T particle, float tpf);
    
    /**
     * Updates position based on velocity.
     */
    public static final ParticleDriver Position = new ParticleDriver() {
        @Override
        public void updateGroup(ParticleGroup group, float tpf) {}
        @Override
        public void updateParticle(ParticleData particle, float tpf) {
            particle.getPosition().addLocal(particle.velocity.mult(tpf));
        }
    };
    
    public static final ParticleDriver Rotation = new ParticleDriver() {
        @Override
        public void updateGroup(ParticleGroup group, float tpf) {}
        @Override
        public void updateParticle(ParticleData particle, float tpf) {
            particle.getRotation().multLocal(new Quaternion().fromAngles(particle.angularVelocity.x*tpf, particle.angularVelocity.y*tpf, particle.angularVelocity.z*tpf));
        }
    };
    
    public static ParticleDriver force(Vector3f force) {
        return new ConstantForce(force);
    }
    
    public static class ConstantForce implements ParticleDriver {
        
        private final Vector3f force = new Vector3f();

        public ConstantForce(Vector3f force) {
            this.force.set(force);
        }

        @Override
        public void updateGroup(ParticleGroup group, float tpf) {}
        @Override
        public void updateParticle(ParticleData particle, float tpf) {
            particle.velocity.addLocal(force.mult(tpf));
        }
        
    }
    
}
