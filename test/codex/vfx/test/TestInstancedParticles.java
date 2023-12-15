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
import codex.vfx.particles.tweens.LinearInterpolator;
import codex.vfx.test.util.DemoApplication;
import codex.vfx.utils.Range;
import codex.vfx.utils.VfxMath;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Easing;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedReference;
import jme3utilities.math.noise.Generator;

/**
 *
 * @author codex
 */
public class TestInstancedParticles extends DemoApplication implements ParticleDriver {
    
    private float time = 0f;
    private ParticleGroup group;
    private Generator gen = new Generator();
    private VersionedReference<Double> speedRef;
    
    public static void main(String[] args) {
        new TestInstancedParticles().start();
    }
    
    @Override
    public void demoInitApp() {
        
        //setupVideoCapture("/Videos/instanced-monkeys.avi");
        
        group = new ParticleGroup(100);
        group.setOverflowProtocol(OverflowProtocol.CULL_OLD);
        group.setDecayRate(1);
        group.addDriver(this);
        group.addDriver(ParticleDriver.ValueUpdate);
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
        //mesh = new Sphere(12, 12, 1);
        
        material.setBoolean("UseInstancing", true);
        material.setBoolean("UseVertexColor", true);
        
        InstancedParticleGeometry geometry = new InstancedParticleGeometry(group, mesh);
        geometry.setShadowMode(RenderQueue.ShadowMode.Off);
        geometry.setCullHint(Spatial.CullHint.Never);
        geometry.setMaterial(material);
        rootNode.attachChild(geometry);
        
        //enableLightProbes(false);
        //enableShadows(false);
        
        Container sliders = new Container();
        sliders.setLayout(new SpringGridLayout());
        sliders.setLocalTranslation(10, windowSize.y-10, 0);
        Slider speed = new Slider();
        speed.setAlpha(1f);
        speed.getModel().setMinimum(0);
        speed.getModel().setMaximum(2);
        speed.getModel().setValue(1);
        speed.getRangePanel().setPreferredSize(new Vector3f(200, 0, 0));
        speed.setInsets(new Insets3f(3, 50, 3, 5));
        speedRef = speed.getModel().createReference();
        Label speedLabel = new Label("Update Speed");
        sliders.addChild(speedLabel, 0, 0).setInsets(new Insets3f(3, 10, 3, 20));
        sliders.addChild(speed, 0, 1);
        guiNode.attachChild(sliders);
    
    }
    @Override
    public void demoUpdate(float tpf) {
        group.update(tpf);
        if (speedRef.update()) {
            group.setUpdateSpeed(speedRef.get().floatValue());
        }
    }
    @Override
    public void updateGroup(ParticleGroup group, float tpf) {
        time -= tpf;
        if (time < 0) {
            ParticleData p = new ParticleData();
            p.setPosition(new Vector3f(0f, 2f, 0f));
            gen.nextUnitVector3f(p.linearVelocity).multLocal(7f);
            gen.nextUnitVector3f(p.angularVelocity).multLocal(10f);
            p.setLife(3f);
            p.setScale(.4f);
            p.color = new Range(
                ColorRGBA.Black,
                ColorRGBA.randomColor(),
                LinearInterpolator.Color,
                Easing.linear
            );
            p.size.set(VfxMath.gen.nextFloat(.8f, 1.2f));
            group.add(p);
            time = .05f;
        }
    }
    @Override
    public void updateParticle(ParticleData particle, float tpf) {
        particle.linearVelocity.multLocal(1f-0.9f*tpf);
        particle.angularVelocity.multLocal(1f-0.9f*tpf);
    }
    @Override
    public void particleAdded(ParticleData particle) {}
    
}
