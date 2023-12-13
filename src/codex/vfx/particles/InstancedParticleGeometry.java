/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import codex.vfx.utils.MeshUtils;
import com.jme3.math.Matrix4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

/**
 *
 * @author codex
 */
public class InstancedParticleGeometry extends Geometry {
    
    private ParticleGroup<ParticleData> group;
    private final Matrix4f tMat = new Matrix4f();
    private int capacity = -1;
    
    public InstancedParticleGeometry(ParticleGroup group, Mesh mesh) {
        super();
        this.group = group;
        super.setMesh(mesh);
        //initBuffers();
        setIgnoreTransform(true);
    }
    
    private void initBuffers() {  
        capacity = group.capacity();
        FloatBuffer ib = BufferUtils.createFloatBuffer(capacity * 16);
        VertexBuffer vb = MeshUtils.initializeVertexBuffer(mesh,
            VertexBuffer.Type.InstanceData,
            VertexBuffer.Usage.Stream,
            VertexBuffer.Format.Float,
            ib, 16
        );
        vb.setInstanced(true);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        if (capacity != group.capacity()) {
            initBuffers();
        }
        VertexBuffer ivb = mesh.getBuffer(VertexBuffer.Type.InstanceData);
        FloatBuffer instances = (FloatBuffer)ivb.getData();        
        instances.clear();
        if (!group.isEmpty()) {
            for (ParticleData p : group) {
                MeshUtils.writeMatrix4(instances, p.transform.toTransformMatrix(), false);
            }
        } else {
            MeshUtils.writeMatrix4(instances, Matrix4f.IDENTITY, false);
        }
        instances.flip();
        ivb.updateData(instances);
        mesh.updateCounts();
        mesh.updateBound();
    }
    
    public void setParticleGroup(ParticleGroup group) {
        this.group = group;
    }
    
}
