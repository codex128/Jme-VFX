/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a method that can be called when a corresponding event occurs.
 * 
 * @author codex
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VfxCommand {
    
    /**
     * 
     * @return 
     */
    String name();
    
}
