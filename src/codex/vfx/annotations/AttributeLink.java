/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author codex
 */
public class AttributeLink implements AnnotatedMethodLink<VfxAttribute> {
    
    private String name;
    private Method input, output;
    private VfxAttribute inAttribute, outAttribute;
    
    @Override
    public void set(Method method, VfxAttribute attribute) {
        assert method != null && attribute != null : "Parameters cannot be null.";
        if (attribute.input()) {
            if (input != null) {
                throw new IllegalStateException("Duplicate input attribute.");
            }
            input = method;
            inAttribute = attribute;
        } else {
            if (output != null) {
                throw new IllegalStateException("Duplicate output attribute.");
            }
            output = method;
            outAttribute = attribute;
        }
        if (name != null) {
            if (!attribute.name().equals(name)) {
                throw new IllegalArgumentException("Attribute names do not match.");
            }
        } else {
            name = attribute.name();
        }
        if (isComplete()) {
            if (input.getParameterCount() != 1) {
                throw new IllegalArgumentException("Expected method accepting exactly one parameter as input.");
            }
            if (output.getReturnType() == Void.class || output.getParameterCount() == 0) {
                throw new IllegalArgumentException("Expected method with no paremeters and a return type as output.");
            }
            if (!input.getParameterTypes()[1].isAssignableFrom(output.getReturnType())) {
                throw new IllegalArgumentException("Return type of output method does not match parameter of input method.");
            }
        }
    }
    public void deleteInput() {
        input = null;
        inAttribute = null;
        if (output == null) {
            name = null;
        }
    }
    public void deleteOutput() {
        output = null;
        outAttribute = null;
        if (input == null) {
            name = null;
        }
    }
    
    @Override
    public void invokeInput(Object object, Object... arguments) {
        try {
            input.invoke(object, arguments);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Failed to invoke method: "+input.getName());
        }
    }
    @Override
    public Object invokeOutput(Object object) {
        try {
            return output.invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Failed to invoke method: "+output.getName());
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    public Method getInputMethod() {
        return input;
    }
    public Method getOutputMethod() {
        return output;
    }
    @Override
    public VfxAttribute getInAnnotation() {
        return inAttribute;
    }
    @Override
    public VfxAttribute getOutAnnotation() {
        return outAttribute;
    }
    public boolean isComplete() {
        return input != null && output != null;
    }
    public Class getType() {
        if (output != null) {
            return output.getReturnType();
        } else {
            return null;
        }
    }
    
}
