/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 *
 * @author codex
 * @param <T>
 */
public interface AnnotatedMethodLink <T extends Annotation> {
    
    public void set(Method method, T annotation);
    
    public void invokeInput(Object target, Object... arguments);
    
    public Object invokeOutput(Object target);
    
    public String getName();
    
    public T getInAnnotation();
    
    public T getOutAnnotation();
    
}
