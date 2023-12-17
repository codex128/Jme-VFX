/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.utils.VfxUtils;
import com.jme3.math.Plane;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;

/**
 *
 * @author codex
 */
public class EmissionCircle implements EmissionVolume {
    
    private final Plane plane = new Plane();
    private final float radius;

    public EmissionCircle(float radius) {
        this.radius = radius;
    }
    
    @Override
    public Vector3f getNextPosition(Transform transform) {
        plane.setNormal(transform.getRotation().mult(Vector3f.UNIT_Z));
        return plane.getClosestPoint(VfxUtils.gen.nextUnitVector3f().multLocal(radius)).addLocal(transform.getTranslation());
    }
    
}
