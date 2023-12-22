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
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.geometry.TriParticleGeometry;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.test.util.DemoApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import codex.vfx.particles.OverflowStrategy;

/**
 *
 * @author codex
 */
public class TestBasicParticles extends DemoApplication implements TimerListener {
    
    private ParticleGroup<ParticleData> group;
    private TriParticleGeometry geometry;
    private final Timer timer = new Timer(.1f);
    private final int particlesPerEmission = 5;
    private final float gravity = 5f;
    
    public static void main(String[] args) {
        new TestBasicParticles().start();
    }
    
    @Override
    public void demoInitApp() {
    
        group = new ParticleGroup(200);
        group.setOverflowStrategy(OverflowStrategy.CullNew);
        group.addDriver(ParticleDriver.force(new Vector3f(0f, -3f, 0f)));
        group.addDriver(ParticleDriver.Position);
        rootNode.attachChild(group);
        
        geometry = new TriParticleGeometry(group, MeshPrototype.QUAD);
        geometry.setLocalTranslation(0, 3, 0);
        geometry.setCullHint(Spatial.CullHint.Never);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        Material mat = new Material(assetManager, "MatDefs/particles.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        geometry.setMaterial(mat);
        rootNode.attachChild(geometry);
        
        timer.setCycleMode(Timer.CycleMode.INFINITE);
        timer.addListener(this);
        timer.start();
    
    }
    @Override
    public void demoUpdate(float tpf) {
        timer.update(tpf);
    }
    @Override
    public void onTimerFinish(Timer timer) {
        for (int i = 0; i < particlesPerEmission; i++) {
            ParticleData p = new ParticleData(4f);
            p.setPosition(geometry.getWorldTranslation());
            p.color.set(new ColorHSBA(FastMath.nextRandomFloat(), 1f, .5f, 1f).toRGBA());
            p.linearVelocity = nextRandomVector().multLocal(2f);
            p.setScale(FastMath.rand.nextFloat(.05f, .2f));
            p.rotationSpeed.set(FastMath.rand.nextFloat(-20f, 20f));
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
