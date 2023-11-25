/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.test;

import codex.boost.GameAppState;
import com.jme3.app.Application;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.LightProbeFactory;
import com.jme3.environment.generation.JobProgressAdapter;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.LightProbe;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.util.SkyFactory;
import jme3utilities.sky.SkyControl;
import jme3utilities.sky.StarsOption;
import jme3utilities.sky.Updater;

/**
 *
 * @author codex
 */
public class GlobalIlluminationState extends GameAppState {
    
    private SkyControl skyControl;
    
    @Override
    protected void init(Application app) {
        
        AmbientLight gi = new AmbientLight();
        rootNode.addLight(gi);
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(1f, -5f, 1f));
        dl.setColor(ColorRGBA.White);
        rootNode.addLight(dl);
        
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 4096*2, 4);
        dlsr.setLambda(1);
        dlsr.setShadowCompareMode(CompareMode.Hardware);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        dlsr.setLight(dl);
        app.getViewPort().addProcessor(dlsr);
        
        EnvironmentCamera envCam = getState(EnvironmentCamera.class, true);
        envCam.setPosition(new Vector3f(0f, 20f, 0f));
        envCam.setBackGroundColor(new ColorRGBA(.3f, 0f, .6f, 1f));
        LightProbeFactory.makeProbe(envCam, rootNode, new JobProgressAdapter<LightProbe>() {
            @Override
            public void done(LightProbe result) {
                result.getArea().setRadius(100f);
                rootNode.addLight(result);
            }
        });
        
        Spatial sky = SkyFactory.createSky(assetManager, "Demo/FullskiesSunset0068.dds", SkyFactory.EnvMapType.CubeMap);
        sky.setShadowMode(RenderQueue.ShadowMode.Off);
        rootNode.attachChild(sky);        
        skyControl = new SkyControl(assetManager, cam, .5f, StarsOption.TopDome, true);
        rootNode.addControl(skyControl);
        //skyControl.setCloudiness(0.8f);
        //skyControl.setCloudsYOffset(0.4f);
        //skyControl.setTopVerticalAngle(1.78f);
        skyControl.getSunAndStars().setHour(12);
        Updater updater = skyControl.getUpdater();
        updater.setAmbientLight(gi);
        updater.setMainLight(dl);
        updater.addShadowRenderer(dlsr);
        skyControl.setEnabled(true);
    
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        float hour = skyControl.getSunAndStars().getHour()+0.3f*tpf;
        if (hour > 24) {
            hour -= 24;
        }
        skyControl.getSunAndStars().setHour(hour);
    }
    
}
