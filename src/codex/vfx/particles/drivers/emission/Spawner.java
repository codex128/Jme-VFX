/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.utils.Value;

/**
 * Simple, easy, niave particle spawner.
 * 
 * @author codex
 */
public class Spawner implements ParticleDriver {
    
    private int maxEmissions = -1;
    private Value<Integer> particlesPerEmission = Value.constant(1);
    private float emissionRate = .1f;
    private float pauseTime = 0f;
    
    private int emissions = 0;
    private float time = 0;
    
    @Override
    public void updateGroup(ParticleGroup group, float tpf) {
        time += tpf;
        if (pauseTime > 0) {
            if (time >= pauseTime) {
                time = pauseTime = 0;
            }
            return;
        }
        if ((maxEmissions < 0 || emissions < maxEmissions) && time >= emissionRate) {
            group.spawnParticles(particlesPerEmission.get());
            time = 0;
            emissions++;
        }
    }
    @Override
    public void updateParticle(ParticleData particle, float tpf) {}
    @Override
    public void particleAdded(ParticleData particle) {}
    
    public void setMaxEmissions(int maxEmissions) {
        this.maxEmissions = maxEmissions;
    }
    public void setParticlesPerEmission(Value<Integer> particlesPerEmission) {
        this.particlesPerEmission = particlesPerEmission;
    }
    public void setEmissionRate(float emissionRate) {
        this.emissionRate = emissionRate;
    }
    public void setPauseTime(float pauseTime) {
        this.pauseTime = pauseTime;
    }
    
    public int getMaxEmissions() {
        return maxEmissions;
    }
    public Value<Integer> getParticlesPerEmission() {
        return particlesPerEmission;
    }
    public float getEmissionRate() {
        return emissionRate;
    }
    public int getEmissions() {
        return emissions;
    }
    
}
