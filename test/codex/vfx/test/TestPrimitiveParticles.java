/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.boost.Timer;
import codex.vfx.mesh.MeshPrototype;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.geometry.TriParticleGeometry;
import codex.vfx.test.util.DemoApplication;
import com.jme3.material.Material;

/**
 *
 * @author codex
 */
public class TestPrimitiveParticles extends DemoApplication {

    private ParticleGroup<ParticleData> group;
    private TriParticleGeometry geometry;
    private final Timer timer = new Timer(.1f);
    private final int particlesPerEmission = 5;
    private final float gravity = 5f;
    
    public static void main(String[] args) {
        new TestPrimitiveParticles().start();
    }
    
    @Override
    public void demoInitApp() {
    
        group = new ParticleGroup(1);
        rootNode.attachChild(group);
        
        group.add(new ParticleData());
        
        geometry = new TriParticleGeometry(group, MeshPrototype.QUAD);
        Material mat = new Material(assetManager, "MatDefs/particles.j3md");
        geometry.setMaterial(mat);
        rootNode.attachChild(geometry);
    
    }
    @Override
    public void demoUpdate(float tpf) {}
    
}
