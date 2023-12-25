/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.vfx;

import codex.vfx.annotations.VfxAttribute;
import codex.vfx.annotations.VfxCommand;
import codex.vfx.annotations.VfxInfo;

/**
 *
 * @author codex
 */
public interface VirtualEffect {
    
    @VfxCommand(name="play")
    public void play();
    
    @VfxCommand(name="pause")
    public void pause();
    
    @VfxAttribute(name="updateSpeed")
    public void setUpdateSpeed(float speed);
    
    @VfxAttribute(name="initialDelay")
    public void setInitialDelay(float delay);
    
    @VfxInfo(name="localPlayState")
    public boolean getLocalPlayState();
    
    @VfxAttribute(name="updateSpeed", input=false)
    public float getUpdateSpeed();
    
    @VfxAttribute(name="initialDelay", input=false)
    public float getInitialDelay();
    
    @VfxInfo(name="worldPlayState")
    public boolean getWorldPlayState();
    
    @VfxInfo(name="worldUpdateSpeed", important=false)
    public float getWorldUpdateSpeed();
    
    @VfxInfo(name="worldInitialDelay", important=false)
    public float getWorldInitialDelay();
    
    @VfxInfo(name="time")
    public float getTime();
    
    public float getRawTime();
    
}
