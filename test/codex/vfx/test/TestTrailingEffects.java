/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.vfx.test.util.DemoApplication;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.geometry.TrailingGeometry;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import codex.vfx.particles.OverflowStrategy;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.particles.drivers.emission.EmissionPoint;
import codex.vfx.particles.drivers.emission.Emitter;
import codex.vfx.particles.drivers.emission.ParticleFactory;
import codex.vfx.particles.tweens.Value;

/**
 * 
 * @author codex
 */
public class TestTrailingEffects extends DemoApplication {
    
    private ParticleGroup<ParticleData> particles;
    private TrailingGeometry geometry;
    private Vector3f spawn = new Vector3f(0f, 2f, 0f);
    
    public static void main(String[] args) {
        new TestTrailingEffects().start();
    }
    
    @Override
    public void demoInitApp() {
        
        particles = new ParticleGroup<ParticleData>(50);
        particles.setOverflowStrategy(OverflowStrategy.CullOld);
        particles.setVolume(new EmissionPoint());
        Emitter e = Emitter.create();
        e.setEmissionRate(Value.constant(0.01f));
        particles.addDriver(e);
        particles.addDriver(ParticleDriver.TransformToVolume);
        particles.addDriver(new ParticleFactory<ParticleData>() {
            @Override
            public void particleAdded(ParticleGroup group, ParticleData p) {
                p.size.set(.1f);
            }
        });
        rootNode.attachChild(particles);
        
        geometry = new TrailingGeometry(particles);
        geometry.setFaceCamera(true);
        geometry.setLocalTranslation(0f, 2f, 0f);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        geometry.setCullHint(Spatial.CullHint.Never);
        Material mat = new Material(assetManager, "MatDefs/trail.j3md");
        TextureKey texKey = new TextureKey("Textures/wispy-trail.png");
        texKey.setTextureTypeHint(Texture.Type.TwoDimensional);
        texKey.setGenerateMips(false);
        Texture tex = assetManager.loadTexture(texKey);
        mat.setTexture("Texture", tex);
        mat.setFloat("Speed", 3.1f);
        mat.setFloat("TextureScale", 3f);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        geometry.setMaterial(mat);
        particles.attachChild(geometry);
        
        setupCharacter();
        anim.setCurrentAction("cold-pistol-kill");
        
        skin.getAttachmentsNode("mixamorig:LeftHandMiddle1").attachChild(particles);
        
        setupBloom();
        
    }
    @Override
    public void demoUpdate(float tpf) {}
    
}
