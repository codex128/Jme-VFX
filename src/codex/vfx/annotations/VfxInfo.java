/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks additional useful information about vfx elements.
 * 
 * @author codex
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VfxInfo {
    
    /**
     * 
     * @return 
     */
    String name();
    
    /**
     * Returns true of the information is important for users.
     * <p>
     * default=true
     * 
     * @return 
     */
    boolean important() default true;
    
    /**
     * Returns true if displayed info should be updated in realtime.
     * <p>
     * default=false
     * 
     * @return 
     */
    boolean realtime() default false;
    
}
