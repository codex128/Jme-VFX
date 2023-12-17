/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

/**
 *
 * @author codex
 * @param <T>
 */
public abstract class ParticleGeometry <T extends ParticleData> extends Geometry {
    
    protected ParticleGroup<T> group;
    protected int capacity = -1;
    protected boolean useSpriteSheet = false;
    
    public ParticleGeometry(ParticleGroup<T> group) {
        this.group = group;
        super.setMesh(new Mesh());
        setIgnoreTransform(true);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        if (capacity != group.capacity()) {
            capacity = group.capacity();
            initBuffers();
        }
        updateMesh();
    }
    @Override
    public void setMesh(Mesh mesh) {}
    
    protected abstract void initBuffers();
    protected abstract void updateMesh();
    
    public void setParticleGroup(ParticleGroup<T> group) {
        assert group != null : "Particle group cannot be null";
        this.group = group;
    }
    public void enableSpriteSheet(boolean enable) {
        useSpriteSheet = enable;
    }
    
    public ParticleGroup<T> getGroup() {
        return group;
    }
    public boolean isSpriteSheetEnabled() {
        return useSpriteSheet;
    }
    
}
