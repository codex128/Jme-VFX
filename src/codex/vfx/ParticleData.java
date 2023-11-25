/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx;

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
        
    private float life = 1f;
    private final float peakLife;
    public Vector3f position = new Vector3f();
    public Vector3f normal = new Vector3f(0, 1, 0);
    public ColorRGBA color = new ColorRGBA(1, 1, 1, 1);
    public float radius = .5f;

    public ParticleData(float life) {
        this.life = life;
        peakLife = this.life;
    }

    public Vector3f getPosition() {
        return position;
    }
    public Vector3f getNormal() {
        return normal;
    }
    public ColorRGBA getColor() {
        return color;
    }
    public float getRadius() {
        return radius;
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
    public void setColor(ColorRGBA color) {
        this.color.set(color);
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }
    public void incrementLife(float tpf) {
        life -= tpf;
    }
    
    public boolean isAlive() {
        return life >= 0;
    }

}
