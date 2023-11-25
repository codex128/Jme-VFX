/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.vfx.ParticleData;
import codex.vfx.ParticleGeometry;
import codex.vfx.ParticleGroup;
import codex.vfx.ParticleSpawner;
import codex.vfx.TrailingGeometry;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;

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
    public void demoInitApp() {
        
        particles = new ParticleGroup<>(50);
        particles.setOverflowHint(ParticleGroup.OverflowHint.CullOld);
        
        geometry = new TrailingGeometry(particles, this);
        geometry.setLocalTranslation(0f, 2f, 0f);
        geometry.setIgnoreTransform(true);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        geometry.setCullHint(Spatial.CullHint.Never);
        Material mat = new Material(assetManager, "MatDefs/trail.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Textures/wispy-trail.png"));
        mat.setFloat("Speed", 1f);
        //mat.setBoolean("FaceCamera", false);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        //mat.getAdditionalRenderState().setWireframe(true);
        mat.setTransparent(true);
        geometry.setMaterial(mat);
        rootNode.attachChild(geometry);
        
    }
    @Override
    public void simpleUpdate(float tpf) {
        geometry.move(0f, 0f, 1f*tpf);
    }
    @Override
    public ParticleData createParticle(Vector3f position, ParticleGroup group) {
        ParticleData p = new ParticleData(1f);
        p.setPosition(cam.getLocation().add(cam.getDirection().mult(5f)));
        p.setRadius(.2f);
        return p;
    }
    
}
