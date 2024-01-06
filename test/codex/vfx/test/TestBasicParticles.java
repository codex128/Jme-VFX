/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.boost.ColorHSBA;
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
import codex.vfx.particles.drivers.emission.Emitter;
import codex.vfx.particles.drivers.emission.ParticleFactory;
import codex.vfx.particles.tweens.Value;
import codex.vfx.utils.VfxUtils;

/**
 * Tests basic particles.
 * 
 * @author codex
 */
public class TestBasicParticles extends DemoApplication {
    
    public static void main(String[] args) {
        new TestBasicParticles().start();
    }
    
    @Override
    public void demoInitApp() {
    
        ParticleGroup<ParticleData> group = new ParticleGroup(200);
        group.setLocalTranslation(0, 3, 0);
        group.setOverflowStrategy(OverflowStrategy.CullOld);
        group.addDriver(ParticleDriver.force(new Vector3f(0f, -3f, 0f)));
        group.addDriver(ParticleDriver.Position);
        group.addDriver(ParticleDriver.Angle);
        Emitter e = Emitter.create();
        e.setParticlesPerEmission(Value.constant(5));
        e.setEmissionRate(Value.constant(.1f));
        group.addDriver(e);
        group.addDriver(new ParticleFactory<ParticleData>() {
            @Override
            public void particleAdded(ParticleGroup<ParticleData> group, ParticleData p) {
                p.setLife(4f);
                p.setPosition(group.getVolume().getNextPosition(group.getWorldTransform()));
                p.color.set(new ColorHSBA(FastMath.nextRandomFloat(), 1f, .5f, 1f).toRGBA());
                p.linearVelocity = VfxUtils.gen.nextUnitVector3f().multLocal(VfxUtils.gen.nextFloat(4));
                p.setScale(FastMath.rand.nextFloat(.05f, .2f));
                p.angleSpeed.set(FastMath.rand.nextFloat(-5f, 5f));
            }
        });
        rootNode.attachChild(group);
        
        TriParticleGeometry geometry = new TriParticleGeometry(group, MeshPrototype.QUAD);
        geometry.setCullHint(Spatial.CullHint.Never);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        Material mat = new Material(assetManager, "MatDefs/particles.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        geometry.setMaterial(mat);
        rootNode.attachChild(geometry);
    
    }
    @Override
    public void demoUpdate(float tpf) {}
    
}
