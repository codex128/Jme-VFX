/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.gpu;

import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Image;
import com.jme3.texture.Texture;

/**
 * Manages a set of OpenGL Textures and OpenCL images that an OpenCL kernel
 * can "ping pong" between for reading and writing.
 * 
 * @author codex
 */
public class PingPongImages {
    
    public static final int NUM_IMAGES = 2;
    
    private final Texture[] textures = new Texture[NUM_IMAGES];
    private final Image[] images = new Image[NUM_IMAGES];
    private boolean writeMain = true;
    
    public PingPongImages() {}
    public PingPongImages(Texture tex1, Texture tex2, Image image1, Image image2) {
        images[0] = image1;
        images[1] = image2;
    }
    
    public void acquireImagesNoEvent(CommandQueue queue) {
        for (Image i : images) {
            if (i == null) continue;
            i.acquireImageForSharingNoEvent(queue);
        }
    }
    public void releaseImagesNoEvent(CommandQueue queue) {
        for (Image i : images) {
            if (i == null) continue;
            i.releaseImageForSharingNoEvent(queue);
        }
    }
    
    public void flipIndex() {
        writeMain = !writeMain;
    }
    
    public void setTexture(int i, Texture tex) {
        textures[i] = tex;
    }
    public void setImage(int i , Image image) {
        images[i] = image;
    }
    
    public Texture getWriteTexture() {
        return textures[getWriteIndex()];
    }
    public Texture getReadTexture() {
        return textures[getReadIndex()];
    }
    public Texture getTexture(int i) {
        return textures[i];
    }
    public Image getWriteImage() {
        return images[getWriteIndex()];
    }
    public Image getReadImage() {
        return images[getReadIndex()];
    }
    public Image getImage(int i) {
        return images[i];
    }
    public int getWriteIndex() {
        return writeMain ? 0 : 1;
    }
    public int getReadIndex() {
        return writeMain ? 1 : 0;
    }
    
    public Texture[] getTextureArray() {
        return textures;
    }
    public Image[] getImageArray() {
        return images;
    }
    
}
