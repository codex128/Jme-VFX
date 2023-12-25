/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.boost.scene.SceneGraphIterator;
import codex.vfx.particles.geometry.InstancedParticleGeometry;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.geometry.PointParticleGeometry;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.particles.drivers.emission.EmissionBox;
import codex.vfx.particles.drivers.emission.EmissionSphere;
import codex.vfx.particles.drivers.emission.EmitFromParticles;
import codex.vfx.particles.drivers.emission.ParticleFactory;
import codex.vfx.particles.drivers.emission.Emitter;
import codex.vfx.particles.tweens.Ease;
import codex.vfx.particles.tweens.Range;
import codex.vfx.particles.tweens.Value;
import codex.vfx.utils.VfxUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.TextureKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.SSRFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import codex.vfx.particles.tweens.Interpolator;
import codex.vfx.particles.OverflowStrategy;

/**
 *
 * @author codex
 */
public class TestMultipleSystems extends SimpleApplication {
    
    public static void main(String[] args) {
        new TestMultipleSystems().start();
    }
    
    @Override
    public void simpleInitApp() {
        
        flyCam.setMoveSpeed(40);
        viewPort.setBackgroundColor(new ColorRGBA(.01f, .01f, .01f, 1f));
        
        rootNode.addLight(VfxUtils.loadLightProbe(assetManager, "Scenes/City_Night_Lights.j3o"));
        rootNode.addLight(new AmbientLight(new ColorRGBA(.3f, 0f, 0f, 1f)));
        rootNode.addLight(new DirectionalLight(new Vector3f(1f, 0f, 0f), new ColorRGBA(1f, .5f, .3f, 1f)));
        
        ParticleGroup main = new ParticleGroup(0);
        rootNode.attachChild(main);
        
        ParticleGroup asteroids = new ParticleGroup(20);
        asteroids.setName("asteroid group");
        asteroids.setLocalTranslation(0, 0, 0);
        asteroids.setOverflowStrategy(OverflowStrategy.CullNew);
        asteroids.setInitialDelay(.0f);
        asteroids.setDecayRate(0);
        asteroids.setVolume(new EmissionBox(new Vector3f(0, 0, -50), 40, 40, 50));
        asteroids.addDriver(ParticleDriver.Position);
        asteroids.addDriver(ParticleDriver.Rotation);
        asteroids.addDriver(ParticleDriver.TransformToVolume);
        asteroids.addDriver(ParticleDriver.force(new Vector3f(0f, -1f, 0f)));
        asteroids.addDriver(new ParticleFactory() {
            @Override
            public void particleAdded(ParticleGroup group, ParticleData p) {
                p.size.set(VfxUtils.gen.nextFloat(.8f, 1.2f));
                //p.setScale(VfxUtils.gen.nextFloat(0.7f, 1.3f));
                p.linearVelocity.set(group.getWorldTransform().getRotation().mult(Vector3f.UNIT_Z));
                p.linearVelocity.multLocal(50f);
                VfxUtils.gen.nextUnitVector3f(p.angularVelocity);
                p.angularVelocity.multLocal(VfxUtils.gen.nextFloat(10f, 15f));
            }
        });
        asteroids.addDriver(new ParticleDriver() {
            @Override
            public void updateGroup(ParticleGroup group, float tpf) {}
            @Override
            public void updateParticle(ParticleData p, float tpf) {
                if (p.getPosition().z > 100) {
                    p.setPosition(asteroids.getVolume().getNextPosition(asteroids.getWorldTransform()));
                    p.linearVelocity.y = 0;
                }
            }
            @Override
            public void particleAdded(ParticleGroup group, ParticleData particle) {}
            @Override
            public void groupReset(ParticleGroup group) {}
        });
        Emitter emitter = Emitter.create();
        emitter.setMaxEmissions(Value.constant(1));
        emitter.setParticlesPerEmission(Value.value(20));
        asteroids.addDriver(emitter);
        main.attachChild(asteroids);
        
        ParticleGroup flames = new ParticleGroup(5000);
        flames.setName("flames group");
        flames.setOverflowStrategy(OverflowStrategy.CullNew);
        flames.setVolume(new EmissionSphere(.8f));
        flames.setDecayRate(1f);
        flames.addDriver(new ParticleFactory() {
            @Override
            public void particleAdded(ParticleGroup group, ParticleData p) {
                //p.color = new Range(ColorRGBA.BlackNoAlpha, new ColorRGBA(1, .3f, 0, .5f), Interpolator.Color, Easing.inLinear);
                p.color.set(new ColorRGBA(1, 1, 1, .5f));
                //p.size = new Range(.2f, .5f, Interpolator.Float, Easing.inLinear);
                p.size.set(.5f);
                p.spriteIndex = new Range(16, 0, Interpolator.Integer, Ease.inLinear);
                //p.spriteIndex.set(5);
                p.setLife(.5f);
            }
        });
        Emitter flameEmit = Emitter.create();
        flameEmit.setEmissionRate(Value.constant(.01f));
        flameEmit.setParticlesPerEmission(Value.constant(5));
        flames.addDriver(new EmitFromParticles(asteroids, flameEmit, false));
        asteroids.attachChild(flames);
        
        ParticleGroup smoke = new ParticleGroup(7000);
        smoke.setName("smoke group");
        smoke.setOverflowStrategy(OverflowStrategy.CullOld);
        smoke.setVolume(new EmissionSphere(.1f));
        smoke.addDriver(ParticleDriver.Angle);
        smoke.addDriver(ParticleDriver.Position);
        smoke.addDriver(ParticleDriver.force(new Vector3f(1f, 0f, 0f)));
        smoke.addDriver(new ParticleFactory() {
            @Override
            public void particleAdded(ParticleGroup group, ParticleData p) {
                //p.color = new Range(ColorRGBA.BlackNoAlpha, ColorRGBA.White, Interpolator.Color, Easing.inLinear);
                p.color = new Range(new ColorRGBA(0f, 0f, 0f, 0f), new ColorRGBA(0.01f, 0.01f, 0.01f, .3f), Interpolator.Color, Ease.inQuint);
                //p.color = new Range(new ColorRGBA(0f, 0f, 0f, 0f), new ColorRGBA(.005f, .005f, .005f, 1f),
                //        Interpolator.Color, Ease.inOut(Ease.inLinear, Ease.inQuad, .97f));
                //p.color = new MultiRange(
                //    new Range(ColorRGBA.BlackNoAlpha, new ColorRGBA(.6f, .6f, .6f, .07f), Interpolator.Color, Easing.inLinear),
                //    new Range(new ColorRGBA(.6f, .6f, .6f, .07f), ColorRGBA.BlackNoAlpha, Interpolator.Color, Easing.inLinear)
                //);
                //p.size.set(1f);
                p.size = new Range(1f, 10f, Interpolator.Float, Ease.outQuad);
                //p.size = new Range(1f, 1f, Interpolator.Float, Ease.inOut(Ease.inLinear, Ease.outQuint, .99f));
                //p.spriteIndex = new Range(15, 0, Interpolator.Integer, Easing.outCubic);
                //p.spriteIndex = new Random(0, 15, Interpolator.Integer, 0.03f);
                //p.spriteIndex.set(VfxUtils.gen.nextInt(15));
                p.setLife(5.7f);
                p.angle.set(VfxUtils.gen.nextFloat(FastMath.TWO_PI));
                p.rotationSpeed.set(VfxUtils.gen.nextFloat(-.1f, .1f));
            }
        });
        Emitter smokeEmit = Emitter.create();
        smokeEmit.setEmissionRate(Value.constant(.02f));
        smokeEmit.setParticlesPerEmission(Value.constant(1));
        smoke.addDriver(new EmitFromParticles(asteroids, smokeEmit, false));
        asteroids.attachChild(smoke);
        
        Spatial model = assetManager.loadModel("Models/meteor.j3o");
        Geometry base = null;
        for (Spatial s : new SceneGraphIterator(model)) {
            if (s instanceof Geometry) {
                base = (Geometry)s;
                break;
            }
        }
        if (base == null) {
            throw new NullPointerException("Unable to locate geometry.");
        } 
        InstancedParticleGeometry geometry = new InstancedParticleGeometry(asteroids, base.getMesh());
        base.getMaterial().setBoolean("UseInstancing", true);
        base.getMaterial().setTexture("EmissiveMap", assetManager.loadTexture(new TextureKey("Models/meteor-flame-glow.png", false)));
        base.getMaterial().setFloat("EmissivePower", 10);
        base.getMaterial().setFloat("EmissiveIntensity", 10);
        geometry.setMaterial(base.getMaterial());
        asteroids.attachChild(geometry);
        
        PointParticleGeometry flameGeometry = new PointParticleGeometry(flames);
        flameGeometry.enableSpriteSheet(true);
        flameGeometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        Material flameMat = new Material(assetManager, "MatDefs/particles.j3md");
        flameMat.setTexture("ColorMap", assetManager.loadTexture("Textures/Effects/explosion.png"));
        //flameMat.setFloat("AlphaDiscard", .2f);
        flameMat.setBoolean("PointSprite", true);
        flameMat.setVector2("SpriteGrid", new Vector2f(4, 4));
        flameMat.setFloat("AlphaDiscard", .1f);
        flameMat.setBoolean("Glow", true);
        flameMat.getAdditionalRenderState().setDepthWrite(false);
        flameMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaSumA);
        flameMat.setTransparent(true);
        flameGeometry.setMaterial(flameMat);
        flames.attachChild(flameGeometry);
        
        PointParticleGeometry smokeGeometry = new PointParticleGeometry(smoke);
        //smokeGeometry.enableSpriteSheet(true);
        smokeGeometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        Material smokeMat = new Material(assetManager, "MatDefs/particles.j3md");
        //smokeMat.setBoolean("Grayscale", true);
        smokeMat.setTexture("ColorMap", assetManager.loadTexture("Textures/Effects/smoke-cloud.png"));
        smokeMat.setBoolean("PointSprite", true);
        smokeMat.setBoolean("RotateTexture", true);
        //smokeMat.setVector2("SpriteGrid", new Vector2f(1, 1));
        //smokeMat.setFloat("AlphaDiscard", .01f);
        smokeMat.getAdditionalRenderState().setDepthWrite(false);
        smokeMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
        smokeMat.setTransparent(true);
        smokeGeometry.setMaterial(smokeMat);
        smoke.attachChild(smokeGeometry);
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        SSRFilter ssr = new SSRFilter();
        fpp.addFilter(ssr);
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setBlurScale(1);
        bloom.setBloomIntensity(10);
        bloom.setExposurePower(2f);
        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
        
        inputManager.addMapping("reset", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("record", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener((ActionListener)(String name, boolean isPressed, float tpf) -> {
            if (isPressed) {
                switch (name) {
                    case "reset":
                        asteroids.reset();
                        flames.reset();
                        smoke.reset();
                        break;
                    case "record":
                        stateManager.attach(new VideoRecorderAppState());
                        break;
                    case "pause":
                        asteroids.flipPlayState();
                        break;
                    default:
                        break;
                }
            }
        }, "reset", "record", "pause");
    
    }
    @Override
    public void simpleUpdate(float tpf) {}
    
}
