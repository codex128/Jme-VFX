/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.particles.ParticleData;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;

/**
 * Generates positions to spawn particles at.
 * <p>
 * Does <em>not</em> generate velocities, colors, etc. That is the
 * responsibility of whatever spawned the particles to manage.
 * 
 * @author codex
 * @param <T>
 */
public interface EmissionVolume <T extends ParticleData> {
    
    /**
     * Returns the next position a particle should be spawned at.
     * <p>
     * Do not modify the returned object. Implementations may want to
     * return constants in order to not create many temporary vector instances.
     * 
     * @param transform
     * @return 
     */
    public Vector3f getNextPosition(Transform transform);
    
}
