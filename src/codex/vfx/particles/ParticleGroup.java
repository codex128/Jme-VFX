/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.particles.drivers.ParticleDriver;
import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Contains a group of particles and properties thereof.
 * 
 * @author codex
 * @param <T> type of particle data this group uses
 */
public class ParticleGroup <T extends ParticleData> implements Iterable<T> {
    
    private final ArrayList<T> particles = new ArrayList<>();
    private final LinkedList<ParticleDriver<T>> drivers = new LinkedList<>();
    private OverflowProtocol overflow = OverflowProtocol.CULL_NEW;
    private final int capacity;
    private float updateSpeed = 1f, decay = 1f;
    
    public ParticleGroup(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Must have at least 1 capacity.");
        }
        this.capacity = capacity;
    }
    
    public void update(float tpf) {
        float t = tpf*updateSpeed;
        for (ParticleDriver<T> d : drivers) {
            d.updateGroup(this, t);
        }
        for (Iterator<T> it = particles.iterator(); it.hasNext();) {
            T p = it.next();
            for (ParticleDriver<T> d : drivers) {
                d.updateParticle(p, t);
            }
            if (!p.update(t*decay)) {
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
        validateGroupSize();
        return c;
    }
    public boolean remove(T particle) {
        return particles.remove(particle);
    }
    
    public void addDriver(ParticleDriver<T> driver, boolean appendToFront) {
        if (appendToFront) {
            drivers.addFirst(driver);
        } else {
            drivers.addLast(driver);
        }
    }
    public void addDriver(ParticleDriver<T> driver) {
        addDriver(driver, false);
    }
    public void removeDriver(ParticleDriver<T> driver) {
        drivers.remove(driver);
    }
    public void clearAllDrivers() {
        drivers.clear();
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
    /**
     * Sets the rate at which particles update.
     * <p>
     * This value is propogated to drivers through {@code tpf}.
     * 
     * @param speed 
     */
    public void setUpdateSpeed(float speed) {
        this.updateSpeed = speed;
    }
    /**
     * Sets the rate at which particles lose life.
     * <p>
     * Decay=1 means particles lose one "life point" each second.
     * Decay=2, particles lose two points each second.
     * <p>
     * A decay of zero allows particles to exist indefinitely.
     * 
     * @param decay 
     */
    public void setDecayRate(float decay) {
        this.decay = decay;
    }
    
    public ParticleData get(int i) {
        return particles.get(i);
    }
    public ArrayList<T> getParticleList() {
        return particles;
    }
    public OverflowProtocol getOverflowHint() {
        return overflow;
    }
    public boolean isEmpty() {
        return particles.isEmpty();
    }
    public int size() {
        return particles.size();
    }
    public int capacity() {
        return capacity;
    }
    public float getUpdateSpeed() {
        return updateSpeed;
    }
    public float getDecayRate() {
        return decay;
    }

    @Override
    public Iterator<T> iterator() {
        return particles.iterator();
    }
    
    private boolean validateGroupSize() {
        if (particles.size() > capacity) {
            throw new IllegalStateException("Number of particles cannot exceed the group capacity..");
        }
        return true;
    }
    
}
