/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.tweens;

import codex.vfx.utils.VfxUtils;

/**
 * Represents a value.
 * 
 * @author codex
 * @param <T> type of value being held
 */
public interface Value <T> {
    
    /**
     * Returns the value held.
     * 
     * @return 
     */
    public T get();
    
    /**
     * Sets the value held.
     * 
     * @param val 
     */
    public void set(T val);
    
    /**
     * Updates the held value based on a time component
     * between 0 and 1 and the time per frame.
     * 
     * @param time time between 1 and 0 (inclusive)
     * @param tpf time per frame
     */
    public void update(float time, float tpf);
    
    /**
     * Returns a Value object that represents a single predictable value
     * that can be changed manually but not by update.
     * 
     * @param <T>
     * @param value
     * @return 
     */
    public static <T> ValueImpl<T> value(T value) {
        return new ValueImpl(value);
    }
    
    /**
     * Returns a Value object that represents a single predictable value
     * that cannot be changed.
     * 
     * @param <T>
     * @param value
     * @return 
     */
    public static <T> Constant<T> constant(T value) {
        return new Constant(value);
    }
    
    /**
     * Returns a Value object that generates random values between two extremes.
     * 
     * @param a
     * @param b
     * @return 
     */
    public static RandomValue random(float a, float b) {
        return new RandomValue(a, b);
    }
    
    public static class ValueImpl <T> implements Value<T> {

        private T value;

        private ValueImpl(T value) {
            this.value = value;
        }
        
        @Override
        public T get() {
            return value;
        }
        @Override
        public void set(T val) {
            this.value = val;
        }
        @Override
        public void update(float time, float tpf) {}
        
    }
    public static class Constant <T> implements Value<T> {

        private final T value;

        private Constant(T value) {
            this.value = value;
        }
        
        @Override
        public T get() {
            return value;
        }
        @Override
        public void set(T val) {}
        @Override
        public void update(float time, float tpf) {}
        
    }
    public static class RandomValue implements Value<Float> {

        private float a, b;
        private float value = 0;

        public RandomValue(float a, float b) {
            this.a = a;
            this.b = b;
            update(0, 0);
        }
        
        @Override
        public Float get() {
            return value;
        }
        @Override
        public void set(Float val) {}
        @Override
        public final void update(float time, float tpf) {
            value = VfxUtils.gen.nextFloat(a, b);
        }
        
    }
    
}
