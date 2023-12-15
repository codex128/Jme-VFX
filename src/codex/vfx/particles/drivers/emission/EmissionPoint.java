/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import com.jme3.math.Vector3f;

/**
 * Emits particles from a single point.
 * 
 * @author codex
 */
public class EmissionPoint implements EmissionVolume {
    
    @Override
    public Vector3f getNextPosition() {
        return Vector3f.ZERO;
    }
    
}
