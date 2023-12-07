/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.utils;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author codex
 */
public class MeshUtils {
    
    public static void initializeVertexBuffer(Mesh mesh, Type type, Usage usage, Format format, Buffer data, int components) {
        VertexBuffer buf = mesh.getBuffer(type);
        if (buf != null) {
            buf.updateData(data);
        } else {
            buf = new VertexBuffer(type);
            buf.setupData(usage, components, format, data);
            mesh.setBuffer(buf);
        }
    }    
    
    public static void updateVertexBuffer(Mesh mesh, Type type) {
        VertexBuffer buf = mesh.getBuffer(type);
        buf.updateData(buf.getData());
    }
    
    public static void writeTriangle(ShortBuffer sb, int p1, int p2, int p3) {
        sb.put((short)p1).put((short)p2).put((short)p3);
    }
    
    public static void writeVector2(FloatBuffer fb, Vector2f vector) {
        fb.put(vector.x).put(vector.y);
    }
    
    public static void writeVector3(FloatBuffer fb, Vector3f vector) {
        fb.put(vector.x).put(vector.y).put(vector.z);
    }
    
}