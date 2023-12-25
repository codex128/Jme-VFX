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
public class CommandLink implements AnnotatedMethodLink<VfxCommand> {
    
    private Method method;
    private VfxCommand command;
    
    @Override
    public void set(Method method, VfxCommand command) {
        if (this.method != null) {
            throw new IllegalStateException("Duplicate commands.");
        }
        assert method != null && command != null : "Parameters cannot be null.";
        this.method = method;
        this.command = command;
        if (this.method.getParameterCount() != 0) {
            throw new IllegalArgumentException("Expected method requiring no parameters.");
        }
    }
    @Override
    public void invokeInput(Object object, Object... arguments) {
        try {
            method.invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Failed to invoke method: "+method.getName());
        }
    }    
    @Override
    public Object invokeOutput(Object target) {
        return null;
    }
    
    @Override
    public String getName() {
        return command != null ? command.name() : null;
    }
    public Method getMethod() {
        return method;
    }
    @Override
    public VfxCommand getInAnnotation() {
        return command;
    }
    @Override
    public VfxCommand getOutAnnotation() {
        return null;
    }
    
}
