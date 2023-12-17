/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.demo;

import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.utils.Duration;
import codex.vfx.utils.VfxUtils;

/**
 * Spawns particles from meteor particles.
 * 
 * @author codex
 */
public class MeteorFlameDriver implements ParticleDriver {
    
    private final ParticleGroup flame;
    private Duration spawn = new Duration(.1f);
    private int particlesPerEmission = 1;

    public MeteorFlameDriver(ParticleGroup flame) {
        this.flame = flame;
    }
    
    @Override
    public void updateGroup(ParticleGroup group, float tpf) {
        spawn.resetIfComplete();
        spawn.update(tpf);
    }
    @Override
    public void updateParticle(ParticleData particle, float tpf) {
        if (spawn.isComplete()) for (int i = 0; i < particlesPerEmission; i++) {
            // A driver of the flame particle group will setup the particle for us.
            // All we have to do is add it at the correct position.
            ParticleData p = new ParticleData();
            VfxUtils.gen.nextUnitVector3f(p.getPosition())
                    .multLocal(VfxUtils.gen.nextFloat(estimateRadius(particle)))
                    .addLocal(particle.getPosition());
            flame.add(p);
        }
    }
    @Override
    public void particleAdded(ParticleGroup group, ParticleData particle) {}
    @Override
    public void groupReset(ParticleGroup group) {}
    
    private float estimateRadius(ParticleData p) {
        return p.getScale().length()*p.size.get();
    }
    
    public void setSpawnRate(float rate) {
        spawn.reset(rate);
    }
    public void setParticlesPerEmission(int ppe) {
        this.particlesPerEmission = ppe;
    }
    
}
