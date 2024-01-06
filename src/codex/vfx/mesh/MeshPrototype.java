/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * Defines how a section of mesh is constructed.
 * <p>
 * Used primarily for defining particle shapes.
 * 
 * @author codex
 */
public class MeshPrototype {
    
    /**
     * Prototype of a 1x1 quad consisting of two triangles.
     */
    public static final MeshPrototype QUAD = new MeshPrototype().setVerts(
        new Vector3f(-1, 1, 0),
        new Vector3f(1, 1, 0),
        new Vector3f(1, -1, 0),
        new Vector3f(-1, -1, 0)
    ).setTexCoords(
        new Vector2f(0, 0),
        new Vector2f(1, 0),
        new Vector2f(1, 1),
        new Vector2f(0, 1)
    ).setIndices(0, 2, 1, 0, 3, 2);
    
    private Vector3f[] verts;
    private Vector2f[] texCoords;
    private int[] indices;

    public MeshPrototype() {}
    public MeshPrototype(Vector3f[] verts, Vector2f[] texCoords, int[] indices) {
        setVerts(verts);
        setTexCoords(texCoords);
        setIndices(indices);
    }

    public final MeshPrototype setVerts(Vector3f... verts) {
        if (this.verts != null && verts.length != this.verts.length) {
            throw new IllegalArgumentException("Number of vertices must remain constant.");
        }
        this.verts = verts;
        if (texCoords != null && this.verts.length != texCoords.length) {
            throw new IllegalArgumentException("Number of vertices must match number of texture coordinates.");
        }
        return this;
    }
    public final MeshPrototype setTexCoords(Vector2f... texCoords) {
        if (this.texCoords != null && texCoords.length != this.texCoords.length) {
            throw new IllegalArgumentException("Number of texture coordinates must remain constant.");
        }
        this.texCoords = texCoords;
        if (verts != null && verts.length != this.texCoords.length) {
            throw new IllegalArgumentException("Number of vertices must match number of texture coordinates.");
        }
        return this;
    }
    public final MeshPrototype setIndices(int... indices) {
        if (this.indices != null && indices.length != this.indices.length) {
            throw new IllegalArgumentException("Number of indices must remain constant.");
        }
        this.indices = indices;
        return this;
    }

    public Vector3f[] getVerts() {
        return verts;
    }
    public Vector2f[] getTexCoords() {
        return texCoords;
    }
    public int[] getIndices() {
        return indices;
    }
    
    public int getNumVerts() {
        return verts.length;
    }
    public int getNumIndices() {
        return indices.length;
    }
    
}
