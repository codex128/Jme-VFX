/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.particles.tweens.Value;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;

/**
 * Contains information about a single particle.
 * <p>
 * May be extended to add more attributes.
 * 
 * @author codex
 */
public class ParticleData {
    
    protected float life = 1f, maxLife = life;
    protected boolean alive = true;
    
    /**
     * World/model space transform.
     */
    public Transform transform = new Transform();
    /**
     * Linear velocity.
     */
    public Vector3f linearVelocity = new Vector3f();
    /**
     * Angular velocity.
     */
    public Vector3f angularVelocity = new Vector3f();
    /**
     * Color {@link Value}.
     */
    public Value<ColorRGBA> color = Value.value(ColorRGBA.White);
    /**
     * Size {@link Value}.
     * <p>
     * Multiplies {@link #transform} scale property.
     */
    public Value<Float> size = Value.value(1f);
    /**
     * Screen space angle {@link Value}.
     */
    public Value<Float> angle = Value.value(0f);
    /**
     * Screen space rotation speed {@link Value}.
     */
    public Value<Float> rotationSpeed = Value.value(0f);
    /**
     * Sprite faction {@link Value}.
     * <p>
     * Between 0, which is the upper left corner of the sprite sheet, and
     * the number of total units on the sprite sheet minus one.
     * <p>
 For a 4x4 sprite sheet (16 units), the faction must be between 0 and 15 (inclusive).
     */
    public Value<Integer> spriteIndex = Value.value(0);

    public ParticleData() {}
    public ParticleData(float life) {
        maxLife = this.life = life;
    }
    
    public float getLife() {
        return life;
    }
    /**
     * Gets the maximum life.
     * <p>
     * This is the life amount when the particle was newly created.
     * 
     * @return 
     */
    public float getMaxLife() {
        return maxLife;
    }
    /**
     * Gets the percentage life remaining.
     * <p>
     * Full life returns 1.0 and no life returns 0.0.
     * <p>
     * <em>Note: this measures the percentage of life remaining,
     * not percentage of life decayed!</em>
     * 
     * @return life percentage
     */
    public float getLifePercent() {
        return life/maxLife;
    }
    public Vector3f getPosition() {
        return transform.getTranslation();
    }
    public Quaternion getRotation() {
        return transform.getRotation();
    }
    public Vector3f getScale() {
        return transform.getScale();
    }
    
    /**
     * Sets the lifetime.
     * 
     * @param life 
     */
    public void setLife(float life) {
        maxLife = this.life = life;
    }
    /**
     * Sets the position.
     * 
     * @param position
     * @return new position
     */
    public Vector3f setPosition(Vector3f position) {
        transform.setTranslation(position);
        return transform.getTranslation();
    }
    /**
     * Sets the position.
     * 
     * @param x
     * @param y
     * @param z
     * @return new position
     */
    public Vector3f setPosition(float x, float y, float z) {
        transform.setTranslation(x, y, z);
        return transform.getTranslation();
    }
    /**
     * Sets the rotation.
     * 
     * @param rotation
     * @return new rotation
     */
    public Quaternion setRotation(Quaternion rotation) {
        transform.setRotation(rotation);
        return transform.getRotation();
    }
    /**
     * Sets the scale.
     * 
     * @param scale
     * @return new scale
     */
    public Vector3f setScale(Vector3f scale) {
        transform.setScale(scale);
        return transform.getScale();
    }
    /**
     * Sets the scale.
     * 
     * @param x
     * @param y
     * @param z
     * @return new scale
     */
    public Vector3f setScale(float x, float y, float z) {
        transform.setScale(x, y, z);
        return transform.getScale();
    }
    /**
     * Sets the scale.
     * 
     * @param scale
     * @return new scale
     */
    public Vector3f setScale(float scale) {
        transform.setScale(scale);
        return transform.getScale();
    }
    
    /**
     * Kills the particle so it is destroyed on next update.
     */
    public void kill() {
        // Setting a boolean instead of setting the life value directly
        // because that may upset drivers performing "over lifetime" operations.
        alive = false;
    }
    /**
     * Updates the particle.
     * 
     * @param tpf
     * @return true if the particle is alive, false otherwise
     */
    public boolean update(float tpf) {
        life = Math.max(life-tpf, 0);
        updateValues(tpf);
        return isAlive();
    }
    /**
     * Updates all {@link Value} objects belonging to this particle.
     * 
     * @param tpf 
     */
    protected void updateValues(float tpf) {
        float l = getLifePercent();
        color.update(l, tpf);
        size.update(l, tpf);
        angle.update(l, tpf);
        rotationSpeed.update(l, tpf);
        spriteIndex.update(l, tpf);
    }
    /**
     * Returns true if the particle is alive.
     * <p>
     * i.e. life is greater than zero, and {@link #kill()} has not been called.
     * 
     * @return 
     */
    public boolean isAlive() {
        return life > 0 && alive;
    }

}
