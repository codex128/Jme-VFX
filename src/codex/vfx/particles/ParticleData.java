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
 * Main implementation of {@link Particle} which adds many
 * useful properties.
 * <p>
 * Most, if not all, prebuilt functionality in this library will use
 * this implementation.
 * 
 * @author codex
 */
public class ParticleData extends Particle {
    
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
    public Value<Float> angleSpeed = Value.value(0f);
    /**
     * Sprite index {@link Value}.
     * <p>
     * Between 0, which is the upper left corner of the sprite sheet, and
     * the number of total units on the sprite sheet minus one.
     * <p>
     * For a 4x4 sprite sheet (16 units), the index must be between 0 and 15 (inclusive).
     */
    public Value<Integer> spriteIndex = Value.value(0);

    public ParticleData() {}
    public ParticleData(float life) {
        maxLife = this.life = life;
    }    
    
    @Override
    protected void updateValues(float l, float tpf) {
        color.update(l, tpf);
        size.update(l, tpf);
        angle.update(l, tpf);
        angleSpeed.update(l, tpf);
        spriteIndex.update(l, tpf);
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

}
