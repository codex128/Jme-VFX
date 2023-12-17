/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.utils;

import com.jme3.asset.AssetManager;
import com.jme3.light.LightProbe;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import jme3utilities.math.noise.Generator;

/**
 * Math utils for vfx applications.
 * 
 * @author codex
 */
public class VfxUtils {
    
    public static final Generator gen = new Generator();
    private static final Plane tempPlane = new Plane();
    
    /**
     * Gets the shared generator instance.
     * <p>
     * Useful for quick'n'dirty randomization.
     * 
     * @return 
     */
    public static Generator getGenerator() {
        return gen;
    }
    
    /**
     * Generates a random vector within a box with
     * the vectors A and B at opposite corners.
     * 
     * @param a
     * @param b
     * @return random vector within box
     */
    public static Vector3f random(Vector3f a, Vector3f b) {
        return new Vector3f(
            gen.nextFloat(a.x, b.x),
            gen.nextFloat(a.y, b.y),
            gen.nextFloat(a.z, b.z)
        );
    }
    
    /**
     * Generates a random vector within a box.
     * 
     * @param center center point of the box
     * @param x x radius
     * @param y y radius
     * @param z z radius
     * @return 
     */
    public static Vector3f random(Vector3f center, float x, float y, float z) {
        return new Vector3f(
            gen.nextFloat(center.x-x, center.x+x),
            gen.nextFloat(center.y-y, center.y+y),
            gen.nextFloat(center.z-z, center.z+z)
        );
    }
    
    /**
     * Loads light probe from j3o file.
     * 
     * @param assetManager
     * @param path
     * @return 
     */
    public static LightProbe loadLightProbe(AssetManager assetManager, String path) {
        Node probeNode = (Node)assetManager.loadModel(path);
        return (LightProbe)probeNode.getLocalLightList().iterator().next();
    }
    
    /**
     * Offsets the vector by an angle and stores the result.
     * 
     * @param vec
     * @param angle
     * @param store stores result, or null
     * @return 
     */
    public static Vector3f offsetByAngle(Vector3f vec, float angle, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        store.set(vec).normalizeLocal().multLocal(FastMath.cos(angle));
        tempPlane.setOriginNormal(Vector3f.ZERO, store);
        store.set(tempPlane.getClosestPoint(VfxUtils.gen.nextUnitVector3f()
                .multLocal(VfxUtils.gen.nextFloat(FastMath.sin(angle))).addLocal(store)));
        return store.normalizeLocal().multLocal(vec.length());
    }
    
}
