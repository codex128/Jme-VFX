/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;

/**
 * Emits particles from a single point.
 * 
 * @author codex
 */
public class EmissionPoint implements EmissionVolume {
    
    public static final EmissionPoint Instance = new EmissionPoint();
    
    @Override
    public Vector3f getNextPosition(Transform transform) {
        return transform.getTranslation();
    }
    
}
