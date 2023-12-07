/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.vfx.opencl.BindImage;
import codex.vfx.opencl.CLDrivenGeometry;
import codex.vfx.opencl.PingPongImages;
import codex.vfx.test.util.DemoApplication;
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
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.system.AppSettings;
import com.jme3.texture.Image.Format;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

/**
 *
 * @author codex
 */
public class TestGpuParticles extends DemoApplication {
    
    // Number of particles = this number squared.
    private final int particleMapSize = 1024;
    
    // Use images to store particle data as opposed to vertex buffers.
    // WARNING: If using buffers, exceeding 70,000 particles may cause
    //          the application to freeze.
    private final boolean useImageParticles = true;
    
    // OpenCL
    private Context clContext;
    private CommandQueue clQueue;
    
    public static void main(String[] args) {
        TestGpuParticles app = new TestGpuParticles();
        AppSettings settings = new AppSettings(true);
        settings.setVSync(true);
        settings.setOpenCLSupport(true);
        settings.setResolution(1024, 768);
        settings.setRenderer(AppSettings.LWJGL_OPENGL45);
        app.setSettings(settings);
        app.enableLightProbes(false);
        app.enableShadows(false);
        app.start();
    }
    
    @Override
    public void demoInitApp() {
        
        // For developing GPU particles, always leave the cursor unlocked,
        // so you can quit the application if it freezes.
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        
        setViewDistance(30);
        
        clContext = context.getOpenCLContext();
        clQueue = clContext.createQueue().register();
        
        System.out.println("GpuParticleTest: creating "
                +(particleMapSize*particleMapSize)+" particles using "
                +(useImageParticles ? "images" : "vertex buffers"));
        
        if (useImageParticles) {
            // Create GPU particles with images to store data
            Program program = clContext.createProgramFromSourceFiles(
                    assetManager, "Shaders/GpuParticleCompute.cl");
            program.build();
            program.register();
            GpuImageParticleGeometry geometry = new GpuImageParticleGeometry(clContext, clQueue, particleMapSize, particleMapSize);
            geometry.setKernels(program, "initParticleData", "updateParticleData");
            Material mat = new Material(assetManager, "MatDefs/GpuParticles.j3md");
            geometry.setMaterial(mat);
            geometry.setCullHint(Spatial.CullHint.Never);
            geometry.initOpenGL();
            rootNode.attachChild(geometry);
        } else {
            // Create GPU particles with buffers to store data
            Program program = clContext.createProgramFromSourceFiles(
                    assetManager, "Shaders/GpuParticleComputeBuffer.cl");
            program.build();
            program.register();
            GpuBufferParticleGeometry geometry = new GpuBufferParticleGeometry(clContext, clQueue, particleMapSize * particleMapSize);
            geometry.setKernels(program, "initParticleData", "updateParticleData");
            Material mat = new Material(assetManager, "MatDefs/GpuBufferParticles.j3md");
            geometry.setMaterial(mat);
            geometry.setCullHint(Spatial.CullHint.Never);
            geometry.initOpenGL();
            rootNode.attachChild(geometry);
        }
        
    }
    @Override
    public void demoUpdate(float tpf) {}
    
    /**
     * Implementation of CLDrivenGeometry that uses images to store particle data.
     */
    public class GpuImageParticleGeometry extends CLDrivenGeometry {
        
        private final int width, height;
        private final Kernel.WorkSize work;
        private PingPongImages posImgs;
        private BindImage clrImg;
        private Image dataImg;
        private float time = 0;
        
        public GpuImageParticleGeometry(Context context, CommandQueue queue, int width, int height) {
            super(context, queue);
            this.width = width;
            this.height = height;
            work = new Kernel.WorkSize(width, height);
        }
        
        @Override
        protected void initOpenGLResources() {
            posImgs = new PingPongImages(width, height, Format.RGBA32F);
            clrImg = new BindImage(width, height, Format.RGBA16F);
        }
        @Override
        protected void initOpenCLResources() {
            posImgs.bind(context);
            clrImg.bind(context, MemoryAccess.READ_WRITE);
            dataImg = context.createImage(MemoryAccess.READ_WRITE,
                new Image.ImageFormat(Image.ImageChannelOrder.RGBA, Image.ImageChannelType.FLOAT),
                new Image.ImageDescriptor(Image.ImageType.IMAGE_2D, width, height, 0, 0)
            );
        }
        @Override
        protected void applyInitArguments() {
            initKernel.setArg(0, posImgs.getWriteImage());
            initKernel.setArg(1, dataImg);
            initKernel.setArg(2, clrImg.getImage());
            initKernel.setGlobalWorkSize(work);
        }
        @Override
        protected void applyUpdateArguments(float tpf) {
            posImgs.assignToKernelArgs(updateKernel, 1, 0);
            updateKernel.setArg(2, dataImg);
            updateKernel.setArg(3, (time += tpf));
            updateKernel.setGlobalWorkSize(work);
        }
        @Override
        protected void acquireResources() {
            posImgs.acquireResources(queue);
            clrImg.acquireResources(queue);
        }
        @Override
        protected void releaseResources() {        
            posImgs.releaseResources(queue);
            clrImg.releaseResources(queue);
            posImgs.flipReadWrite();
        }
        @Override
        protected void setupMesh(Mesh mesh) {
            createMinimalPositionBuffer(width * height, true);
            mesh.updateCounts();
            //mesh.updateBound();
        }
        @Override
        protected void setupMaterial(Material material) {
            posImgs.setTexturesToMaterial(material, "PositionMap0", "PositionMap1");
            posImgs.setReadWriteValueToMaterial(material, "ReadWriteValue");
            material.setTexture("ColorMap", clrImg.getTexture());
            material.setVector2("DataResolution", new Vector2f(width, height));
        }
        
    }
    
    /**
     * Implementation of CLDrivenGeometry that uses buffers to store geometry.
     */
    public class GpuBufferParticleGeometry extends CLDrivenGeometry {

        private final int capacity;
        private Buffer posBuf, clrBuf, dataBuf;
        private float time = 0;
        
        public GpuBufferParticleGeometry(Context context, CommandQueue queue, int capacity) {
            super(context, queue);
            this.capacity = capacity;
        }
        
        @Override
        protected void initOpenGLResources() {
            // position buffer
            FloatBuffer pb = BufferUtils.createVector3Buffer(capacity);
            MeshUtils.initializeVertexBuffer(mesh,
                    VertexBuffer.Type.Position,
                    VertexBuffer.Usage.Stream,
                    VertexBuffer.Format.Float, pb, 3);
            // color buffer
            FloatBuffer cb = BufferUtils.createFloatBuffer(capacity * 4);
            MeshUtils.initializeVertexBuffer(mesh,
                    VertexBuffer.Type.Color,
                    VertexBuffer.Usage.Stream,
                    VertexBuffer.Format.Float, cb, 4);
        }
        @Override
        protected void initOpenCLResources() {
            posBuf = context.bindVertexBuffer(
                    mesh.getBuffer(VertexBuffer.Type.Position),
                    MemoryAccess.READ_WRITE);
            clrBuf = context.bindVertexBuffer(
                    mesh.getBuffer(VertexBuffer.Type.Color),
                    MemoryAccess.READ_WRITE);
            // data buffer: 5 components per particle
            dataBuf = context.createBuffer(capacity * 5, MemoryAccess.READ_WRITE);
        }
        @Override
        protected void applyInitArguments() {
            initKernel.getGlobalWorkSize().set(1, capacity, 1, 1);
            initKernel.setArg(0, posBuf);
            initKernel.setArg(1, clrBuf);
            initKernel.setArg(2, dataBuf);
        }
        @Override
        protected void applyUpdateArguments(float tpf) {
            updateKernel.getGlobalWorkSize().set(1, capacity, 1, 1);
            updateKernel.setArg(0, posBuf);
            updateKernel.setArg(1, clrBuf);
            updateKernel.setArg(2, dataBuf);
            updateKernel.setArg(3, (time += tpf));
        }
        @Override
        protected void acquireResources() {
            posBuf.acquireBufferForSharingNoEvent(queue);
            clrBuf.acquireBufferForSharingNoEvent(queue);
        }
        @Override
        protected void releaseResources() {
            posBuf.releaseBufferForSharingNoEvent(queue);
            clrBuf.releaseBufferForSharingNoEvent(queue);
        }
        @Override
        protected void setupMesh(Mesh mesh) {}
        @Override
        protected void setupMaterial(Material material) {}
        
    }
        
}
