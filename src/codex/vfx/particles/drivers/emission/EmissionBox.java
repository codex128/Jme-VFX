/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.utils.VfxUtils;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;

/**
 *
 * @author codex
 */
public class EmissionBox implements EmissionVolume {

    private final Vector3f center = new Vector3f();
    private final Vector3f extent = new Vector3f();

    public EmissionBox() {
        this(.5f, .5f, .5f);
    }
    public EmissionBox(float extent) {
        this.extent.set(extent, extent, extent);
    }
    public EmissionBox(Vector3f center) {
        this(center, .5f, .5f, .5f);
    }
    public EmissionBox(float x, float y, float z) {
        this.extent.set(x, y, z);
    }
    public EmissionBox(Vector3f center, float extent) {
        this.center.set(center);
        this.extent.set(extent, extent, extent);
    }
    public EmissionBox(Vector3f center, Vector3f extent) {
        this.center.set(center);
        this.extent.set(extent);
    }
    public EmissionBox(Vector3f center, float x, float y, float z) {
        this.center.set(center);
        this.extent.set(x, y, z);
    }
    
    @Override
    public Vector3f getNextPosition(Transform transform) {
        return VfxUtils.random(center, extent.x, extent.y, extent.z)
                .multLocal(transform.getScale())
                .addLocal(transform.getTranslation());
    }

    public void setCenter(Vector3f center) {
        this.center.set(center);
    }
    public void setExtent(Vector3f extent) {
        this.extent.set(extent);
    }
    public void setExtent(float extent) {
        this.extent.set(extent, extent, extent);
    }
    public void setExtent(float x, float y, float z) {
        this.extent.set(x, y, z);
    }
    
    public Vector3f getCenter() {
        return center;
    }
    public Vector3f getExtent() {
        return extent;
    }
    
}
