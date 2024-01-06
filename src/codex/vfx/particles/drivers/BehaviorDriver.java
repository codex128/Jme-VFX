/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers;

import codex.vfx.particles.Particle;
import codex.vfx.particles.ParticleGroup;

/**
 *
 * @author codex
 * @param <T>
 */
public abstract class BehaviorDriver <T extends Particle> implements ParticleDriver<T> {

    @Override
    public void updateGroup(ParticleGroup<T> group, float tpf) {}
    @Override
    public void particleAdded(ParticleGroup<T> group, T particle) {}
    @Override
    public void groupReset(ParticleGroup<T> group) {}
    
}
