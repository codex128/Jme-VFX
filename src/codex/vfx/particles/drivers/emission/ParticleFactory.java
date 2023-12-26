/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.particles.Particle;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.drivers.ParticleDriver;

/**
 * 
 * @author codex
 * @param <T> type of particle data
 */
public abstract class ParticleFactory <T extends Particle> implements ParticleDriver<T> {

    @Override
    public void updateGroup(ParticleGroup<T> group, float tpf) {}
    @Override
    public void updateParticle(T particle, float tpf) {}
    @Override
    public void groupReset(ParticleGroup<T> group) {}
    
}
