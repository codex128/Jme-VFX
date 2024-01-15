/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.utils;

import jme3utilities.math.noise.Generator;

/**
 *
 * @author codex
 */
public class VfxRandomGenerator extends Generator {
    
    @Override
    public float nextFloat(float max) {
        return nextFloat(0, max);
    }
    
}
