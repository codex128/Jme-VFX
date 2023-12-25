/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author codex
 */
public class InfoLink implements AnnotatedMethodLink<VfxInfo> {

    private Method method;
    private VfxInfo info;
    
    @Override
    public void set(Method method, VfxInfo annotation) {
        if (this.method != null) {
            throw new IllegalStateException("Duplicate info.");
        }
        this.method = method;
        this.info = annotation;
        if (this.method.getParameterCount() != 0) {
            throw new IllegalArgumentException("Expected method requiring zero parameters.");
        }
        if (this.method.getReturnType() != Void.class) {
            throw new IllegalArgumentException("Expected method with return type.");
        }
    }
    @Override
    public void invokeInput(Object target, Object... arguments) {}
    @Override
    public Object invokeOutput(Object target) {
        try {
            return method.invoke(target);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Failed to invoke method: "+method.getName());
        }
    }

    @Override
    public String getName() {
        return info.name();
    }
    public Method getMethod() {
        return method;
    }
    @Override
    public VfxInfo getOutAnnotation() {
        return info;
    }
    @Override
    public VfxInfo getInAnnotation() {
        return null;
    }
    
}
