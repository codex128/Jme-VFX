/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.opencl;

import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Context;
import com.jme3.opencl.Image;
import com.jme3.opencl.MemoryAccess;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;

/**
 *
 * @author codex
 */
public class BindImage {
    
    private Image image;
    private final Texture2D texture;
    
    public BindImage(Texture2D texture) {
        this.texture = texture;
    }
    public BindImage(int width, int height, Format format) {
        this.texture = new Texture2D(width, height, format);
    }

    public void bind(Context context, MemoryAccess access) {
        if (image == null) {
            image = context.bindImage(texture, access);
        }
    }
    public void acquireResources(CommandQueue queue) {
        image.acquireImageForSharingNoEvent(queue);
    }
    public void releaseResources(CommandQueue queue) {
        image.releaseImageForSharingNoEvent(queue);
    }
    
    public Image getImage() {
        return image;
    }
    public Texture2D getTexture() {
        return texture;
    }
    
}
