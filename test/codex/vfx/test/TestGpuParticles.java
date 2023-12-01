/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.vfx.particles.gpu.GpuParticleGeometry;
import codex.vfx.test.util.DemoApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Context;
import com.jme3.opencl.Program;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author codex
 */
public class TestGpuParticles extends DemoApplication {
    
    private final int particleMapSize = 2048;
    
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
        
        inputManager.setCursorVisible(true);
        setViewDistance(30);
        
        GL11.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
        GL14.glPointParameterf(GL14.GL_POINT_SIZE_MIN, 1.0f);
        GL14.glPointParameterf(GL14.GL_POINT_SIZE_MAX, 10.0f);
        GL14.glPointParameterf(GL14.GL_POINT_FADE_THRESHOLD_SIZE, 0f);
        GL14.glPointParameterf(GL14.GL_POINT_DISTANCE_ATTENUATION, 1.0f);
        
        clContext = context.getOpenCLContext();
        clQueue = clContext.createQueue().register();
        Program program = clContext.createProgramFromSourceFiles(assetManager, "Shaders/GpuParticleCompute.cl");
        program.build();
        program.register();
        
        Material mat = new Material(assetManager, "MatDefs/GpuParticles.j3md");
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        GpuParticleGeometry geometry = new GpuParticleGeometry(clContext, clQueue, program, mat, particleMapSize, particleMapSize);
        geometry.setCullHint(Spatial.CullHint.Never);
        rootNode.attachChild(geometry);
        
        //setupBloom();
        //bloom.setBlurScale(5.0f);
        
    }
    @Override
    public void demoUpdate(float tpf) {
    
        
        
    }
    
}
