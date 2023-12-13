/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

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
        
    private float life = 1f, maxLife = life;
    private boolean alive = true;
    public Transform transform = new Transform();
    public Vector3f velocity = new Vector3f();
    public Vector3f angularVelocity = new Vector3f();
    public ColorRGBA color = new ColorRGBA(1, 1, 1, 1);
    public float size = .5f;
    public float angle = 0f;
    public float rotationSpeed = 0f;
    public int spriteIndex = 0;

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
    public void setPosition(Vector3f position) {
        transform.setTranslation(position);
    }
    public void setPosition(float x, float y, float z) {
        transform.setTranslation(x, y, z);
    }
    public void setRotation(Quaternion rotation) {
        transform.setRotation(rotation);
    }
    public void setScale(Vector3f scale) {
        transform.setScale(scale);
    }
    public void setScale(float x, float y, float z) {
        transform.setScale(x, y, z);
    }
    public void setScale(float scale) {
        transform.setScale(scale);
    }
    
    public void kill() {
        // Setting a boolean instead of setting the life value directly
        // because that may upset drivers performing "over lifetime" operations.
        alive = false;
    }
    public boolean update(float rate) {
        life -= rate;
        return isAlive();
    }
    public boolean isAlive() {
        return life > 0 && alive;
    }

}
