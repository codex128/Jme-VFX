/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.particles.tweens.Value;

/**
 * Emits particles at certain time intervals.
 * <p>
 * Does not fill in particle data.
 * 
 * @author codex
 * @param <T>
 */
public abstract class Emitter <T extends ParticleData> implements ParticleDriver<T>, Spawner<T> {
    
    private Value<Integer> maxEmissions = Value.value(-1);
    private Value<Integer> particlesPerEmission = Value.value(1);
    private Value<Float> emissionRate = Value.value(.1f);
    
    private int emissions = 0;
    private float time = 0;
    
    @Override
    public void updateGroup(ParticleGroup group, float tpf) {
        int n = updateSpawn(group.getTime(), tpf);
        for (int i = 0; i < n; i++) {
            group.add(spawn());
        }
    }
    @Override
    public void updateParticle(ParticleData particle, float tpf) {}
    @Override
    public void particleAdded(ParticleGroup group, ParticleData particle) {}    
    @Override
    public void groupReset(ParticleGroup group) {
        reset();
    }
    @Override
    public int updateSpawn(float time, float tpf) {
        this.time += tpf;
        int m = maxEmissions.get();
        if ((m < 0 || emissions < m) && this.time >= emissionRate.get()) {
            //for (int i = 0, n = particlesPerEmission.get(); i < n; i++) {
            //    group.add(createParticle());
            //}
            this.time = 0;
            emissions++;
            return particlesPerEmission.get();
        }
        return 0;
    }
    @Override
    public T spawn() {
        return createParticle();
    }
    @Override
    public void reset() {
        emissions = 0;
        time = 0;
    }
    
    /**
     * Creates a new particle instance.
     * 
     * @return 
     */
    protected abstract T createParticle();
    
    public void setMaxEmissions(Value<Integer> maxEmissions) {
        this.maxEmissions = maxEmissions;
    }
    public void setParticlesPerEmission(Value<Integer> particlesPerEmission) {
        this.particlesPerEmission = particlesPerEmission;
    }
    public void setEmissionRate(Value<Float> emissionRate) {
        this.emissionRate = emissionRate;
    }
    
    public Value<Integer> getMaxEmissions() {
        return maxEmissions;
    }
    public Value<Integer> getParticlesPerEmission() {
        return particlesPerEmission;
    }
    public Value<Float> getEmissionRate() {
        return emissionRate;
    }
    public int getNumEmissions() {
        return emissions;
    }
    
    /**
     * Creates a spawner instance using a non-abstract implementation
     * that creates basic particles.
     * 
     * @return 
     */
    public static SpawnerImpl create() {
        return new SpawnerImpl();
    }
    
    public static class SpawnerImpl extends Emitter<ParticleData> {

        @Override
        protected ParticleData createParticle() {
            return new ParticleData();
        }
        
    }
    
}
