/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.gpu;

/**
 *
 * @author codex
 */
public class ParticleInitArgs {
    
    private final Object[] args;

    public ParticleInitArgs(Object... args) {
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
    
}
