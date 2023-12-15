/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.mesh.MeshPrototype;
import codex.vfx.utils.MeshUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Represents particle data as a mesh.
 * 
 * @author codex
 */
public class ParticleTriMesh extends Mesh {
    
    private int capacity;
    private int size = -1;
    
    public void initBuffers(ParticleGroup group, MeshPrototype prototype) {
        
        capacity = group.capacity();
        
        // Position buffer, stores the origin of the particle for each vertex.
        FloatBuffer pb = BufferUtils.createVector3Buffer(capacity * prototype.getNumVerts());
        MeshUtils.initializeVertexBuffer(this, Type.Position, Usage.Stream, Format.Float, pb, 3);
        
        // Local position buffer, sprite index packed into z component
        FloatBuffer pb2 = BufferUtils.createVector3Buffer(capacity * prototype.getNumVerts());
        MeshUtils.initializeVertexBuffer(this, Type.TexCoord2, Usage.Stream, Format.Float, pb2, 3);
        
        // Main texture coordinate buffer, does not change.
        FloatBuffer tb = BufferUtils.createVector2Buffer(capacity * prototype.getNumVerts());
        for (int i = 0; i < group.capacity(); i++) {
            for (Vector2f c : prototype.getTexCoords()) {
                MeshUtils.writeVector2(tb, c);
            }
        }
        tb.flip();
        MeshUtils.initializeVertexBuffer(this, Type.TexCoord, Usage.Static, Format.Float, tb, 2);
        
        // Color buffer, including alpha.
        FloatBuffer cb = BufferUtils.createFloatBuffer(capacity * prototype.getNumVerts() * 4);
        MeshUtils.initializeVertexBuffer(this, Type.Color, Usage.Stream, Format.Float, cb, 4);
        
        // Index buffer, changes as the number of particles changes -> dynamic usage
        ShortBuffer ib = BufferUtils.createShortBuffer(capacity * prototype.getNumIndices());
        for (int i = 0, vi = 0; i < group.capacity(); i++) {
            for (int j : prototype.getIndices()) {
                ib.put((short)(vi+j));
            }
            vi += prototype.getNumVerts();
        }
        ib.position(0);
        ib.limit(group.size() * prototype.getNumIndices());
        MeshUtils.initializeVertexBuffer(this, Type.Index, Usage.Dynamic, Format.UnsignedShort, ib, 3);
        
        updateCounts();
        
    }
    public void updateMesh(ParticleGroup<ParticleData> group, MeshPrototype prototype) {
        if (capacity != group.capacity()) {
            initBuffers(group, prototype);
        }
        VertexBuffer pBuf = getBuffer(Type.Position);
        FloatBuffer positions = (FloatBuffer)pBuf.getData();        
        VertexBuffer lBuf = getBuffer(Type.TexCoord2);
        FloatBuffer localPos = (FloatBuffer)lBuf.getData();
        VertexBuffer cBuf = getBuffer(Type.Color);
        FloatBuffer colors = (FloatBuffer)cBuf.getData();    
        positions.clear();
        localPos.clear();
        colors.clear();
        Vector3f vec = new Vector3f();
        Quaternion q = new Quaternion();
        for (ParticleData p : group) {
            q.fromAngles(0f, 0f, p.angle.get());
            for (Vector3f v : prototype.getVerts()) {
                q.mult(v, vec).multLocal(p.size.get());
                MeshUtils.writeVector3(positions, p.getPosition());
                localPos.put(vec.x).put(vec.y).put(p.spriteIndex.get());
                ColorRGBA c = p.color.get();
                colors.put(c.r).put(c.g).put(c.b).put(c.a);
            }
        }        
        positions.flip();
        localPos.flip();
        colors.flip();
        pBuf.updateData(positions);
        lBuf.updateData(localPos);
        cBuf.updateData(colors);
        if (size != group.size()) {
            VertexBuffer iBuf = getBuffer(Type.Index);
            ShortBuffer index = (ShortBuffer)iBuf.getData();
            index.position(0);
            index.limit(group.size() * prototype.getNumIndices());
            iBuf.updateData(index);
            size = group.size();
            updateCounts();
        }
    }
    
}
