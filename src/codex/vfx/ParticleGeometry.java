/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx;

import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import java.util.Iterator;

/**
 * Responsible for rendering a particle mesh.
 * 
 * @author codex
 */
public class ParticleGeometry extends Geometry {
    
    private ParticleGroup<ParticleData> group;
    private ParticleMesh pMesh;

    public ParticleGeometry(ParticleGroup group, Mesh mesh) {
        super();
        this.group = group;
        setMesh(mesh);
        initGeomSettings();
    }
    
    private void initGeomSettings() {
        setQueueBucket(RenderQueue.Bucket.Transparent);
        setIgnoreTransform(true);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        pMesh.updateMesh(group);
        for (Iterator<ParticleData> it = group.iterator(); it.hasNext();) {
            ParticleData p = it.next();
            p.incrementLife(tpf);
            if (!p.isAlive()) {
                it.remove();
            }
        }
    }
    @Override
    public final void setMesh(Mesh mesh) {
        if (!(mesh instanceof ParticleMesh)) {
            throw new IllegalArgumentException("Requires ParticleMesh.");
        }
        super.setMesh(mesh);
        pMesh = (ParticleMesh)mesh;
        if (pMesh != null) {
            pMesh.initBuffers(group);
        }
    }
    @Override
    public ParticleMesh getMesh() {
        return pMesh;
    }
    
}
