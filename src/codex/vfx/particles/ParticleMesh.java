/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles;

import com.jme3.scene.Mesh;

/**
 *
 * @author codex
 */
public abstract class ParticleMesh extends Mesh {
    
    public abstract void initBuffers();
    public abstract void updateMesh();
    
}
