/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.particles.drivers.emission.ParticleFactory;
import codex.vfx.particles.drivers.emission.EmissionVolume;
import codex.vfx.particles.drivers.ParticleDriver;
import codex.vfx.particles.drivers.emission.EmissionPoint;
import com.jme3.scene.Node;
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
public class ParticleGroup <T extends ParticleData> extends Node implements Iterable<T> {
    
    private final ArrayList<T> particles = new ArrayList<>();
    private final LinkedList<ParticleDriver<T>> drivers = new LinkedList<>();
    private OverflowProtocol overflow = OverflowProtocol.CULL_NEW;
    private ParticleFactory<T> factory;
    private EmissionVolume volume = new EmissionPoint();
    private final int capacity;
    private float updateSpeed = 1f, decay = 1f;
    private float time = 0f;
    
    public ParticleGroup(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Must have at least 1 capacity.");
        }
        this.capacity = capacity;
    }
    
    /**
     * Manually updates this group.
     * <p>
     * Do not call if this if this group is attached to the
     * scene graph, because this will be called automatically.
     * 
     * @param tpf time per frame
     */
    public void update(float tpf) {
        float t = tpf*updateSpeed;
        time += t;
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
    @Override
    public void updateLogicalState(float tpf) {
        update(tpf);
    }
    
    private boolean addParticle(T particle) {
        if (particles.add(particle)) {
            for (ParticleDriver<T> d : drivers) {
                d.particleAdded(particle);
            }
            return true;
        }
        return false;
    }
    private void addParticle(T particle, int index) {
        particles.add(index, particle);
        for (ParticleDriver<T> d : drivers) {
            d.particleAdded(particle);
        }
    }
    
    /**
     * Adds a particle to this group.
     * 
     * @param particle
     * @return true if the particle was successfully added to the group
     */
    public boolean add(T particle) {
        if (particles.size() < capacity) {
            return addParticle(particle);
        }
        return (overflow.removeParticle(this, particle) && addParticle(particle)) & validateGroupSize(true);
    }
    /**
     * Adds the particle to this group at the specified index.
     * 
     * @param particle particle to add
     * @param index index to add the particle at. Must not be out of bounds.
     * @return true if the particle was successfully added to the group
     */
    public boolean add(T particle, int index) {
        if (particles.size() < capacity) {
            addParticle(particle, index);
            return true;
        }
        if (overflow.removeParticle(this, particle)) {
            addParticle(particle, index);
            validateGroupSize(true);
            return true;
        }
        return false;
    }
    /**
     * Adds all the particles in the collection to this group.
     * 
     * @param particles
     * @return number of particles successfully added to the group
     */
    public int addAll(Collection<T> particles) {
        if (this.particles.size()+particles.size() <= capacity) {
            this.particles.addAll(particles);
            for (T p : particles) for (ParticleDriver<T> d : drivers) {
                d.particleAdded(p);
            }
            return particles.size();
        }
        int c = 0;
        for (T p : particles) {
            if (this.particles.size() >= capacity && !overflow.removeParticle(this, p)) {
                continue;
            }
            if (addParticle(p)) c++;
        }
        validateGroupSize(true);
        return c;
    }
    /**
     * Removes the particle from the group.
     * 
     * @param particle
     * @return true if the particle existed in the group
     */
    public boolean remove(T particle) {
        return particles.remove(particle);
    }
    /**
     * Clears all particles from this group.
     */
    public void clearAllParticles() {
        particles.clear();
    }
    
    /**
     * Spawn a particle using this group's particle factory, emission volume, and world transform.
     * <p>
     * The spawned particle is added to this group.
     * 
     * @return spawned particle
     */
    public T spawnParticle() {
        return spawnParticle(true);
    }
    /**
     * Spawn a particle using this group's particle factory, emission volume, and world transform.
     * <p>
     * The spawned particle is added to this group only if indicated.
     * 
     * @param add if true, the spawned particle is added to this group
     * @return spawned particle
     */
    public T spawnParticle(boolean add) {
        T p = factory.createParticle(worldTransform, volume);
        if (add) add(p);
        return p;
    }
    /**
     * Spawn a number of particles using this group's particle factory, emission volume, and world transform.
     * <p>
     * The spawned particles are added to this group.
     * 
     * @param n number of particles to spawn
     */
    public void spawnParticles(int n) {
        while (n-- > 0) {
            add(factory.createParticle(worldTransform, volume));
        }
    }
    
    /**
     * Add a particle driver.
     * <p>
     * The driver will be added to the front of the driver stack if indicated.
     * Otherwise, the driver will be added to the rear.
     * 
     * @param driver
     * @param appendToFront 
     */
    public void addDriver(ParticleDriver<T> driver, boolean appendToFront) {
        if (appendToFront) {
            drivers.addFirst(driver);
        } else {
            drivers.addLast(driver);
        }
    }
    /**
     * Adds a particle driver to the end of the driver stack.
     * 
     * @param driver 
     */
    public void addDriver(ParticleDriver<T> driver) {
        addDriver(driver, false);
    }
    /**
     * Removes the driver from the driver stack.
     * 
     * @param driver 
     */
    public void removeDriver(ParticleDriver<T> driver) {
        drivers.remove(driver);
    }
    /**
     * Removes all drivers from the driver stack.
     */
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
     * Set the default particle factory used by this particle group and subsiquent drivers.
     * 
     * @param factory particle factory (not null)
     */
    public void setFactory(ParticleFactory<T> factory) {
        assert factory != null : "Factory cannot be null.";
        this.factory = factory;
    }
    /**
     * Set the default emission volume used by this particle group and subsiquent drivers.
     * 
     * @param volume emission volume (not null)
     */
    public void setVolume(EmissionVolume volume) {
        assert volume != null : "Emission volume cannot be null";
        this.volume = volume;
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
    
    /**
     * Resets the simulation by removing all particles and reseting the simulation time.
     */
    public void resetSimulation() {
        particles.clear();
        time = 0;
    }
    /**
     * Resets the simulation time.
     */
    public void resetTime() {
        time = 0;
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
    public ParticleFactory<T> getFactory() {
        return factory;
    }
    public EmissionVolume getVolume() {
        return volume;
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
    public float getTime() {
        return time;
    }

    @Override
    public Iterator<T> iterator() {
        return particles.iterator();
    }
    
    private boolean validateGroupSize(boolean exception) {
        if (particles.size() > capacity) {
            if (exception) {
                throw new IllegalStateException("Number of particles cannot exceed the group capacity.");
            }
            return true;
        }
        return false;
    }
    
}
