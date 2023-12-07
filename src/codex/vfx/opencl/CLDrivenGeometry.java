/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.opencl;

import codex.vfx.utils.MeshUtils;
import com.jme3.material.Material;
import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Context;
import com.jme3.opencl.Event;
import com.jme3.opencl.Kernel;
import com.jme3.opencl.Program;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * 
 * @author codex
 */
public abstract class CLDrivenGeometry extends Geometry {
    
    private static final byte CL_WAIT_FRAMES = 2;
    
    protected final Context context;
    protected final CommandQueue queue;
    protected Kernel initKernel, updateKernel;
    private boolean openglReady = false, openclReady = false;
    private byte initCounter = 0;
    
    public CLDrivenGeometry(Context context, CommandQueue queue) {
        this.context = context;
        this.queue = queue;
        createDefaultMesh();
    }    
    public CLDrivenGeometry(Context context, CommandQueue queue, Kernel initKernel, Kernel updateKernel) {
        this.context = context;
        this.queue = queue;
        this.initKernel = initKernel;
        this.updateKernel = updateKernel;
        createDefaultMesh();
    }
    
    public void initOpenGL() {
        if (material == null) {
            throw new IllegalStateException("Material must be assigned before creating resources.");
        }
        if (mesh == null) {
            throw new IllegalStateException("Mesh must be assigned before creating resources.");
        }
        initOpenGLResources();
        setupMesh(mesh);
        setupMaterial(material);
        openglReady = true;
    }
    private void initOpenCL() {
        if (!openglReady) {
            throw new IllegalStateException("OpenGL resources must be uploaded to the GPU.");
        }
        initOpenCLResources();
        if (initKernel != null) {
            applyInitArguments();
            acquireResources();
            runInitKernel();
            releaseResources();
        }
        openclReady = true;
    }
    private void createDefaultMesh() {
        setMesh(new Mesh());
        mesh.setMode(Mesh.Mode.Points);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        if (!openglReady) {
            return;
        }
        if (openclReady && updateKernel != null) {
            applyUpdateArguments(tpf);
            acquireResources();
            runUpdateKernel(tpf);
            releaseResources();
        }
        if (initCounter == CL_WAIT_FRAMES) {
            initOpenCL();
        }
        if (initCounter <= CL_WAIT_FRAMES) {
            initCounter++;
        }
    }
    @Override
    public final void setMesh(Mesh mesh) {
        if (mesh == null) {
            throw new NullPointerException("Mesh cannot be null.");
        }
        if (this.mesh != mesh) {
            super.setMesh(mesh);
            if (openglReady) {
                setupMesh(this.mesh);
            }
        }
    }
    @Override
    public final void setMaterial(Material material) {
        if (material == null) {
            throw new NullPointerException("Material cannot be null.");
        }
        if (this.material != material) {
            super.setMaterial(material);
            if (openclReady) {
                setupMaterial(this.material);
            }
        }
    }
    
    public void setInitKernel(Kernel kernel) {
        this.initKernel = kernel;
    }
    public void setUpdateKernel(Kernel kernel) {
        this.updateKernel = kernel;
    }
    public void setKernels(Program program, String init, String update) {
        initKernel = program.createKernel(init).register();
        updateKernel = program.createKernel(update).register();
    }
    
    public Kernel getInitKernel() {
        return initKernel;
    }
    public Kernel getUpdateKernel() {
        return updateKernel;
    }
    
    protected Event runInitKernel() {
        initKernel.RunNoEvent(queue);
        return null;
    }
    protected Event runUpdateKernel(float tpf) {
        updateKernel.RunNoEvent(queue);
        return null;
    }
    protected void createMinimalPositionBuffer(int size, boolean fill) {
        ByteBuffer pb = BufferUtils.createByteBuffer(size);
        if (fill) for (int i = 0; i < size; i++) {
            pb.put((byte)0);
        }
        pb.flip();
        MeshUtils.initializeVertexBuffer(mesh, VertexBuffer.Type.Position, VertexBuffer.Usage.Static, VertexBuffer.Format.UnsignedByte, pb, 1);
    }
    
    protected abstract void initOpenGLResources();
    protected abstract void initOpenCLResources();
    protected abstract void applyInitArguments();
    protected abstract void applyUpdateArguments(float tpf);
    protected abstract void acquireResources();
    protected abstract void releaseResources();
    protected abstract void setupMesh(Mesh mesh);
    protected abstract void setupMaterial(Material material);
    
}
