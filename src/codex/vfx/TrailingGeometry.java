/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

/**
 *
 * @author codex
 */
public class TrailingGeometry extends Geometry {
    
    private final ParticleGroup group;
    private final ParticleSpawner spawner;
    private final TrailingMesh tMesh;
    private float spawnRate = 0.03f, time = 0f;

    public TrailingGeometry(ParticleGroup group, ParticleSpawner spawner) {
        super();
        this.group = group;
        this.spawner = spawner;
        mesh = tMesh = new TrailingMesh();
        tMesh.initBuffers(this.group);
        setIgnoreTransform(true);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        tMesh.updateMesh(group);
        if ((time += tpf) > spawnRate) {
            group.add(spawner.createParticle(getWorldTranslation(), group));
            time = 0;
        }
    }    
    @Override
    public void setMesh(Mesh mesh) {}
    @Override
    public TrailingMesh getMesh() {
        return tMesh;
    }
    
}