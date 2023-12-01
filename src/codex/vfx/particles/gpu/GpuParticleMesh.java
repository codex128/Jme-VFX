/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.gpu;

import codex.vfx.utils.MeshUtils;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
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
import com.jme3.shader.VarType;
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
    public static final String POSITION_TEXTURES_PARAM = "PositionTexture";
    
    /**
     * Name of the data resolution parameter of the material.
     */
    public static final String RESOLUTION_PARAM = "DataResolution";
    
    /**
     * Name of the texture index parameter of the material.
     */
    public static final String TEXTURE_INDEX = "TextureIndex";
    
    /**
     * Name of the color map parameter of the material.
     */
    public static final String COLOR_MAP_PARAM = "ColorMap";
    
    private final Context context;
    private final CommandQueue queue;
    private final Kernel initKernel, updateKernel;
    private final Kernel.WorkSize work;
    private final int width, height;
    private PingPongImages posImages, velImages;
    private Image clColorImg;
    private Texture2D glColorTex;
    private boolean openglReady = false, openclReady = false;
    private float time = 0f;
    
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
        posImages = new PingPongImages();
        posImages.setTexture(0, new Texture2D(width, height, Format.RGBA32F));
        posImages.setTexture(1, new Texture2D(width, height, Format.RGBA32F));
        glColorTex = new Texture2D(width, height, Format.RGBA32F);
        material.setTexture(POSITION_TEXTURES_PARAM+"0", posImages.getTexture(0));
        material.setTexture(POSITION_TEXTURES_PARAM+"1", posImages.getTexture(1));
        material.setTexture(COLOR_MAP_PARAM, glColorTex);
        material.setVector2(RESOLUTION_PARAM, new Vector2f(width, height));
        updateCounts();
        openglReady = true;
    }
    
    /**
     * Initialize bindings for OpenCL and run initialization kernel.
     * 
     * @param material material belonging to this mesh's geometry
     */
    public void initOpenCL(Material material) {
        if (!openglReady) {
            throw new IllegalStateException(
                    "Cannot initialize OpenCL until resources have been passed to the GPU."
                    +" Initialize OpenGL first.");
        }
        velImages = new PingPongImages();
        Image.ImageFormat format = new Image.ImageFormat(Image.ImageChannelOrder.RGBA, Image.ImageChannelType.FLOAT);
        Image.ImageDescriptor desc = new Image.ImageDescriptor(Image.ImageType.IMAGE_2D, width, height, 0, 0);
        posImages.setImage(0, context.bindImage(posImages.getTexture(0), MemoryAccess.READ_WRITE));
        posImages.setImage(1, context.bindImage(posImages.getTexture(1), MemoryAccess.READ_WRITE));
        velImages.setImage(0, context.createImage(MemoryAccess.READ_WRITE, format, desc));
        velImages.setImage(1, context.createImage(MemoryAccess.READ_WRITE, format, desc));
        clColorImg = context.bindImage(glColorTex, MemoryAccess.WRITE_ONLY);
        posImages.acquireImagesNoEvent(queue);
        clColorImg.acquireImageForSharingNoEvent(queue);
        initKernel.Run1NoEvent(queue, work,
            posImages.getWriteImage(),
            velImages.getWriteImage(),
            clColorImg
        );
        posImages.releaseImagesNoEvent(queue);
        clColorImg.releaseImageForSharingNoEvent(queue);
        material.setInt(TEXTURE_INDEX, posImages.getWriteIndex());
        posImages.flipIndex();
        velImages.flipIndex();
        openclReady = true;
    }
    
    /**
     * Run OpenCL update kernel.
     * 
     * @param material material belonging to this mesh's geometry
     * @param tpf time per frame
     */
    public void updateMesh(Material material, float tpf) {        
        if (!openglReady || !openclReady) {
            // don't update if OpenGL or OpenCL have not been initialized
            return;
        }
        time += tpf;
        posImages.acquireImagesNoEvent(queue);
        updateKernel.Run1(queue, work,
            posImages.getWriteImage(),
            posImages.getReadImage(),
            velImages.getWriteImage(),
            velImages.getReadImage(),
            FastMath.rand.nextFloat(), time, tpf
        );   
        posImages.releaseImagesNoEvent(queue);            
        material.setInt(TEXTURE_INDEX, posImages.getWriteIndex());
        posImages.flipIndex();
        velImages.flipIndex();
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
