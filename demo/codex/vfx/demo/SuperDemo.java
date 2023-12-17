/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.demo;

import codex.boost.scene.SceneGraphIterator;
import codex.vfx.particles.InstancedParticleGeometry;
import codex.vfx.particles.OverflowProtocol;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.particles.drivers.emission.ParticleFactory;
import codex.vfx.particles.drivers.emission.Spawner;
import codex.vfx.utils.VfxUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import jme3utilities.sky.SkyControl;
import jme3utilities.sky.StarsOption;
import jme3utilities.sky.Updater;

/**
 *
 * @author codex
 */
public class SuperDemo extends SimpleApplication {
    
    private ParticleGroup flame, meteors;
    private InstancedParticleGeometry geometry;
    
    public static void main(String[] args) {
        new SuperDemo().start();
    }
    
    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(new ColorRGBA(.01f, .01f, .01f, 1f));
        flyCam.setMoveSpeed(20);
        
        rootNode.addLight(VfxUtils.loadLightProbe(assetManager, "Models/City_Night_Lights.j3o"));
        AmbientLight gi = new AmbientLight(new ColorRGBA(.5f, .3f, .3f, 1f));
        rootNode.addLight(gi);
        DirectionalLight dl = new DirectionalLight(Vector3f.UNIT_X, new ColorRGBA(.9f, .7f, .7f, 1f));
        rootNode.addLight(dl);
        
        setupMeteors();
        
    }
    @Override
    public void simpleUpdate(float tpf) {}
    
    private void setupMeteors() {
        
//        flame = new ParticleGroup(1000);
//        flame.setOverflowProtocol(OverflowProtocol.CULL_OLD);
        //rootNode.attachChild(flame);
        
        meteors = new ParticleGroup(20);
        //meteors.setCullHint(Spatial.CullHint.Never);
        meteors.setLocalTranslation(0, 2, 0);
        //meteors.setLocalRotation(new Quaternion().lookAt(Vector3f.UNIT_X, Vector3f.UNIT_Y));
        //meteors.setVolume(new EmissionCircle(20));
        meteors.setOverflowProtocol(OverflowProtocol.CULL_OLD);
        //meteors.addDriver(new MeteorFlameDriver(flame));
        meteors.addDriver(ParticleDriver.ValueUpdate);
        meteors.addDriver(ParticleDriver.Position);
        meteors.addDriver(ParticleDriver.Rotation);
        meteors.addDriver(ParticleDriver.TransformToVolume);
        meteors.addDriver(Spawner.create());
        meteors.addDriver(new ParticleFactory() {
            @Override
            public void particleAdded(ParticleGroup group, ParticleData p) {
                p.linearVelocity.set(group.getWorldTransform().getRotation().mult(Vector3f.UNIT_Z));
                p.linearVelocity.multLocal(10f);
                VfxUtils.gen.nextUnitVector3f(p.angularVelocity);
                p.setLife(10f);
            }
        });
        rootNode.attachChild(meteors);
        
        Spatial model = assetManager.loadModel("Models/meteor.j3o");
        Geometry baseMesh = null;
        for (Spatial s : new SceneGraphIterator(model)) {
            if (s instanceof Geometry) {
                baseMesh = (Geometry)s;
                break;
            }
        }
        if (baseMesh == null) {
            throw new NullPointerException("Unable to locate meteor base mesh.");
        }
        
//        PointParticleGeometry flameGeometry = new PointParticleGeometry(flame);
//        Material flameMat = new Material(assetManager, "MatDefs/particles.j3md");
//        flameMat.setBoolean("PointSprite", true);
//        flameGeometry.setMaterial(flameMat);
        
        geometry = new InstancedParticleGeometry(meteors, baseMesh.getMesh());
        baseMesh.getMaterial().setBoolean("UseInstancing", true);
        geometry.setMaterial(baseMesh.getMaterial());
        meteors.attachChild(geometry);
        
        //rootNode.setCullHint(Spatial.CullHint.Dynamic);
        //rootNode.setCullHint(Spatial.CullHint.Never);
        
    }
    
}
