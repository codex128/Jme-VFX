/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.particles.ParticleData;
import com.jme3.math.Transform;

/**
 * Creates particles.
 * 
 * @author codex
 * @param <T> type of particle data
 */
public interface ParticleFactory <T extends ParticleData> {
    
    public T createParticle(Transform transform, EmissionVolume volume);
    
}
