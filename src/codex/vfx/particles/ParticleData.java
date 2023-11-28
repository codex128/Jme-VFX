/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * Contains information about a single particle.
 * <p>
 * May be extended to add more attributes.
 * 
 * @author codex
 */
public class ParticleData {
        
    private float life = 1f, peakLife = life;
    public Vector3f position = new Vector3f();
    public Vector3f normal = new Vector3f(0, 1, 0);
    public Vector3f velocity = new Vector3f();
    public ColorRGBA color = new ColorRGBA(1, 1, 1, 1);
    public float scale = .5f;
    public float angle = 0f;
    public float rotationSpeed = 0f;
    public int spriteIndex = 0;

    public ParticleData() {}
    public ParticleData(float life) {
        peakLife = this.life = life;
    }

    public Vector3f getPosition() {
        return position;
    }
    public Vector3f getNormal() {
        return normal;
    }
    public Vector3f getVelocity() {
        return velocity;
    }
    public ColorRGBA getColor() {
        return color;
    }
    public float getScale() {
        return scale;
    }
    public float getLife() {
        return life;
    }
    public float getPeakLife() {
        return peakLife;
    }
    public float getLifePercent() {
        return life/peakLife;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }
    public void setNormal(Vector3f normal) {
        this.normal.set(normal);
    }
    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }
    public void setColor(ColorRGBA color) {
        this.color.set(color);
    }
    public void setScale(float radius) {
        this.scale = radius;
    }
    public void setLife(float life) {
        peakLife = this.life = life;
    }
    public boolean updateLife(float tpf) {
        life -= tpf;
        return isAlive();
    }
    
    public boolean isAlive() {
        return life >= 0;
    }

}
