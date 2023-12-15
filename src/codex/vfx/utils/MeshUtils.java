/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.utils;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
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
    
    private static final Quaternion tempQuat = new Quaternion();
    private static final Matrix3f tempMat3 = new Matrix3f();
    private static final Matrix4f tempMat4 = new Matrix4f();
    
    public static VertexBuffer initializeVertexBuffer(Mesh mesh, Type type, Usage usage, Format format, Buffer data, int components) {
        VertexBuffer buf = mesh.getBuffer(type);
        if (buf != null) {
            buf.updateData(data);
        } else {
            buf = new VertexBuffer(type);
            buf.setupData(usage, components, format, data);
            mesh.setBuffer(buf);
        }
        return buf;
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
    
    public static void writeVector4(FloatBuffer fb, Vector4f vector) {
        fb.put(vector.x).put(vector.y).put(vector.z).put(vector.w);
    }
    
    public static void writeMatrix4(FloatBuffer fb, Matrix4f matrix, boolean rowMajor) {
        float[] mat = new float[16];
        matrix.get(mat, rowMajor);
        fb.put(mat);
    }
    
    public static void writeTransformMatrix(FloatBuffer fb, Matrix4f matrix) {
        matrix.toRotationMatrix(tempMat3);
        tempMat3.invertLocal();
        tempQuat.fromRotationMatrix(tempMat3);
        fb.put(matrix.m00);
        fb.put(matrix.m10);
        fb.put(matrix.m20);
        fb.put(tempQuat.getX());
        fb.put(matrix.m01);
        fb.put(matrix.m11);
        fb.put(matrix.m21);
        fb.put(tempQuat.getY());
        fb.put(matrix.m02);
        fb.put(matrix.m12);
        fb.put(matrix.m22);
        fb.put(tempQuat.getZ());
        fb.put(matrix.m03);
        fb.put(matrix.m13);
        fb.put(matrix.m23);
        fb.put(tempQuat.getW());
    }
    
    public static void writeColor(FloatBuffer fb, ColorRGBA color) {
        fb.put(color.r).put(color.g).put(color.b).put(color.a);
    }
    
}