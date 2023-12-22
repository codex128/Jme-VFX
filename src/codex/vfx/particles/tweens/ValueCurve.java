/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.tweens;

import codex.vfx.utils.InternalException;
import com.jme3.math.EaseFunction;
import com.jme3.math.FastMath;
import java.util.LinkedList;

/**
 *
 * @author codex
 * @param <T>
 */
public class ValueCurve <T> implements Value<T> {

    private final LinkedList<CurvePoint<T>> points = new LinkedList<>();
    private final Interpolator<T> interpolator;
    private float start, end;
    private T value;

    public ValueCurve(Interpolator<T> interpolator) {
        this.interpolator = interpolator;
    }
    public ValueCurve(Interpolator<T> interpolator, CurvePoint<T>... points) {
        this.interpolator = interpolator;
        for (CurvePoint p : points) {
            add(p);
        }
    }
    
    @Override
    public T get() {
        return value;
    }
    @Override
    public void set(T val) {}
    @Override
    public void update(float time, float tpf) {
        if (points.isEmpty()) {            
            throw new IllegalStateException("Curve contains no points.");
        } else if (points.size() == 1) {
            value = points.getFirst().getValue();
            return;
        }
        CurvePoint<T> hi = null;
        CurvePoint<T> low = null;
        float hiPos = 2, lowPos = -1;
        for (CurvePoint<T> point : points) {
            // use percent position from start to end
            float pos = FastMath.unInterpolateLinear(point.getPosition(), start, end);
            if (pos > 1 || pos < 0) {
                throw new InternalException("Point fell outside calculated curve range.");
            }
            // get two points closest to time, one on either side
            if (pos > time && pos-time < hiPos-time) {
                hi = point;
                hiPos = pos;
            } else if (pos <= time && time-pos < time-lowPos) {
                low = point;
                lowPos = pos;
            }
        }
        if (hi != null && low != null) {
            // interpolate between points
            float t = FastMath.unInterpolateLinear(time, low.getPosition(), hi.getPosition());
            t = low.getEasing().apply(FastMath.clamp(t, 0, 1));
            value = interpolator.interpolate(t, low.getValue(), hi.getValue());
        } else if (low != null) {
            value = low.getValue();
        } else if (hi != null) {
            value = hi.getValue();
        }
    }
    
    /**
     * Adds the point to this curve.
     * 
     * @param point 
     * @return this curve isntance
     */
    public final ValueCurve add(CurvePoint<T> point) {
        points.addLast(point);
        if (points.size() == 2) {
            updateCurve();
        } else if (points.size() > 2) {
            if (point.getPosition() < start) {
                start = point.getPosition();
            } else if (point.getPosition() > end) {
                end = point.getPosition();
            }
        }
        return this;
    }
    /**
     * Adds a point with the given properties to this curve.
     * 
     * @param value
     * @param position
     * @return this curve instance
     */
    public ValueCurve add(T value, float position) {
        return add(value, position, Ease.inLinear);
    }
    /**
     * Adds a point with the given properties to this curve.
     * 
     * @param value
     * @param position
     * @param easing
     * @return this curve instance
     */
    public ValueCurve add(T value, float position, EaseFunction easing) {
        return add(new CurvePoint(value, position, easing));
    }
    /**
     * Removes the point from this curve.
     * 
     * @param point 
     */
    public void remove(CurvePoint<T> point) {
        if (points.remove(point)) {
            updateCurve();
        }
    }
    /**
     * Removes the point at the index from this curve.
     * 
     * @param index
     * @return removed point
     */
    public CurvePoint<T> remove(int index) {
        CurvePoint<T> c = points.remove(index);
        if (c != null) {
            updateCurve();
        }
        return c;
    }
    
    private void updateCurve() {
        // find the start and end positions
        start = Float.MAX_VALUE;
        end = -Float.MAX_VALUE;
        for (CurvePoint c : points) {
            if (c.getPosition() < start) {
                start = c.getPosition();
            }
            if (c.getPosition() > end) {
                end = c.getPosition();
            }
        }
    }
    
}
