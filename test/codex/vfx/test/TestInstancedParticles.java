/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.boost.scene.SceneGraphIterator;
import codex.vfx.particles.InstancedParticleGeometry;
import codex.vfx.particles.OverflowProtocol;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.test.util.DemoApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.mikktspace.MikktspaceTangentGenerator;
import jme3utilities.math.noise.Generator;

/**
 *
 * @author codex
 */
public class TestInstancedParticles extends DemoApplication implements ParticleDriver {
    
    private float time = 0f;
    private ParticleGroup group;
    private Generator gen = new Generator();
    
    public static void main(String[] args) {
        new TestInstancedParticles().start();
    }
    
    @Override
    public void demoInitApp() {
        
        //setupVideoCapture("/Videos/instanced-particle-shading.avi");
        
        group = new ParticleGroup(100);
        group.setOverflowProtocol(OverflowProtocol.CULL_OLD);
        group.setDecayRate(1);
        group.addDriver(this);
        group.addDriver(ParticleDriver.Position);
        group.addDriver(ParticleDriver.Rotation);
        group.addDriver(ParticleDriver.force(new Vector3f(0f, -5f, 0f)));
        
        Spatial model = assetManager.loadModel("Models/monkey.j3o");
        Mesh mesh = null;
        Material material = null;
        for (Spatial s : new SceneGraphIterator(model)) {
            if (s instanceof Geometry) {
                Geometry g = (Geometry)s;
                mesh = g.getMesh();
                material = g.getMaterial();
                break;
            }
        }
        if (mesh == null || material == null) {
            throw new NullPointerException("Unable to locate geometry.");
        }
        
        //mesh = new Box(1f, 1f, 1f);
        mesh = new Sphere(12, 12, 1);
        
        material.setBoolean("UseInstancing", true);
        
        InstancedParticleGeometry geometry = new InstancedParticleGeometry(group, mesh);
        MikktspaceTangentGenerator.generate(geometry);
        geometry.setShadowMode(RenderQueue.ShadowMode.Off);
        geometry.setCullHint(Spatial.CullHint.Never);
        geometry.setMaterial(material);
        rootNode.attachChild(geometry);
        
        enableLightProbes(false);
        //enableShadows(false);
    
    }
    @Override
    public void demoUpdate(float tpf) {
        group.update(tpf);
    }
    @Override
    public void updateGroup(ParticleGroup group, float tpf) {
        time -= tpf;
        if (time < 0) {
            ParticleData p = new ParticleData();
            p.setPosition(new Vector3f(0f, 2f, 0f));
            gen.nextUnitVector3f(p.velocity).multLocal(5f);
            gen.nextUnitVector3f(p.angularVelocity).multLocal(10f);
            p.setLife(10f);
            p.setScale(.4f);
            group.add(p);
            time = .05f;
        }
    }
    @Override
    public void updateParticle(ParticleData particle, float tpf) {
        //particle.getRotation().multLocal(new Quaternion().fromAngleAxis(FastMath.PI*tpf*0.3f, Vector3f.UNIT_XYZ));
    }
    
}
