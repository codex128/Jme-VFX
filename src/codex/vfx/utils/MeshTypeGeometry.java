/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.utils;

import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

/**
 * Geometry that requires a certain type of mesh.
 * 
 * @author codex
 * @param <T> required mesh type
 */
public abstract class MeshTypeGeometry <T extends Mesh> extends Geometry {
    
    protected final Class<T> requiredMeshType;
    protected T tMesh;

    public MeshTypeGeometry(Class<T> requiredMeshType) {
        super();
        this.requiredMeshType = requiredMeshType;
    }
    
    @Override
    public void setMesh(Mesh mesh) {
        if (!requiredMeshType.isAssignableFrom(mesh.getClass())) {
            throw new IllegalArgumentException("Requires "+requiredMeshType.getSimpleName()+" as mesh.");
        }
        super.setMesh(mesh);
        tMesh = (T)mesh;
    }
    
    protected void setTypeMesh(T mesh) {
        setMesh(mesh);
        tMesh = mesh;
    }
    
    @Override
    public T getMesh() {
        return tMesh;
    }
    
}
