/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.utils.MeshUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix4f;
import com.jme3.math.Transform;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

/**
 *
 * @author codex
 */
public class InstancedParticleGeometry extends Geometry {
    
    private ParticleGroup<ParticleData> group;
    private final Transform transform = new Transform();
    private int capacity = -1;
    
    
    public InstancedParticleGeometry(ParticleGroup group, Mesh mesh) {
        super();
        this.group = group;
        super.setMesh(mesh);
        setCullHint(Spatial.CullHint.Never);
        setIgnoreTransform(true);
    }
    
    private void initBuffers() {  
        capacity = group.capacity();
        FloatBuffer ib = BufferUtils.createFloatBuffer(capacity * 16);
        VertexBuffer ivb = MeshUtils.initializeVertexBuffer(mesh,
            VertexBuffer.Type.InstanceData,
            VertexBuffer.Usage.Stream,
            VertexBuffer.Format.Float,
            ib, 16
        );
        ivb.setInstanced(true);
        FloatBuffer cb = BufferUtils.createFloatBuffer(capacity * 4);
        VertexBuffer cvb = MeshUtils.initializeVertexBuffer(mesh,
            VertexBuffer.Type.Color,
            VertexBuffer.Usage.Stream,
            VertexBuffer.Format.Float,
            cb, 4
        );
        cvb.setInstanced(true);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        if (capacity != group.capacity()) {
            initBuffers();
        }
        VertexBuffer ivb = mesh.getBuffer(VertexBuffer.Type.InstanceData);
        FloatBuffer instances = (FloatBuffer)ivb.getData();
        instances.clear();
        VertexBuffer cvb = mesh.getBuffer(VertexBuffer.Type.Color);
        FloatBuffer colors = (FloatBuffer)cvb.getData();
        colors.clear();
        if (!group.isEmpty()) {
            for (ParticleData p : group) {
                transform.set(p.transform);
                transform.getScale().multLocal(p.size.get());
                MeshUtils.writeTransformMatrix(instances, transform.toTransformMatrix());
                MeshUtils.writeColor(colors, p.color.get());
            }
        } else {
            MeshUtils.writeMatrix4(instances, Matrix4f.IDENTITY, false);
            MeshUtils.writeColor(colors, ColorRGBA.BlackNoAlpha);
        }
        instances.flip();
        ivb.updateData(instances);
        colors.flip();
        cvb.updateData(colors);
        mesh.updateCounts();
        mesh.updateBound();
    }
    
    public void setParticleGroup(ParticleGroup group) {
        this.group = group;
    }
    
}
