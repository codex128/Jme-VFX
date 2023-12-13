/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test.util;

import codex.boost.scene.SceneGraphIterator;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.gltf.GltfModelKey;
import java.io.File;

/**
 *
 * @author codex
 */
public abstract class DemoApplication extends SimpleApplication {
    
    protected Spatial character;
    protected AnimComposer anim;
    protected SkinningControl skin;
    protected Vector2f windowSize;
    protected FilterPostProcessor fpp;
    protected BloomFilter bloom;
    protected boolean enableLightProbes = true;
    protected boolean enableShadows = true;
    
    public static void main(String[] args) {
        new RawDemoApp().start();
    }
    
    @Override
    public void simpleInitApp() {
        
        flyCam.setMoveSpeed(7);
        setViewDistance(5);
        cam.lookAtDirection(Vector3f.UNIT_XYZ.negate(), Vector3f.UNIT_Y);
        
        windowSize = new Vector2f();
        
        fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);
        
        stateManager.attachAll(
            new EnvironmentCamera(),
            new DemoLightingState(this)
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
    
    protected void setupCharacter() {
        character = assetManager.loadModel("Demo/YBot.j3o");
        character.setCullHint(Spatial.CullHint.Never);
        character.setLocalScale(0.01f);
        character.setLocalTranslation(0f, 0f, 0f);
        for (Spatial s : new SceneGraphIterator(character)) {
            if (s instanceof Geometry) {
                Material m = ((Geometry)s).getMaterial();
                m.getAdditionalRenderState().setDepthWrite(true);
                break;
            }
        }
        anim = ((Node)character).getChild("Armature").getControl(AnimComposer.class);
        skin = anim.getSpatial().getControl(SkinningControl.class);
        anim.setCurrentAction("idle");
        rootNode.attachChild(character);
    }
    protected void setupBloom() {
        bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bloom);
    }
    protected void setupVideoCapture(String save) {
        VideoRecorderAppState video = new VideoRecorderAppState();
        video.setFile(new File(System.getProperty("user.home")+save));
        stateManager.attach(video);
    }
    protected void setViewDistance(float d) {
        cam.setLocation(Vector3f.UNIT_XYZ.normalize().multLocal(d));
    }
    protected void enableLightProbes(boolean enable) {
        enableLightProbes = enable;
    }
    protected void enableShadows(boolean enable) {
        enableShadows = enable;
    }
    
    private static class RawDemoApp extends DemoApplication {
        @Override
        public void demoInitApp() {}
        @Override
        public void demoUpdate(float tpf) {}
    }
    
}
