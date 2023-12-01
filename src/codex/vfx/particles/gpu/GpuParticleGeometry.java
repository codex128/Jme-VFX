/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.gpu;

import codex.vfx.utils.MeshTypeGeometry;
import com.jme3.material.Material;
import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Context;
import com.jme3.opencl.Program;

/**
 *
 * @author codex
 */
public class GpuParticleGeometry extends MeshTypeGeometry<GpuParticleMesh> {
    
    public static final byte BIND_WAIT_FRAMES = 2;
    
    private byte initCounter = 0;
    
    public GpuParticleGeometry(Context context, CommandQueue queue, Program program, Material mat, int width, int height) {
        super(GpuParticleMesh.class);
        setTypeMesh(new GpuParticleMesh(context, queue, program, width, height));
        setIgnoreTransform(true);
        super.setMaterial(mat);
        tMesh.initOpenGL(material);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        if (initCounter == BIND_WAIT_FRAMES) {
            tMesh.initOpenCL();
        }
        if (initCounter <= BIND_WAIT_FRAMES) {
            initCounter++;
        }
        tMesh.updateMesh(tpf);
    }
    @Override
    public void setMaterial(Material material) {}
    
}
