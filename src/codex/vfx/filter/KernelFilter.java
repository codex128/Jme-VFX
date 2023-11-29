/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.filter;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.shader.VarType;

/**
 * Test for kernel-based filters.
 * 
 * @author codex
 */
public class KernelFilter extends Filter {
    
    private Matrix3f kernel;
    private float factor = 1;
    
    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "MatDefs/kernelFilter.j3md");
        material.setVector2("SampleStep", Vector2f.UNIT_XY.divide(w, h));
        if (kernel == null) {
            throw new NullPointerException("Kernel is not set.");
        }
        material.setParam("Kernel", VarType.Matrix3, kernel);
        material.setFloat("SampleFactor", factor);
    }
    @Override
    protected Material getMaterial() {
        return material;
    }
    
    public void setKernelMatrix(Matrix3f kernel) {
        this.kernel = kernel;
    }    
    public void setSampleFactor(float factor) {
        this.factor = factor;
    }
    
}
