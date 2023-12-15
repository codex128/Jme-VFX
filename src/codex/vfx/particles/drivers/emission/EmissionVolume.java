/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

/**
 * Generates positions to spawn particles at.
 * <p>
 * Does <em>not</em> generate velocities, colors, etc. That is the
 * responsibility of whatever spawned the particles to manage.
 * 
 * @author codex
 */
public interface EmissionVolume {
    
    /**
     * Returns the next position a particle should be spawned at.
     * <p>
     * Do not modify the returned object. Implementations may want to
     * return constants in order to not create many temporary vector instances.
     * 
     * @return 
     */
    public Vector3f getNextPosition();
    
    /**
     * Generates a debug mesh matching roughly the volume in which particles are spawned.
     * 
     * @return debug mesh, or null if this feature is not supported
     */
    public default Mesh createDebugMesh() {
        return null;
    }
    
}
