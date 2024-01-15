/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.geometry;

import codex.vfx.particles.Particle;
import codex.vfx.particles.ParticleGroup;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

/**
 *
 * @author codex
 * @param <T>
 */
public abstract class ParticleGeometry <T extends Particle> extends Geometry {
    
    public static final BoundingSphere INFINITE_BOUND = new BoundingSphere(Float.POSITIVE_INFINITY, Vector3f.ZERO);
    
    protected ParticleGroup<T> group;
    protected int capacity = -1;
    protected boolean useSpriteSheet = false;
    private boolean forceMeshUpdate = false;
    
    public ParticleGeometry(ParticleGroup<T> group) {
        super();
        this.group = group;
        setMesh(new Mesh());
        setIgnoreTransform(true);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        if (group.capacity() > 0) {
            if (checkBuffers()) {
                capacity = group.capacity();
                initBuffers();
            }
            if (forceMeshUpdate || group.getWorldPlayState()) {
                updateMesh();
            }
        }
    }
    @Override
    public void updateWorldBound() {
        super.updateWorldBound();
        worldBound = INFINITE_BOUND;
    }
    
    protected abstract void initBuffers();
    protected abstract void updateMesh();
    protected boolean checkBuffers() {
        return capacity != group.capacity();
    }
    
    /**
     * Sets the particle group used to generate the particle mesh.
     * 
     * @param group 
     */
    public void setParticleGroup(ParticleGroup<T> group) {
        assert group != null : "Rendered particle group cannot be null";
        if (this.group != group) {
            this.group = group;
            // force buffers to reinitialize
            capacity = -1;
        }
    }
    /**
     * Forces this geometry to always update its mesh.
     * <p>
     * If the particle group is paused, the mesh would normally not be
     * updated, since all particles are assumed to be static. If, for some
     * reason, the particles are not static in this state, setting this to
     * true will force this geometry to always update the mesh.
     * <p>
     * default=false
     * 
     * @param forceMeshUpdate true to force this geometry to always update the mesh
     * @see ParticleGroup#play()
     * @see ParticleGroup#pause()
     */
    public void setForceMeshUpdate(boolean forceMeshUpdate) {
        this.forceMeshUpdate = forceMeshUpdate;
    }
    /**
     * Enables writing to a sprite index buffer.
     * <p>
     * This must be enabled to support sprite sheets. Otherwise geometries
     * will not write particle sprite indices to their meshes.
     * <p>
     * default=false
     * 
     * @param enable 
     */
    public void enableSpriteSheet(boolean enable) {
        useSpriteSheet = enable;
    }
    
    public ParticleGroup<T> getParticleGroup() {
        return group;
    }
    public boolean isForceMeshUpdate() {
        return forceMeshUpdate;
    }
    public boolean isSpriteSheetEnabled() {
        return useSpriteSheet;
    } 
    
}
