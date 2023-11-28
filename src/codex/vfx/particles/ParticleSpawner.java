/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.particles.ParticleData;
import com.jme3.math.Vector3f;

/**
 *
 * @author codex
 */
public interface ParticleSpawner {
    
    public ParticleData createParticle(Vector3f position, ParticleGroup group);
    
}
