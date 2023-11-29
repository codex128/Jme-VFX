/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.vfx.test.util.DemoApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.FrameBuffer.FrameBufferTarget;
import com.jme3.texture.FrameBuffer.FrameBufferTextureTarget;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;

/**
 *
 * @author codex
 */
public class TestMultipleFramebuffers extends DemoApplication {

    Node scene = new Node();
    
    public static void main(String[] args) {
        new TestMultipleFramebuffers().start();
    }
    
    @Override
    public void demoInitApp() {
        
        ViewPort vp = renderManager.createPreView("offscreen-view", cam);
        vp.setClearFlags(true, true, true);
        
        int width = context.getFramebufferWidth();
        int height = context.getFramebufferHeight();
        
        FrameBuffer fb = new FrameBuffer(width, height, 5);
        
        Texture2D texture = new Texture2D(width, height, Image.Format.RGBA8);
        //texture.setMinFilter(Texture.MinFilter.Trilinear);
        //texture.setMagFilter(Texture.MagFilter.Bilinear);
        
        FrameBufferTextureTarget fbtt = FrameBufferTarget.newTarget(texture);
        fb.addColorTarget(fbtt);
        
        vp.setOutputFrameBuffer(fb);
        vp.attachScene(scene);
        
        Geometry cube = new Geometry("cube", new Box(1f, 1f, 1f));
        cube.setLocalTranslation(0f, 2f, 0f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);
        cube.setMaterial(mat);
        rootNode.attachChild(cube);
        
        Geometry offscreenCube = new Geometry("cube", new Box(.4f, .4f, .4f));
        offscreenCube.setLocalTranslation(0f, 2f, 0f);
        Material offscreenMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        offscreenMat.setColor("Color", ColorRGBA.Blue);
        offscreenCube.setMaterial(offscreenMat);
        scene.attachChild(offscreenCube);
        
    }
    @Override
    public void demoUpdate(float tpf) {
        scene.updateLogicalState(tpf);
        scene.updateGeometricState();
    }
    
}
