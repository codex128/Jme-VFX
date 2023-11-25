/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import com.jme3.app.SimpleApplication;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 *
 * @author codex
 */
public abstract class DemoApplication extends SimpleApplication {
    
    public static void main(String[] args) {
        new DemoApplication() {
            @Override
            public void demoInitApp() {}
        }.start();
    }
    
    @Override
    public void simpleInitApp() {
        
        flyCam.setMoveSpeed(7);
        cam.setLocation(new Vector3f(3f, 3f, 3f));
        cam.lookAtDirection(new Vector3f(-1f, -0.5f, -1f), Vector3f.UNIT_Y);
        
        stateManager.attachAll(
            new EnvironmentCamera(),
            new GlobalIlluminationState()
        );
        
        setupScene();
        
        demoInitApp();
        
    }
    
    public abstract void demoInitApp();
    
    private void setupScene() {
        
        Spatial scene = assetManager.loadModel("Demo/demo-scene.j3o");
        rootNode.attachChild(scene);
        
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
    }
    
}
