/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.drivers.emission;

import codex.vfx.particles.Particle;

/**
 * Spawns particles at certain intervals.
 * <p>
 * This is responsible for the <em>timing</em> and <em>number</em>
 * of the spawns. Whatever is handling the Spawner must do the actual spawning.
 * 
 * @author codex
 * @param <T> type of particle to spawn
 */
public interface Spawner <T extends Particle> {
    
    /**
     * Updates the spawner.
     * 
     * @param time time in seconds
     * @param tpf time per frame
     * @return number of particles to spawn (0 to spawn no particles)
     */
    public int updateSpawn(float time, float tpf);
    
    /**
     * Creates and returns a single particle.
     * 
     * @return spawned particle
     */
    public T spawn();
    
    /**
     * Resets the spawner.
     */
    public void reset();
    
}
