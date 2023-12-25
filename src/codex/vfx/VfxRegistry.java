/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx;

import java.util.LinkedList;

/**
 * Registry for root vfx objects.
 * 
 * @author codex
 */
public class VfxRegistry {
    
    private static final LinkedList<VirtualEffect> effects = new LinkedList<>();
    
    public static void register(VirtualEffect effect) {
        effects.add(effect);
    }
    public static void remove(VirtualEffect effect) {
        effects.remove(effect);
    }
    
    public static LinkedList<VirtualEffect> getEffects() {
        return effects;
    }
    
    public static void play() {
        for (VirtualEffect e : effects) {
            e.play();
        }
    }
    public static void pause() {
        for (VirtualEffect e : effects) {
            e.pause();
        }
    }
    
}
