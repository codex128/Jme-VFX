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
public interface OverflowStrategy <T extends Particle> {
    
    /**
     * Removes a particle from the particle group.
     * 
     * @param group
     * @param particle latest particle added or null if unknown
     * @return true if the removed particle was the latest particle added
     */
    public boolean removeParticle(ParticleGroup<T> group, T particle);
    
    /**
     * Removes the "oldest" particle (at index=0) from the group when overflow occurs.
     */
    public static final OverflowStrategy CullOld = (ParticleGroup group, Particle particle) -> {
        return group.remove(0) == particle && particle != null;
    };
    
    /**
     * Removes the "newest" particle (at end of list) from the group when overflow occurs.
     */
    public static final OverflowStrategy CullNew = (ParticleGroup group, Particle particle) -> {
        return group.remove(group.size()-1) == particle && particle != null;
    };
    
}
