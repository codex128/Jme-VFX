/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

/**
 * Removes particles from a {@link ParticleGroup} when an added
 * particle would make the group's size exceed its capacity.
 * 
 * @author codex
 * @param <T> type of particle data
 */
public interface OverflowProtocol <T extends ParticleData> {
    
    /**
     * Removes the oldest particle (at index=0) from the group when overflow occurs.
     */
    public static final OverflowProtocol CULL_OLD = (ParticleGroup group, ParticleData p) -> {
        group.getParticleList().remove(0);
        return true;
    };
    
    /**
     * Denies the next particle from being added in case of overflow.
     */
    public static final OverflowProtocol CULL_NEW = (ParticleGroup group, ParticleData p) -> false;
    
    /**
     * Removes a particle from the particle group, or returns false
     * to not add the next particle.
     * 
     * @param group
     * @param p next particle to be added to the group
     * @return true to add the next particle, false to not add it
     */
    public boolean removeParticle(ParticleGroup<T> group, T p);
    
}
