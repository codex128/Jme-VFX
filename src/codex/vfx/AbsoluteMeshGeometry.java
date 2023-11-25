/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx;

import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 * Bootlegged method for rendering a mesh transformed to {@link Transform#IDENTITY},
 * as opposed to the geometry's actual world transform.
 * 
 * @author codex
 */
public abstract class AbsoluteMeshGeometry extends Geometry {
    
    protected Transform wTransform = new Transform();
    
    @Override
    public void updateWorldTransforms() {
        // code copied from Geometry and Spatial
        if (parent == null) {
            wTransform.set(localTransform);
            refreshFlags &= ~RF_TRANSFORM;
        } else {
            // check if transform for parent is updated
            // don't have access to parent.refreshFlags
            //assert ((parent.refreshFlags & RF_TRANSFORM) == 0) : "Illegal rf transform update. Problem spatial name: " + getName();
            wTransform.set(localTransform);
            wTransform.combineWithParent(parent.getWorldTransform());
            refreshFlags &= ~RF_TRANSFORM;
        }
        worldTransform.set(Transform.IDENTITY);
        computeWorldMatrix();
        if (isGrouped()) {
            groupNode.onTransformChange(this);
        }
        worldLights.sort(true);
    }    
    @Override
    public Transform getWorldTransform() {
        return wTransform;
    }
    @Override
    public Vector3f getWorldTranslation() {
        return wTransform.getTranslation();
    }
    @Override
    public Quaternion getWorldRotation() {
        return wTransform.getRotation();
    }
    @Override
    public Vector3f getWorldScale() {
        return wTransform.getScale();
    }
    
}
