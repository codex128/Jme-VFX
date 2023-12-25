/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks a configurable attribute on a vfx element.
 * 
 * @author codex
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VfxAttribute {
    
    public static final RetentionPolicy POLICY = RetentionPolicy.RUNTIME;
    
    /**
     * 
     * @return 
     */
    String name();
    
    /**
     * Returns true if this annotated element accepts input.
     * <p>
     * Otherwise, the annotated element would return an output.
     * <br>default=true
     * 
     * @return 
     */
    boolean input() default true;
    
}
