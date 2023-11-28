/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.boost.ColorHSBA;
import codex.boost.Timer;
import codex.boost.TimerListener;
import codex.vfx.mesh.MeshPrototype;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGeometry;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.test.util.DemoApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author codex
 */
public class TestBasicParticles extends DemoApplication implements TimerListener {
    
    private ParticleGroup<ParticleData> group;
    private ParticleGeometry geometry;
    private Timer timer = new Timer(.1f);
    private final float gravity = 5f;
    
    public static void main(String[] args) {
        new TestBasicParticles().start();
    }
    
    @Override
    public void demoInitApp() {
    
        group = new ParticleGroup(50);
        group.setOverflowHint(ParticleGroup.OverflowHint.CullOld);
        
        geometry = new ParticleGeometry(group, MeshPrototype.QUAD);
        geometry.setCullHint(Spatial.CullHint.Never);
        //geometry.setQueueBucket(RenderQueue.Bucket.Gui);
        //geometry.setLocalTranslation(0f, 2f, 0f);
        Material mat = new Material(assetManager, "MatDefs/particles.j3md");
        //mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        //mat.getAdditionalRenderState().setWireframe(true);
        geometry.setMaterial(mat);
        rootNode.attachChild(geometry);
        
        timer.setCycleMode(Timer.CycleMode.INFINITE);
        timer.addListener(this);
        timer.start();
    
    }
    @Override
    public void demoUpdate(float tpf) {
        timer.update(tpf);
        group.updateMembers(tpf);
        for (ParticleData p : group) {
            p.velocity.y -= gravity*tpf;
            p.angle += p.rotationSpeed*tpf;
        }
    }
    @Override
    public void onTimerFinish(Timer timer) {
        for (int i = 0; i < 5; i++) {
            ParticleData p = new ParticleData(2f);
            p.setPosition(new Vector3f(0f, 2f, 0f));
            p.setColor(new ColorHSBA(FastMath.nextRandomFloat(), 1f, .5f, 1f).toRGBA());
            p.setVelocity(nextRandomVector().multLocal(2f));
            p.setScale(.1f);
            p.rotationSpeed = FastMath.rand.nextFloat(-20f, 20f);
            group.add(p);
        }
    }
    
    private Vector3f nextRandomVector() {
        return new Vector3f(
            FastMath.rand.nextFloat(-1, 1),
            FastMath.rand.nextFloat(-1, 1),
            FastMath.rand.nextFloat(-1, 1)
        ).normalizeLocal();
    }
    
}
