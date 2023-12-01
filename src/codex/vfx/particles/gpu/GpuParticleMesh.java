/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.gpu;

import codex.vfx.utils.MeshUtils;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.opencl.Buffer;
import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Context;
import com.jme3.opencl.Image;
import com.jme3.opencl.Kernel;
import com.jme3.opencl.MemoryAccess;
import com.jme3.opencl.Program;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;

/**
 *
 * @author codex
 */
public class GpuParticleMesh extends Mesh {
    
    /**
     * Indicates the function of an OpenCL program used to initialize particle data.
     */
    public static final String INIT_KERNEL_FUNCTION = "initParticleData";
    
    /**
     * Indicates the function of an OpenCL program used to update particle data.
     */
    public static final String UPDATE_KERNEL_FUNCTION = "updateParticleData";
    
    /**
     * Name of the position texture parameter of the material.
     */
    public static final String POSITION_TEXTURE_PARAM = "PositionTexture";
    
    /**
     * Name of the data resolution parameter of the material.
     */
    public static final String RESOLUTION_PARAM = "DataResolution";
    
    private final Context context;
    private final CommandQueue queue;
    private final Kernel initKernel, updateKernel;
    private final Kernel.WorkSize work;
    private final int width, height;
    private Image clPosImage, clVelImage;
    private Texture2D glPosTex;
    private boolean openglReady = false, openclReady = false;
    
    public GpuParticleMesh(Context context, CommandQueue queue, Program program, int width, int height) {
        this.context = context;
        this.queue = queue;
        this.width = width;
        this.height = height;
        initKernel = program.createKernel(INIT_KERNEL_FUNCTION).register();
        updateKernel = program.createKernel(UPDATE_KERNEL_FUNCTION).register();
        work = new Kernel.WorkSize(this.width, this.height);
        this.setMode(Mode.Points);
        System.out.println("Creating "+getCapacity()+" GPU Particles");
    }
    
    /**
     * Initialize OpenGL vertex buffers and material parameters.
     * 
     * @param material material belonging to this mesh's geometry.
     */
    public void initOpenGL(Material material) {
        // 1 byte per vertex
        int cap = getCapacity();
        ByteBuffer pb = BufferUtils.createByteBuffer(cap);
        for (int i = 0; i < cap; i++) {
            pb.put((byte)0);
        }
        MeshUtils.initializeVertexBuffer(this, Type.Position, VertexBuffer.Usage.Static, VertexBuffer.Format.UnsignedByte, pb, 1);
        glPosTex = new Texture2D(width, height, Format.RGBA32F);
        material.setTexture(POSITION_TEXTURE_PARAM, glPosTex);
        material.setVector2(RESOLUTION_PARAM, new Vector2f(width, height));
        updateCounts();
        openglReady = true;
    }
    
    /**
     * Initialize bindings for OpenCL and run initialization kernel.
     */
    public void initOpenCL() {
        if (!openglReady) {
            throw new IllegalStateException(
                    "Cannot initialize OpenCL until resources have been passed to the GPU."
                    +" Initialize OpenGL first.");
        }
        Image.ImageFormat format = new Image.ImageFormat(Image.ImageChannelOrder.RGBA, Image.ImageChannelType.FLOAT);
        Image.ImageDescriptor desc = new Image.ImageDescriptor(Image.ImageType.IMAGE_2D, width, height, 0, 0);
        clPosImage = context.bindImage(glPosTex, MemoryAccess.WRITE_ONLY);
        clVelImage = context.createImage(MemoryAccess.READ_WRITE, format, desc);
        clPosImage.acquireImageForSharingNoEvent(queue);
        initKernel.Run1NoEvent(queue, work, clPosImage, clVelImage, 1f, 0.01f);
        clPosImage.releaseImageForSharingNoEvent(queue);
        openclReady = true;
    }
    
    /**
     * Run OpenCL update kernel.
     * 
     * @param tpf time per frame
     */
    public void updateMesh(float tpf) {        
        if (!openglReady || !openclReady) {
            // don't update if OpenGL or OpenCL have not been initialized
            return;
        }
        clPosImage.acquireImageForSharingNoEvent(queue);
        updateKernel.Run1NoEvent(queue, work, clPosImage, clVelImage, FastMath.rand.nextFloat(), tpf);
        clPosImage.releaseImageForSharingNoEvent(queue);
    }
    
    /**
     * Returns the maximum number of particles this mesh supports.
     * 
     * @return 
     */
    public final int getCapacity() {
        return width * height;
    }
    
}
