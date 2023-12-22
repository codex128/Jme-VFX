/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.geometry;

import codex.vfx.particles.geometry.ParticleGeometry;
import codex.boost.control.GeometryControl;
import codex.vfx.particles.ParticleData;
import codex.vfx.particles.ParticleGroup;
import codex.vfx.utils.MeshUtils;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author codex
 */
public class PointParticleGeometry extends ParticleGeometry<ParticleData> {
        
    public PointParticleGeometry(ParticleGroup group)  {
        super(group);
        mesh.setMode(Mesh.Mode.Points);
        setCullHint(Spatial.CullHint.Never);
        addControl(new MaterialQuadratic());
    }
    
    @Override
    protected void initBuffers() {
        
        FloatBuffer pb = BufferUtils.createVector3Buffer(capacity);
        MeshUtils.initializeVertexBuffer(mesh, Type.Position, Usage.Stream, Format.Float, pb, 3);
        
        FloatBuffer cb = BufferUtils.createFloatBuffer(capacity * 4);
        MeshUtils.initializeVertexBuffer(mesh, Type.Color, Usage.Stream, Format.Float, cb, 4);
        
        FloatBuffer sb = BufferUtils.createFloatBuffer(capacity);
        MeshUtils.initializeVertexBuffer(mesh, Type.Size, Usage.Stream, Format.Float, sb, 1);
        
        FloatBuffer rb = BufferUtils.createFloatBuffer(capacity);
        for (int i = 0; i < capacity; i++) {
            rb.put(0);
        }
        rb.flip();
        MeshUtils.initializeVertexBuffer(mesh, Type.TexCoord4, Usage.Stream, Format.Float, rb, 1);
        
        ShortBuffer ib = BufferUtils.createShortBuffer(capacity);
        for (int i = 0; i < capacity; i++) {
            ib.put((short)0);
        }
        ib.flip();
        MeshUtils.initializeVertexBuffer(mesh, Type.TexCoord3, Usage.Dynamic, Format.UnsignedShort, ib, 1);
        
        mesh.updateCounts();
        
    }
    @Override
    protected void updateMesh() {
        if (group.isEmpty()) {
            return;
        }
        VertexBuffer pBuf = mesh.getBuffer(Type.Position);
        FloatBuffer positions = (FloatBuffer)pBuf.getData();
        VertexBuffer cBuf = mesh.getBuffer(Type.Color);
        FloatBuffer colors = (FloatBuffer)cBuf.getData();
        VertexBuffer sBuf = mesh.getBuffer(Type.Size);
        FloatBuffer sizes = (FloatBuffer)sBuf.getData();
        VertexBuffer rBuf = mesh.getBuffer(Type.TexCoord4);
        FloatBuffer rotations = (FloatBuffer)rBuf.getData();
        VertexBuffer iBuf = mesh.getBuffer(Type.TexCoord3);
        ShortBuffer spriteIndex = (ShortBuffer)iBuf.getData();
        positions.clear();
        colors.clear();
        rotations.clear();
        sizes.clear();
        if (useSpriteSheet) {
            spriteIndex.clear();
        }
        for (ParticleData p : group) {
            MeshUtils.writeVector3(positions, p.getPosition());
            MeshUtils.writeColor(colors, p.color.get());
            sizes.put(p.size.get()*vectorComponentAverage(p.getScale()));
            rotations.put(p.angle.get());
            if (useSpriteSheet) {
                spriteIndex.put(p.spriteIndex.get().shortValue());
            }
        }
        positions.flip();
        colors.flip();
        rotations.flip();
        sizes.flip();
        pBuf.updateData(positions);
        cBuf.updateData(colors);
        rBuf.updateData(rotations);
        sBuf.updateData(sizes);
        if (useSpriteSheet) {
            spriteIndex.flip();
            iBuf.updateData(spriteIndex);
        }
    }
    
    private float vectorComponentAverage(Vector3f vec) {
        return (vec.x + vec.y + vec.z) / 3;
    }
    
    /**
     * Updates the material quadratic.
     */
    private static final class MaterialQuadratic extends GeometryControl {
        
        private static final String QUADRATIC = "Quadratic";
        
        @Override
        protected void controlUpdate(float tpf) {}
        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
            if (geometry.getMaterial().getParam(QUADRATIC) != null) {
                float c = vp.getCamera().getProjectionMatrix().m00 * vp.getCamera().getWidth() * .5f;
                //System.out.println("quadratic="+c);
                geometry.getMaterial().setFloat(QUADRATIC, c);
            }
        }        
    }
    
}
