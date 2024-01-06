/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.mesh;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 *
 * @author codex
 */
public class Quad extends Mesh {    
    
    private static final float[] uvs = {1, 1, 1, 0, 0, 0, 0, 1};
    private static final int[] index = {0, 2, 1, 0, 3, 2};
    
    public Quad(Vector3f normal, Vector3f up, float width, float height, float originX, float originY) {
        Vector3f across = normal.cross(up);
        Vector3f climb = normal.cross(across);
        Vector3f[] verts = {
            across.mult(-width*originX).addLocal(climb.mult(-height*originY)),
            across.mult(-width*originX).addLocal(climb.mult(height*originY)),
            across.mult(width*originX).addLocal(climb.mult(height*originY)),
            across.mult(width*originX).addLocal(climb.mult(-height*originY)),
        };
        Vector3f[] normals = {normal, normal, normal, normal};
        setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verts));
        setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(uvs));
        setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(index));
        updateCounts();
        updateBound();
    }
    
}
