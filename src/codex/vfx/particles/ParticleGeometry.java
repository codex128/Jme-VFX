/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.mesh.MeshPrototype;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

/**
 *
 * @author codex
 */
public class ParticleGeometry extends Geometry {
    
    private final ParticleGroup<ParticleData> group;
    private final MeshPrototype prototype;
    private ParticleTriMesh pMesh;
    
    public ParticleGeometry(ParticleGroup group, MeshPrototype prototype) {
        this.group = group;
        this.prototype = prototype;
        pMesh = new ParticleTriMesh();
        super.setMesh(pMesh);
        pMesh.initBuffers(group, prototype);
        setIgnoreTransform(true);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        pMesh.updateMesh(group, prototype);
    }
    @Override
    public void setMesh(Mesh mesh) {
        if (!(mesh instanceof ParticleTriMesh)) {
            throw new IllegalArgumentException("Requires ParticleMesh.");
        }
        super.setMesh(mesh);
        pMesh = (ParticleTriMesh)this.mesh;
    }
    @Override
    public ParticleTriMesh getMesh() {
        return pMesh;
    }
    
}
