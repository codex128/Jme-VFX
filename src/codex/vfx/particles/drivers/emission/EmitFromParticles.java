/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.utils.VfxUtils;
import com.jme3.math.Transform;

/**
 * Emits particles to a target group from particles in an emission group.
 * 
 * @author codex
 */
public class EmitFromParticles implements ParticleDriver<ParticleData> {
    
    private final ParticleGroup<ParticleData> emitter;
    private Spawner<ParticleData> spawner;
    private boolean stopIfFull = true;
    private Transform transform = new Transform();

    public EmitFromParticles(ParticleGroup emitter) {
        this.emitter = emitter;
    }
    public EmitFromParticles(ParticleGroup emitter, Spawner spawner) {
        this.emitter = emitter;
        this.spawner = spawner;
    }
    public EmitFromParticles(ParticleGroup emitter, Spawner spawner, boolean stopIfFull) {
        this.emitter = emitter;
        this.spawner = spawner;
        this.stopIfFull = stopIfFull;
    }
    
    @Override
    public void updateGroup(ParticleGroup group, float tpf) {
        if (spawner != null) {
            int n = spawner.updateSpawn(group.getTime(), tpf);
            if (n > 0) for (ParticleData emit : emitter) {
                for (int i = 0; i < n; i++) {
                    if (stopIfFull && group.isFull()) {
                        // no need to create particles that will only be rejected
                        break;
                    }
                    ParticleData p = spawner.spawn();
                    transform.set(emit.transform);
                    transform.getScale().multLocal(emit.size.get());
                    p.setPosition(group.getVolume().getNextPosition(transform));
                    group.add(p);
                }
            }
        }
    }
    @Override
    public void updateParticle(ParticleData particle, float tpf) {}
    @Override
    public void particleAdded(ParticleGroup group, ParticleData p) {
        if (spawner == null && !emitter.isEmpty()) {
            // assign particle to a random emission particle
            p.setPosition(group.getVolume().getNextPosition(emitter.get(VfxUtils.gen.nextInt(emitter.size())).transform));
        }
    }
    @Override
    public void groupReset(ParticleGroup group) {
        if (spawner != null) {
            spawner.reset();
        }
    }
    
    /**
     * Sets the spawner responsible for timing emissions.
     * <p>
     * If the spawner is null, then this driver will not emit particles, but
     * relocate added particles to a random emission particle.
     * <p>
     * default=null
     * 
     * @param spawner 
     */
    public void setSpawner(Spawner spawner) {
        this.spawner = spawner;
    }
    /**
     * Sets this driver to stop emitting particles if the target
     * group is full.
     * <p>
     * default=true
     * 
     * @param stopIfFull 
     */
    public void setStopIfFull(boolean stopIfFull) {
        this.stopIfFull = stopIfFull;
    }
    
    public ParticleGroup<ParticleData> getEmissionGroup() {
        return emitter;
    }
    public Spawner getSpawner() {
        return spawner;
    }
    public boolean isStopIfFull() {
        return stopIfFull;
    }
    
}
