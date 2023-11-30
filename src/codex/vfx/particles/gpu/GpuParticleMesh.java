/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.gpu;

import codex.vfx.utils.MeshUtils;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.opencl.Buffer;
import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Context;
import com.jme3.opencl.Event;
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
     * Name of the position texture param of the material.
     */
    public static final String POSITION_TEXTURE_PARAM = "PositionTexture";
    
    private final Context context;
    private final CommandQueue queue;
    private final Kernel initKernel, updateKernel;
    private final Kernel.WorkSize work;
    private final int capacity;
    private Buffer clPosBuffer, clVelBuffer;
    private Image clPosImage, clVelImage;
    private Texture2D glPosTex;
    private Event runEvent;
    private int width = 128, height = 128;
    private boolean bindingsConfig = false;
    
    public GpuParticleMesh(Context context, CommandQueue queue, Program program, int capacity) {
        this.context = context;
        this.queue = queue;
        this.capacity = capacity;
        initKernel = program.createKernel(INIT_KERNEL_FUNCTION).register();
        updateKernel = program.createKernel(UPDATE_KERNEL_FUNCTION).register();
        work = new Kernel.WorkSize(this.capacity);
        this.setMode(Mode.Points);
    }
    
    /**
     * Initialize OpenGL vertex buffers and material parameters.
     * 
     * @param material material belonging to this mesh's geometry.
     */
    public void initOpenGL(Material material) {
        // 1 byte per vertex
        ByteBuffer pb = BufferUtils.createByteBuffer(capacity);
        MeshUtils.initializeVertexBuffer(this, Type.Position, VertexBuffer.Usage.Static, VertexBuffer.Format.UnsignedByte, pb, 1);
        glPosTex = new Texture2D(width, height, Format.Luminance32F);
        material.setTexture(POSITION_TEXTURE_PARAM, glPosTex);
        updateCounts();
    }
    
    /**
     * Initialize bindings for OpenCL and run initialization kernel.
     */
    public void initOpenCL() {
        //clPosBuffer = context.bindVertexBuffer(getBuffer(Type.Position), MemoryAccess.READ_WRITE);
        //Buffer clIndexBuffer = context.bindVertexBuffer(getBuffer(Type.Index), MemoryAccess.WRITE_ONLY);
        //clVelBuffer = context.createBuffer(capacity * 3);
        //clPosBuffer.acquireBufferForSharingNoEvent(queue);
        //initKernel.Run1NoEvent(queue, work, clPosBuffer, /*clIndexBuffer,*/ clVelBuffer, 1f, 0.01f);
        //clPosBuffer.releaseBufferForSharingNoEvent(queue);
        Image.ImageFormat format = new Image.ImageFormat(Image.ImageChannelOrder.RGB, Image.ImageChannelType.FLOAT);
        Image.ImageDescriptor desc = new Image.ImageDescriptor(Image.ImageType.IMAGE_2D, width, height, 0, 0);
        clPosImage = context.bindImage(glPosTex, MemoryAccess.READ_WRITE);
        clVelImage = context.createImage(MemoryAccess.READ_WRITE, format, desc);
        clPosImage.acquireImageForSharingNoEvent(queue);
        initKernel.Run1NoEvent(queue, work, clPosImage, clVelImage, 1f, 0.01f);
        clPosImage.releaseImageForSharingNoEvent(queue);
        bindingsConfig = true;
    }
    
    /**
     * Run OpenCL update kernel.
     * 
     * @param tpf time per frame
     */
    public void updateMesh(float tpf) {        
        if (!bindingsConfig) {
            // don't update if OpenCL has not been initialized
            return;
        }        
        if (runEvent == null) {
            clPosBuffer.acquireBufferForSharingNoEvent(queue);
            runEvent = updateKernel.Run1(queue, work, clPosBuffer, clVelBuffer, FastMath.rand.nextFloat(), tpf);
        }        
        if (runEvent != null && runEvent.isCompleted()) {
            clPosBuffer.releaseBufferForSharingNoEvent(queue);
            runEvent = null;
        }
    }
    
}
