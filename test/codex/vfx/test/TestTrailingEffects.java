/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.vfx.test.util.DemoApplication;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.ParticleSpawner;
import codex.vfx.particles.TrailingGeometry;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author codex
 */
public class TestTrailingEffects extends DemoApplication implements ParticleSpawner {
    
    private ParticleGroup<ParticleData> particles;
    private TrailingGeometry geometry;
    private Vector3f spawn = new Vector3f(0f, 2f, 0f);
    
    public static void main(String[] args) {
        new TestTrailingEffects().start();
    }
    
    @Override
    @SuppressWarnings("Convert2Lambda")
    public void demoInitApp() {
        
        particles = new ParticleGroup<>(50);
        particles.setOverflowHint(ParticleGroup.OverflowHint.CullOld);
        
        geometry = new TrailingGeometry(particles, new ParticleSpawner() {
            @Override
            public ParticleData createParticle(Vector3f position, ParticleGroup group) {
                ParticleData p = new ParticleData(1f);
                p.setPosition(position);
                p.setScale(.1f);
                return p;
            }
        });
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
        mat.setFloat("Speed", 1f);
        mat.setFloat("TextureScale", 2f);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        geometry.setMaterial(mat);
        
        Spatial person = assetManager.loadModel("Demo/YBot.j3o");
        person.setLocalScale(0.01f);
        person.setLocalTranslation(0f, 0f, 0f);
        rootNode.attachChild(person);
        
        AnimComposer anim = ((Node)person).getChild("Armature").getControl(AnimComposer.class);
        SkinningControl skin = anim.getSpatial().getControl(SkinningControl.class);
        anim.setCurrentAction("cold-pistol-kill");
        
        skin.getAttachmentsNode("mixamorig:LeftHand").attachChild(geometry);
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bloom);
        //viewPort.addProcessor(fpp);
        
    }
    @Override
    public void demoUpdate(float tpf) {
        //geometry.move(0f, 0f, 1f*tpf);
    }
    @Override
    public ParticleData createParticle(Vector3f position, ParticleGroup group) {
        ParticleData p = new ParticleData(1f);
        p.setPosition(position);
        p.setScale(.1f);
        return p;
    }
    
}
