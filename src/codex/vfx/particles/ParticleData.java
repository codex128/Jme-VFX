/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.utils.Value;
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
    public Transform transform = new Transform();
    public Vector3f linearVelocity = new Vector3f();
    public Vector3f angularVelocity = new Vector3f();
//    public ColorRGBA color = new ColorRGBA(1, 1, 1, 1);
//    public float size = .5f;
//    public float angle = 0f;
//    public float rotationSpeed = 0f;
//    public int spriteIndex = 0;
    
    public Value<ColorRGBA> color = Value.value(ColorRGBA.White);
    public Value<Float> size = Value.value(1f);
    public Value<Float> angle = Value.value(0f);
    public Value<Float> rotationSpeed = Value.value(0f);
    public Value<Byte> spriteIndex = Value.value((byte)0);

    public ParticleData() {}
    public ParticleData(float life) {
        maxLife = this.life = life;
    }
    
    public float getLife() {
        return life;
    }
    public float getMaxLife() {
        return maxLife;
    }
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

    public void setLife(float life) {
        maxLife = this.life = life;
    }
    public Vector3f setPosition(Vector3f position) {
        transform.setTranslation(position);
        return transform.getTranslation();
    }
    public Vector3f setPosition(float x, float y, float z) {
        transform.setTranslation(x, y, z);
        return transform.getTranslation();
    }
    public Quaternion setRotation(Quaternion rotation) {
        transform.setRotation(rotation);
        return transform.getRotation();
    }
    public Vector3f setScale(Vector3f scale) {
        transform.setScale(scale);
        return transform.getScale();
    }
    public Vector3f setScale(float x, float y, float z) {
        transform.setScale(x, y, z);
        return transform.getScale();
    }
    public Vector3f setScale(float scale) {
        transform.setScale(scale);
        return transform.getScale();
    }
    
    public void kill() {
        // Setting a boolean instead of setting the life value directly
        // because that may upset drivers performing "over lifetime" operations.
        alive = false;
    }
    public boolean update(float tpf) {
        life = Math.max(life-tpf, 0);
        return isAlive();
    }
    public void updateValues(float tpf) {
        float l = getLifePercent();
        color.update(l, tpf);
        size.update(l, tpf);
        angle.update(l, tpf);
        rotationSpeed.update(l, tpf);
        spriteIndex.update(l, tpf);
    }
    public boolean isAlive() {
        return life > 0 && alive;
    }

}
