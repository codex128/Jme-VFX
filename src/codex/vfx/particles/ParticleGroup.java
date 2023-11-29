/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import java.nio.BufferOverflowException;
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
    
    private final ArrayList<T> particles = new ArrayList<>();
    private final int capacity;
    private OverflowProtocol overflow = OverflowProtocol.CULL_NEW;
    
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
        }
        return (overflow.removeParticle(this, particle) && particles.add(particle)) & validateGroupSize();
    }
    public int addAll(Collection<T> particles) {
        if (this.particles.size()+particles.size() <= capacity) {
            this.particles.addAll(particles);
            return particles.size();
        }
        int c = 0;
        for (T p : particles) {
            if (this.particles.size() >= capacity && !overflow.removeParticle(this, p)) {
                continue;
            }
            this.particles.add(p);
            c++;
        }
        if (particles.size() > capacity) {
            throw new BufferOverflowException();
        }
        return c;
    }
    public boolean remove(T particle) {
        return particles.remove(particle);
    }
    
    /**
     * Sets the protocol for when an added particle would make
     * the group's size exceed its capacity.
     * 
     * @param overflow 
     */
    public void setOverflowProtocol(OverflowProtocol overflow) {
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
    public OverflowProtocol getOverflowHint() {
        return overflow;
    }

    @Override
    public Iterator<T> iterator() {
        return particles.iterator();
    }
    
    private boolean validateGroupSize() {
        if (particles.size() > capacity) {
            throw new BufferOverflowException();
        }
        return true;
    }
    
}
