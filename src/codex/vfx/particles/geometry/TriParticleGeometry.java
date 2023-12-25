/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.geometry;

import codex.vfx.particles.geometry.ParticleGeometry;
import codex.vfx.mesh.MeshPrototype;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.utils.MeshUtils;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author codex
 */
public class TriParticleGeometry extends ParticleGeometry<ParticleData> {
    
    private final MeshPrototype prototype;
    private int size = -1;
    
    public TriParticleGeometry(ParticleGroup group, MeshPrototype prototype) {
        super(group);
        this.prototype = prototype;
        setCullHint(Spatial.CullHint.Never);
    }
        
    @Override
    protected void initBuffers() {
        
        // Position buffer, stores the origin of the particle for each vertex.
        FloatBuffer pb = BufferUtils.createVector3Buffer(capacity * prototype.getNumVerts());
        MeshUtils.initializeVertexBuffer(mesh, VertexBuffer.Type.Position, VertexBuffer.Usage.Stream, VertexBuffer.Format.Float, pb, 3);
        
        // Local position buffer
        FloatBuffer pb2 = BufferUtils.createVector3Buffer(capacity * prototype.getNumVerts());
        MeshUtils.initializeVertexBuffer(mesh, VertexBuffer.Type.TexCoord2, VertexBuffer.Usage.Stream, VertexBuffer.Format.Float, pb2, 2);
        
        // Main texture coordinate buffer, does not change.
        FloatBuffer tb = BufferUtils.createVector2Buffer(capacity * prototype.getNumVerts());
        for (int i = 0; i < group.capacity(); i++) {
            for (Vector2f c : prototype.getTexCoords()) {
                MeshUtils.writeVector2(tb, c);
            }
        }
        tb.flip();
        MeshUtils.initializeVertexBuffer(mesh, VertexBuffer.Type.TexCoord, VertexBuffer.Usage.Static, VertexBuffer.Format.Float, tb, 2);
        
        // sprite factions buffer
        ShortBuffer sib = BufferUtils.createShortBuffer(capacity * prototype.getNumVerts());
        MeshUtils.initializeVertexBuffer(mesh, VertexBuffer.Type.TexCoord3, VertexBuffer.Usage.Stream, VertexBuffer.Format.UnsignedShort, sib, 1);
        
        // Color buffer, including alpha.
        FloatBuffer cb = BufferUtils.createFloatBuffer(capacity * prototype.getNumVerts() * 4);
        MeshUtils.initializeVertexBuffer(mesh, VertexBuffer.Type.Color, VertexBuffer.Usage.Stream, VertexBuffer.Format.Float, cb, 4);
        
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
        MeshUtils.initializeVertexBuffer(mesh, VertexBuffer.Type.Index, VertexBuffer.Usage.Dynamic, VertexBuffer.Format.UnsignedShort, ib, 3);
        
        mesh.updateCounts();
        
    }
    @Override
    protected void updateMesh() {
        if (capacity != group.capacity()) {
            initBuffers();
        }
        VertexBuffer pBuf = mesh.getBuffer(VertexBuffer.Type.Position);
        FloatBuffer positions = (FloatBuffer)pBuf.getData();        
        VertexBuffer lBuf = mesh.getBuffer(VertexBuffer.Type.TexCoord2);
        FloatBuffer localPos = (FloatBuffer)lBuf.getData();
        VertexBuffer siBuf = mesh.getBuffer(VertexBuffer.Type.TexCoord3);
        ShortBuffer spriteIndex = (ShortBuffer)siBuf.getData();
        VertexBuffer cBuf = mesh.getBuffer(VertexBuffer.Type.Color);
        FloatBuffer colors = (FloatBuffer)cBuf.getData();
        positions.clear();
        localPos.clear();
        colors.clear();
        if (useSpriteSheet) {
            spriteIndex.clear();
        }
        Vector3f vec = new Vector3f();
        Quaternion q = new Quaternion();
        for (ParticleData p : group) {
            q.fromAngles(0f, 0f, p.angle.get());
            for (Vector3f v : prototype.getVerts()) {
                q.mult(v, vec).multLocal(p.size.get()).multLocal(p.getScale());
                MeshUtils.writeVector3(positions, p.getPosition());
                localPos.put(vec.x).put(vec.y);
                MeshUtils.writeColor(colors, p.color.get());
                if (useSpriteSheet) {
                    spriteIndex.put(p.spriteIndex.get().shortValue());
                }
            }
        }
        positions.flip();
        localPos.flip();
        colors.flip();
        pBuf.updateData(positions);
        lBuf.updateData(localPos);
        cBuf.updateData(colors);
        if (useSpriteSheet) {
            spriteIndex.flip();
            siBuf.updateData(spriteIndex);
        }
        if (size != group.size()) {
            VertexBuffer iBuf = mesh.getBuffer(VertexBuffer.Type.Index);
            ShortBuffer index = (ShortBuffer)iBuf.getData();
            index.position(0);
            index.limit(group.size() * prototype.getNumIndices());
            iBuf.updateData(index);
            size = group.size();
            mesh.updateCounts();
        }
    }
    
}
