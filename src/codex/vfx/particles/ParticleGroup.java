/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.particles.ParticleData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Contains a group of particles and properties thereof.
 * 
 * @author codex
 * @param <T> type of particle data this group uses
 */
public class ParticleGroup <T extends ParticleData> implements Iterable<T> {
    
    public enum OverflowHint {
        
        /**
         * In case of overflow, new particles are culled in favor of old particles.
         */
        CullNew,
        
        /**
         * In case of overflow, old particles are culled in favor of new particles.
         */
        CullOld;
        
    }
    
    private final ArrayList<T> particles = new ArrayList<>();
    private final int capacity;
    private OverflowHint overflow = OverflowHint.CullNew;
    
    public ParticleGroup(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Must have at least 1 capacity.");
        }
        this.capacity = capacity;
    }
    
    public void updateMembers(float tpf) {
        for (Iterator<T> it = particles.iterator(); it.hasNext();) {
            ParticleData p = it.next();
            p.position.addLocal(p.velocity.mult(tpf));
            if (!p.updateLife(tpf)) {
                it.remove();
            }
        }
    }
    
    public boolean add(T particle) {
        if (particles.size() < capacity) {
            return particles.add(particle);
        } else if (overflow == OverflowHint.CullOld) {
            particles.remove(0);
            return particles.add(particle);
        }
        return false;
    }
    public int addAll(Collection<T> particles) {
        if (this.particles.size()+particles.size() <= capacity) {
            this.particles.addAll(particles);
            return particles.size();
        }
        int c = 0;
        for (T p : particles) {
            if (this.particles.size() >= capacity) {
                if (overflow == OverflowHint.CullNew) {
                    break;
                } else if (overflow == OverflowHint.CullOld) {
                    this.particles.remove(0);
                }
            }
            this.particles.add(p);
            c++;
        }
        return c;
    }
    public boolean remove(T particle) {
        return particles.remove(particle);
    }
    
    /**
     * Indicates what to do if the particle list exceeds the capacity.
     * 
     * @param overflow 
     */
    public void setOverflowHint(OverflowHint overflow) {
        this.overflow = overflow;
    }
    
    public ParticleData get(int i) {
        return particles.get(i);
    }    
    public ArrayList<T> getParticleList() {
        return particles;
    }
    public int size() {
        return particles.size();
    }
    public int capacity() {
        return capacity;
    }
    public OverflowHint getOverflowHint() {
        return overflow;
    }

    @Override
    public Iterator<T> iterator() {
        return particles.iterator();
    }
    
}
