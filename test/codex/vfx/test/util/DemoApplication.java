/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test.util;

import com.jme3.app.SimpleApplication;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;

/**
 *
 * @author codex
 */
public abstract class DemoApplication extends SimpleApplication {
    
    public static void main(String[] args) {
        new RawDemoApp().start();
    }
    
    @Override
    public void simpleInitApp() {
        
        flyCam.setMoveSpeed(7);
        cam.setLocation(new Vector3f(3f, 3f, 3f));
        cam.lookAtDirection(new Vector3f(-1f, -0.5f, -1f), Vector3f.UNIT_Y);
        
        stateManager.attachAll(
            new EnvironmentCamera(),
            new DemoLightingState()
        );
        
        setupScene();
        
        demoInitApp();
        
    }
    @Override
    public void simpleUpdate(float tpf) {
        demoUpdate(tpf);
    }
    
    public abstract void demoInitApp();
    public abstract void demoUpdate(float tpf);
    
    private void setupScene() {
        
        Spatial scene = assetManager.loadModel("Demo/demo-scene.j3o");
        rootNode.attachChild(scene);
        
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
    }
    
    private static class RawDemoApp extends DemoApplication {
        @Override
        public void demoInitApp() {}
        @Override
        public void demoUpdate(float tpf) {}
    }
    
}
