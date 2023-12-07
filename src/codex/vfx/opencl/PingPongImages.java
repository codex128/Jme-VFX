/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.opencl;

import com.jme3.material.Material;
import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Context;
import com.jme3.opencl.Image;
import com.jme3.opencl.Kernel;
import com.jme3.opencl.MemoryAccess;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.TextureArray;
import java.util.Arrays;

/**
 * Manages a set of two images optionally bound to a set of two textures, and
 * "ping pongs" between them every update.
 * <p>
 * First update: read from image1, write to image2<br>
 * Second update: read from image2, write to image1<br>
 * ...and so on...
 * 
 * @author codex
 */
public class PingPongImages {
    
    private final Image[] images = new Image[2];
    private Texture[] textures;
    private TextureArray texArray;
    private boolean readWrite = true;
    
    public PingPongImages(Context context, MemoryAccess access, Image.ImageFormat format, Image.ImageDescriptor descr) {
        images[0] = context.createImage(access, format, descr);
        images[1] = context.createImage(access, format, descr);
    }
    public PingPongImages(int width, int height, Format format) {
        textures = new Texture[] {
            new Texture2D(width, height, format),
            new Texture2D(width, height, format)
        };
    }
    public PingPongImages(Image img1, Image img2) {
        images[0] = img1;
        images[1] = img2;
    }
    public PingPongImages(Texture tex1, Texture tex2) {
        textures = new Texture[] {tex1, tex2};
    }
    
    public void bind(Context context) {
        if (textures != null) {
            images[0] = context.bindImage(textures[0], MemoryAccess.READ_WRITE);
            images[1] = context.bindImage(textures[1], MemoryAccess.READ_WRITE);
        }
    }
    public void acquireResources(CommandQueue queue) {
        if (textures != null) {
            images[0].acquireImageForSharingNoEvent(queue);
            images[1].acquireImageForSharingNoEvent(queue);
        }
    }
    public void releaseResources(CommandQueue queue) {
        if (textures != null) {
            images[0].releaseImageForSharingNoEvent(queue);
            images[1].releaseImageForSharingNoEvent(queue);
        }
    }
    public int assignToKernelArgs(Kernel kernel, int index, boolean readImgFirst) {
        kernel.setArg(index, getFirstImage(readImgFirst));
        kernel.setArg(index+1, getSecondImage(readImgFirst));
        return index+2;
    }
    public void assignToKernelArgs(Kernel kernel, int readIndex, int writeIndex) {
        kernel.setArg(readIndex, getReadImage());
        kernel.setArg(writeIndex, getWriteImage());
    }    
    public void flipReadWrite() {
        readWrite = !readWrite;
    }
    
    public void setTexturesToMaterial(Material mat, String param1, String param2) {
        if (textures == null) return;
        mat.setTexture(param1, textures[0]);
        mat.setTexture(param2, textures[1]);
    }
    public void setTextureArrayToMaterial(Material mat, String param) {
        if (textures == null) return;
        if (texArray == null) {
            constructTextureArray();
        }
        mat.setTexture(param, texArray);
    }
    public void setReadIndexToMaterial(Material mat, String param) {
        mat.setInt(param, getReadIndex());
    }
    public void setWriteIndexToMaterial(Material mat, String param) {
        mat.setInt(param, getWriteIndex());
    }
    public void setReadWriteValueToMaterial(Material mat, String param) {
        mat.setBoolean(param, readWrite);
    }
    
    private void constructTextureArray() {
        texArray = new TextureArray(Arrays.asList(textures[0].getImage(), textures[1].getImage()));
    }
    
    public Image getImage(int i) {
        return images[i];
    }
    public Image getReadImage() {
        return images[getReadIndex()];
    }
    public Image getWriteImage() {
        return images[getWriteIndex()];
    }
    public Image getFirstImage(boolean readImgFirst) {
        if (readImgFirst) return getReadImage();
        else return getWriteImage();
    }
    public Image getSecondImage(boolean readImgFirst) {
        if (readImgFirst) return getWriteImage();
        else return getReadImage();
    }
    public Texture getTexture(int i) {
        if (textures == null) return null;
        return textures[i];
    }
    public Texture getReadTexture() {
        if (textures == null) return null;
        return textures[getReadIndex()];
    }
    public Texture getWriteTexture() {
        if (textures == null) return null;
        return textures[getWriteIndex()];
    }
    public Texture getFirstTexture(boolean readTexFirst) {
        if (readTexFirst) return getReadTexture();
        else return getWriteTexture();
    }
    public Texture getSecondTexture(boolean readTexFirst) {
        if (readTexFirst) return getWriteTexture();
        else return getReadTexture();
    }
    public int getReadIndex() {
        return readWrite ? 0 : 1;
    }
    public int getWriteIndex() {
        return readWrite ? 1 : 0;
    }
    
    public Image[] getImages() {
        return images;
    }
    public Texture[] getTextures() {
        return textures;
    }
    public boolean getReadWriteValue() {
        return readWrite;
    }
    
}
