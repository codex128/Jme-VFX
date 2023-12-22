/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.utils;

/**
 * Exception indicating a failure in API.
 * <p>
 * For use while API is inmature.
 * 
 * @author codex
 */
public class InternalException extends NullPointerException {
    
    public InternalException() {
        super();
    }
    
    public InternalException(String message) {
        super(message);
    }
    
}
