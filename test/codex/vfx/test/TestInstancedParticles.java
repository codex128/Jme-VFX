/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.boost.scene.SceneGraphIterator;
import codex.vfx.particles.geometry.InstancedParticleGeometry;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.test.util.DemoApplication;
import codex.vfx.particles.tweens.Range;
import codex.vfx.utils.VfxUtils;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Easing;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedReference;
import jme3utilities.math.noise.Generator;
import codex.vfx.particles.tweens.Interpolator;
import codex.vfx.particles.OverflowStrategy;

/**
 *
 * @author codex
 */
public class TestInstancedParticles extends DemoApplication implements ParticleDriver {
    
    private float time = 0f;
    private ParticleGroup group;
    private Generator gen = new Generator();
    private VersionedReference<Double>
            rateRef, speedRef, impulseRef, lifeRef, airRef,
            angImpRef, chunkRef, gravityRef;
    
    public static void main(String[] args) {
        new TestInstancedParticles().start();
    }
    
    @Override
    public void demoInitApp() {
        
        //setupVideoCapture("/Videos/instanced-monkeys.avi");
        
        group = new ParticleGroup(1000);
        group.setOverflowStrategy(OverflowStrategy.CullOld);
        group.setDecayRate(1);
        group.addDriver(this);
        group.addDriver(ParticleDriver.ValueUpdate);
        group.addDriver(ParticleDriver.Position);
        group.addDriver(ParticleDriver.Rotation);
        //group.addDriver(ParticleDriver.force(new Vector3f(0f, -5f, 0f)));
        rootNode.attachChild(group);
        
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
        geometry.setMaterial(material);
        Node n = new Node();
        n.attachChild(geometry);
        rootNode.attachChild(n);
        
        //enableLightProbes(false);
        //enableShadows(false);
        
        Container main = new Container();
        main.setLocalTranslation(10, windowSize.y-10, 0);
        Container sliders = new Container();
        sliders.setBackground(null);
        sliders.setLayout(new SpringGridLayout());
        speedRef = createSlider(sliders, 0, "Update Speed", 1f, 0f, 2f);
        impulseRef = createSlider(sliders, 1, "Impulse", 7f, 0f, 30f);
        rateRef = createSlider(sliders, 2, "Rate", .05f, 0f, 3f);
        lifeRef = createSlider(sliders, 3, "Lifetime", 3f, .1f, 10f);
        airRef = createSlider(sliders, 4, "Air Resistance", .9f, 0f, 3f);
        angImpRef = createSlider(sliders, 5, "Angular Impulse", 10f, 0f, 50f);
        chunkRef = createSlider(sliders, 6, "Particles per Emission", 1, 0, 20);
        gravityRef = createSlider(sliders, 7, "Gravity", 5f, 0f, 20f);
        main.addChild(sliders);
        Container buttons = new Container();
        buttons.setBackground(null);
        buttons.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        buttons.addChild(new Button("Reset")).addClickCommands((Button source) -> {
            group.reset();
        });
        main.addChild(buttons);
        guiNode.attachChild(main);
    
    }
    @Override
    public void demoUpdate(float tpf) {
        if (speedRef.update()) {
            group.setUpdateSpeed(speedRef.get().floatValue());
        }
    }
    @Override
    public void updateGroup(ParticleGroup group, float tpf) {
        time += tpf;
        if (time > rateRef.get().floatValue()) for (int i = 0, n = chunkRef.get().intValue(); i < n; i++) {
            ParticleData p = new ParticleData();
            p.setPosition(new Vector3f(0f, 2f, 0f));
            gen.nextUnitVector3f(p.linearVelocity).multLocal(impulseRef.get().floatValue());
            gen.nextUnitVector3f(p.angularVelocity).multLocal(angImpRef.get().floatValue());
            p.setLife(lifeRef.get().floatValue());
            p.setScale(.4f);
            p.color = new Range(
                ColorRGBA.Black,
                ColorRGBA.randomColor(),
                Interpolator.Color,
                Easing.inCubic
            );
            p.size.set(VfxUtils.gen.nextFloat(.8f, 1.2f));
            group.add(p);
            time = 0;
        }
    }
    @Override
    public void updateParticle(ParticleData particle, float tpf) {
        particle.linearVelocity.addLocal(0f, -gravityRef.get().floatValue()*tpf, 0f);
        float resist = FastMath.clamp(1f-(airRef.get().floatValue()*tpf), 0, 1);
        particle.linearVelocity.multLocal(resist);
        particle.angularVelocity.multLocal(resist);
    }
    @Override
    public void particleAdded(ParticleGroup group, ParticleData particle) {}
    @Override
    public void groupReset(ParticleGroup group) {}
    
    private VersionedReference<Double> createSlider(Container c, int row, String label, float value, float min, float max) {
        Slider s = new Slider();
        s.getModel().setMinimum(min);
        s.getModel().setMaximum(max);
        s.getModel().setValue(value);
        s.getRangePanel().setPreferredSize(new Vector3f(200, 0, 0));
        s.setInsets(new Insets3f(3, 3, 3, 5));
        Label l = new Label(label);
        l.setInsets(new Insets3f(3, 15, 3, 5));
        l.setTextHAlignment(HAlignment.Right);
        c.addChild(l, row, 0);
        c.addChild(s, row, 1);
        return s.getModel().createReference();
    }
    
}
