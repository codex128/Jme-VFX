/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.utils;

import com.jme3.math.FastMath;

/**
 *
 * @author codex
 */
public class Duration {
    
    private float duration;
    private float time = 0;

    public Duration(float duration) {
        this.duration = duration;
    }
    
    public boolean update(float tpf) {
        time += tpf;
        return isComplete();
    }
    public boolean isComplete() {
        return time >= duration;
    }
    public void reset() {
        time = 0;
    }
    public void reset(float duration) {
        time = 0;
        this.duration = duration;
    }
    public void resetIfComplete() {
        if (isComplete()) time = 0;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
    public void setTime(float time) {
        this.time = time;
    }
    
    public float getDuration() {
        return duration;
    }
    public float getTime() {
        return time;
    }
    public float getPercent() {
        return FastMath.clamp(time/duration, 0, 1);
    }
    
}
