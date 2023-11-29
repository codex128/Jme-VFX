/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.utils.MeshUtils;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author codex
 */
public class TrailingMesh extends Mesh {
    
    private int capacity;
    private int size = -1;
    
    public void initBuffers(ParticleGroup group) {
        
        capacity = group.capacity();
        
        // position buffer
        FloatBuffer pb = BufferUtils.createVector3Buffer(capacity * 2);
        VertexBuffer buf = getBuffer(VertexBuffer.Type.Position);
        if (buf != null) {
            buf.updateData(pb);
        } else {
            VertexBuffer pvb = new VertexBuffer(VertexBuffer.Type.Position);
            pvb.setupData(VertexBuffer.Usage.Stream, 3, VertexBuffer.Format.Float, pb);
            setBuffer(pvb);
        }
        
        // color buffer
        ByteBuffer cb = BufferUtils.createByteBuffer(capacity * 2 * 4);
        buf = getBuffer(VertexBuffer.Type.Color);
        if (buf != null) {
            buf.updateData(cb);
        } else {
            VertexBuffer cvb = new VertexBuffer(VertexBuffer.Type.Color);
            cvb.setupData(VertexBuffer.Usage.Stream, 4, VertexBuffer.Format.UnsignedByte, cb);
            cvb.setNormalized(true);
            setBuffer(cvb);
        }
        
        // buffer for storing rotational axis used for hardware normals
        FloatBuffer ab = BufferUtils.createFloatBuffer(capacity * 2 * 4);
        buf = getBuffer(VertexBuffer.Type.TexCoord3);
        if (buf != null) {
            buf.updateData(ab);
        } else {
            VertexBuffer avb = new VertexBuffer(VertexBuffer.Type.TexCoord3);
            avb.setupData(VertexBuffer.Usage.Stream, 4, VertexBuffer.Format.Float, ab);
            setBuffer(avb);
        }
        
        // buffer for general vertex info
        FloatBuffer tb2 = BufferUtils.createFloatBuffer(capacity * 2);
        buf = getBuffer(VertexBuffer.Type.TexCoord2);
        if (buf != null) {
            buf.updateData(tb2);
        } else {
            VertexBuffer tvb = new VertexBuffer(VertexBuffer.Type.TexCoord2);
            tvb.setupData(VertexBuffer.Usage.Stream, 1, VertexBuffer.Format.Float, tb2);
            setBuffer(tvb);
        }
        
        // main buffer for texture coordinates
        FloatBuffer tb = BufferUtils.createVector2Buffer(capacity * 2);
        float uvx = 0, incrUv = 1f/group.capacity();
        for (int i = 0; i < group.capacity(); i++) {
            tb.put(uvx).put(0f)
              .put(uvx).put(1f);
            uvx += incrUv;
        }
        tb.flip();
        buf = getBuffer(VertexBuffer.Type.TexCoord);
        if (buf != null) {
            buf.updateData(tb);
        } else {
            VertexBuffer tvb = new VertexBuffer(VertexBuffer.Type.TexCoord);
            tvb.setupData(VertexBuffer.Usage.Static, 2, VertexBuffer.Format.Float, tb);
            setBuffer(tvb);
        }
        
        // index buffer
        ShortBuffer ib = BufferUtils.createShortBuffer((capacity-1) * 6);
        for (int i = 0, j = 0; i < group.capacity()-1; i++) {
            MeshUtils.writeTriangle(ib, j  , j+1, j+2);
            MeshUtils.writeTriangle(ib, j+3, j+2, j+1);
            j += 2;
        }
        ib.flip();
        buf = getBuffer(VertexBuffer.Type.Index);
        if (buf != null) {
            buf.updateData(ib);
        } else {
            VertexBuffer ivb = new VertexBuffer(VertexBuffer.Type.Index);
            ivb.setupData(VertexBuffer.Usage.Dynamic, 3, VertexBuffer.Format.UnsignedShort, ib);
            setBuffer(ivb);
        }
        
    }
    public void updateMesh(ParticleGroup group, boolean faceCamera) {
        
        if (group.capacity() != capacity) {
            initBuffers(group);
        }
        
        VertexBuffer pvb = getBuffer(VertexBuffer.Type.Position);
        FloatBuffer positions = (FloatBuffer)pvb.getData();        
        VertexBuffer cvb = getBuffer(VertexBuffer.Type.Color);
        ByteBuffer colors = (ByteBuffer)cvb.getData();        
        VertexBuffer tvb = getBuffer(VertexBuffer.Type.TexCoord2);
        FloatBuffer info = (FloatBuffer)tvb.getData();        
        VertexBuffer avb = getBuffer(VertexBuffer.Type.TexCoord3);
        FloatBuffer axis = (FloatBuffer)avb.getData();
        
        // initialize buffers to be written (does not actually clear the buffer)
        positions.clear();
        colors.clear();
        info.clear();
        axis.clear();        
        
        //texCoords.limit(group.size() * 2 * 2);
        //texCoords.position(0);
        
        float life = 0, lifeIncr = 1f/group.size();
        for (int i = 0; group.size() >= 2 && i < group.size(); i++) {
            ParticleData data = group.get(i);
            // configure the rotation axis
            Vector3f rotAxis;
            if (i+1 >= group.size()) {
                // axis away from previous position
                rotAxis = group.get(i-1).position.subtract(data.position).normalizeLocal().negateLocal();
            } else if (i == 0) {
                // axis towards next position
                rotAxis = group.get(i+1).position.subtract(data.position).normalizeLocal();
            } else {
                // axis as average between direction to next (forward) and previous (negated)
                Vector3f toNext = group.get(i+1).position.subtract(data.position).normalizeLocal();
                Vector3f toPrev = group.get(i-1).position.subtract(data.position).normalizeLocal();
                rotAxis = toNext.add(toPrev.negateLocal()).normalizeLocal();
            }
            if (faceCamera) {
                // hardware normals
                writeVectorToBuffer(positions, data.position);
                writeVectorToBuffer(positions, data.position);
                writeVectorToBuffer(axis, buildVector4f(rotAxis, data.scale));
                writeVectorToBuffer(axis, buildVector4f(rotAxis, -data.scale));
            } else {
                // software normals
                Vector3f across = rotAxis.cross(data.normal).normalizeLocal().multLocal(data.scale);
                Vector3f neg = across.negate().addLocal(data.position);
                across.addLocal(data.position);
                writeVectorToBuffer(positions, across);
                writeVectorToBuffer(positions, neg);
                writeVectorToBuffer(axis, Vector4f.ZERO);
                writeVectorToBuffer(axis, Vector4f.ZERO);
            }
            info.put(life).put(life);
            life += lifeIncr;
            int abgr = data.color.asIntABGR();
            colors.putInt(abgr);
            colors.putInt(abgr);
        }
        
        positions.flip();
        colors.flip();
        info.flip();
        axis.flip();
        
        // update vertex buffers
        pvb.updateData(positions);
        cvb.updateData(colors);
        tvb.updateData(info);
        avb.updateData(axis);
        
        if (group.size() != size) {        
            VertexBuffer ivb = getBuffer(VertexBuffer.Type.Index);
            ShortBuffer index = (ShortBuffer)ivb.getData();
            index.position(0);
            index.limit(Math.max(group.size()-1, 0) * 6);
            ivb.updateData(index);
            size = group.size();
        }
        
        updateCounts();
        updateBound();
        
    }
    
    private void writeVectorToBuffer(FloatBuffer fb, Vector3f vector) {
        fb.put(vector.x).put(vector.y).put(vector.z);
    }
    private void writeVectorToBuffer(FloatBuffer fb, Vector4f vector) {
        fb.put(vector.x).put(vector.y).put(vector.z).put(vector.w);
    }
    private Vector4f buildVector4f(Vector3f vec, float w) {
        return new Vector4f(vec.x, vec.y, vec.z, w);
    }
    
}
