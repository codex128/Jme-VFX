/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.vfx.mesh.MeshPrototype;
import codex.vfx.particles.OverflowProtocol;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.TriParticleGeometry;
import codex.vfx.test.util.DemoApplication;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.BaseAction;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;

/**
 * Demo of spawning hit effects for punches and kicks.
 * 
 * @author codex
 */
public class HitEffectDemo extends DemoApplication {
    
    private ParticleGroup<ParticleData> group;
    private TriParticleGeometry geometry;
    
    public static void main(String[] args) {
        new HitEffectDemo().start();
    }
    
    @Override
    public void demoInitApp() {
        
        group = new ParticleGroup(2);
        group.setOverflowProtocol(OverflowProtocol.CULL_OLD);
        
        geometry = new TriParticleGeometry(group, MeshPrototype.QUAD);
        geometry.setLocalTranslation(0, 3, 0);
        geometry.setCullHint(Spatial.CullHint.Never);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        Material mat = new Material(assetManager, "MatDefs/particles.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Demo/impact.png"));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        geometry.setMaterial(mat);
        //rootNode.attachChild(geometry);
        
        setupCharacter();
        anim.addAction("pistol-kill", new BaseAction(Tweens.parallel(
            anim.action("cold-pistol-kill"),
            Tweens.sequence(
                Tweens.delay(1.75f),
                Tweens.callMethod(this, "spawnHitEffect", "mixamorig:LeftHandIndex4")
            )
        )));
        anim.setCurrentAction("pistol-kill");
        
        skin.getAttachmentsNode("mixamorig:LeftHandIndex4").attachChild(geometry);
        
    }
    @Override
    public void demoUpdate(float tpf) {
        group.update(tpf);
        for (ParticleData p : group) {
            p.color.get().a = FastMath.clamp(p.getLifePercent(), 0f, 0f);
        }
    }
    
    public void spawnHitEffect(String jointName) {
        ParticleData p = new ParticleData(1f);
        if (group.add(p)) {
            p.setPosition(geometry.getWorldTranslation());
            p.setScale(.2f);
            p.angle.set(FastMath.rand.nextFloat(FastMath.TWO_PI));
        }
    }
    
}
