/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.utils;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.math.Matrix4f;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import java.nio.FloatBuffer;

/**
 *
 * @author codex
 */
public class InfiniteSphere extends BoundingSphere {

    public static final InfiniteSphere INSTANCE = new InfiniteSphere();
    
    @Override
    public Type getType() {
        return Type.Sphere;
    }

    @Override
    public BoundingVolume transform(Transform trans, BoundingVolume store) {
        return this;
    }

    @Override
    public BoundingVolume transform(Matrix4f trans, BoundingVolume store) {
        return this;
    }

    @Override
    public Plane.Side whichSide(Plane plane) {
        return Plane.Side.None;
    }

    @Override
    public void computeFromPoints(FloatBuffer points) {}

    @Override
    public BoundingVolume merge(BoundingVolume volume) {
        return this;
    }

    @Override
    public BoundingVolume mergeLocal(BoundingVolume volume) {
        return this;
    }

    @Override
    public BoundingVolume clone(BoundingVolume store) {
        return this;
    }

    @Override
    public float distanceToEdge(Vector3f point) {
        return Float.POSITIVE_INFINITY;
    }

    @Override
    public boolean intersects(BoundingVolume bv) {
        return false;
    }

    @Override
    public boolean intersects(Ray ray) {
        return false;
    }

    @Override
    public boolean intersectsSphere(BoundingSphere bs) {
        return false;
    }

    @Override
    public boolean intersectsBoundingBox(BoundingBox bb) {
        return false;
    }

    @Override
    public boolean contains(Vector3f point) {
        return true;
    }

    @Override
    public boolean intersects(Vector3f point) {
        return true;
    }

    @Override
    public float getVolume() {
        return Float.POSITIVE_INFINITY;
    }

    @Override
    public int collideWith(Collidable other, CollisionResults results) throws UnsupportedCollisionException {
        return 0;
    }
    
}
