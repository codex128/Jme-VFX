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
 *
 * @author codex
 */
public class TestFilterKernels extends DemoApplication {
    
    public static void main(String[] args) {
        new TestFilterKernels().start();
    }
    
    @Override
    public void demoInitApp() {
        
        setupCharacter();
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        KernelFilter filter = new KernelFilter();
        filter.setKernelMatrix(new Matrix3f(2, 2, 2, 2, -15, 2, 2, 2, 2));
        filter.setSampleFactor(1f);
        fpp.addFilter(filter);
        viewPort.addProcessor(fpp);
        
    }
    @Override
    public void demoUpdate(float tpf) {}
    
}
