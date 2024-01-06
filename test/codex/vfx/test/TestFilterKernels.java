/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.vfx.filter.KernelFilter;
import codex.vfx.test.util.DemoApplication;
import com.jme3.math.Matrix3f;
import com.jme3.post.FilterPostProcessor;

/**
 * Test for kernel post filters.
 * 
 * @author codex
 */
public class TestFilterKernels extends DemoApplication {
    
    // Matrix defining how the kernel samples surrounding pixels.
    // Feel free to play around with this.
    private final Matrix3f kernel = new Matrix3f(
        -2,   -2,  -2,
        -2,   15,  -2,
        -2,   -2,  -2
    );
    
    public static void main(String[] args) {
        new TestFilterKernels().start();
    }
    
    @Override
    public void demoInitApp() {
        
        setupCharacter();
        
        FilterPostProcessor f = new FilterPostProcessor(assetManager);
        KernelFilter filter = new KernelFilter();
        filter.setKernelMatrix(kernel);
        filter.setSampleFactor(1f);
        f.addFilter(filter);
        viewPort.addProcessor(f);
        
    }
    @Override
    public void demoUpdate(float tpf) {}
    
}
