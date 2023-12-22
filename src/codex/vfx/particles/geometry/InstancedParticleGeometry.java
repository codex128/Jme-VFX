/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.geometry;

import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.particles.geometry.ParticleGeometry;
import codex.vfx.utils.MeshUtils;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix4f;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

/**
 * Displays particles as instanced meshes.
 * 
 * @author codex
 */
public class InstancedParticleGeometry extends ParticleGeometry<ParticleData> {
    
    private final Transform transform = new Transform();
    private final boolean writeColors;
    
    public InstancedParticleGeometry(ParticleGroup group, Mesh mesh) {
        this(group, mesh, true);
    }
    public InstancedParticleGeometry(ParticleGroup group, Mesh mesh, boolean writeColors) {
        super(group);
        setMesh(mesh);
        this.writeColors = writeColors;
    }
    
    @Override
    protected void initBuffers() {
        FloatBuffer ib = BufferUtils.createFloatBuffer(capacity * 16);
        VertexBuffer ivb = MeshUtils.initializeVertexBuffer(mesh,
            VertexBuffer.Type.InstanceData,
            VertexBuffer.Usage.Stream,
            VertexBuffer.Format.Float,
            ib, 16
        );
        ivb.setInstanced(true);
        if (writeColors) {
            FloatBuffer cb = BufferUtils.createFloatBuffer(capacity * 4);
            VertexBuffer cvb = MeshUtils.initializeVertexBuffer(mesh,
                VertexBuffer.Type.Color,
                VertexBuffer.Usage.Stream,
                VertexBuffer.Format.Float,
                cb, 4
            );
            cvb.setInstanced(true);
        }
    }
    @Override
    protected void updateMesh() {
        VertexBuffer ivb = mesh.getBuffer(VertexBuffer.Type.InstanceData);
        FloatBuffer instances = (FloatBuffer)ivb.getData();
        instances.clear();
        VertexBuffer cvb = mesh.getBuffer(VertexBuffer.Type.Color);
        FloatBuffer colors = (FloatBuffer)cvb.getData();
        if (writeColors) {
            colors.clear();
        }
        if (!group.isEmpty()) {
            for (ParticleData p : group) {
                transform.set(p.transform);
                transform.getScale().multLocal(p.size.get());
                MeshUtils.writeTransformMatrix(instances, transform.toTransformMatrix());
                if (writeColors) {
                    MeshUtils.writeColor(colors, p.color.get());
                }
            }
        } else {
            MeshUtils.writeMatrix4(instances, Matrix4f.IDENTITY, false);
            if (writeColors) {
                MeshUtils.writeColor(colors, ColorRGBA.BlackNoAlpha);
            }
        }
        instances.flip();
        ivb.updateData(instances);
        if (writeColors) {
            colors.flip();
            cvb.updateData(colors);
        }
        mesh.updateCounts();
        mesh.updateBound();
    }
    @Override
    public void updateWorldBound() {
        super.updateWorldBound();
        worldBound = new BoundingSphere(Float.POSITIVE_INFINITY, Vector3f.ZERO);
    }
    
    /**
     * Returns true if this geometry writes particle color to the color buffer,
     * which will override existing vertex colors.
     * <p>
     * This property can only be set by
     * {@link #InstancedParticleGeometry(codex.vfx.particles.ParticleGroup, com.jme3.scene.Mesh, boolean)}
     * 
     * @return 
     */
    public boolean isWriteColors() {
        return writeColors;
    }
    
}
