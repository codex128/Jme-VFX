/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.utils.MeshUtils;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 *
 * @author codex
 */
public class PointParticleGeometry extends ParticleGeometry<ParticleData> {
    
    private ParticleGroup<ParticleData> group;
    private boolean useSpriteSheet = false;
    
    public PointParticleGeometry(ParticleGroup group)  {
        super(group);
        mesh.setMode(Mesh.Mode.Points);
        setCullHint(Spatial.CullHint.Never);
    }
     
    @Override
    protected void initBuffers() {
        
        FloatBuffer pb = BufferUtils.createVector3Buffer(capacity);
        MeshUtils.initializeVertexBuffer(mesh, Type.Position, Usage.Stream, Format.Float, pb, 3);
        
        FloatBuffer cb = BufferUtils.createFloatBuffer(capacity * 4);
        MeshUtils.initializeVertexBuffer(mesh, Type.Color, Usage.Stream, Format.Float, cb, 4);
        
        ByteBuffer sb = BufferUtils.createByteBuffer(capacity);
        for (int i = 0; i < capacity; i++) {
            sb.put((byte)0);
        }
        sb.flip();
        MeshUtils.initializeVertexBuffer(mesh, Type.TexCoord3, Usage.Dynamic, Format.UnsignedByte, sb, 1);
        
        mesh.updateCounts();
        
    }
    @Override
    protected void updateMesh() {
        VertexBuffer pBuf = mesh.getBuffer(Type.Position);
        FloatBuffer positions = (FloatBuffer)pBuf.getData();
        VertexBuffer cBuf = mesh.getBuffer(Type.Color);
        FloatBuffer colors = (FloatBuffer)cBuf.getData();
        VertexBuffer sBuf = mesh.getBuffer(Type.TexCoord3);
        ByteBuffer spriteIndex = (ByteBuffer)sBuf.getData();
        positions.clear();
        colors.clear();
        if (useSpriteSheet) {
            spriteIndex.clear();
        }
        for (ParticleData p : group) {
            MeshUtils.writeVector3(positions, p.getPosition());
            MeshUtils.writeColor(colors, p.color.get());
            if (useSpriteSheet) {
                spriteIndex.put(p.spriteIndex.get());
            }
        }
        positions.flip();
        colors.flip();
        pBuf.updateData(positions);
        cBuf.updateData(colors);
        if (useSpriteSheet) {
            spriteIndex.flip();
            sBuf.updateData(spriteIndex);
        }
    }
    
}
