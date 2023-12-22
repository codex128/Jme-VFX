/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vfx.particles.tweens;

import codex.vfx.utils.VfxUtils;
import com.jme3.math.EaseFunction;
import com.jme3.math.FastMath;

/**
 *
 * @author codex
 */
public class Ease {
    
    public static final EaseFunction constant = (float value) -> 0;
    public static final EaseFunction random = (float value) -> VfxUtils.gen.nextFloat();
    
    public static final EaseFunction inLinear = (float value) -> value;
    public static final EaseFunction inQuad = (float value) -> value * value;
    public static final EaseFunction inCubic = (float value) -> value * value * value;
    public static final EaseFunction inQuart = (float value) -> value * value * value * value;
    public static final EaseFunction inQuint = (float value) -> value * value * value * value * value;
    public static final EaseFunction outElastic = (float value) -> {
        return FastMath.pow(2f, -10f * value) * FastMath.sin((value - 0.3f / 4f) * (2f * FastMath.PI) / 0.3f) + 1f;
    };
    public static final EaseFunction outBounce = (float value) -> {
        if (value < (1f / 2.75f)) {
            return (7.5625f * value * value);
        } else if (value < (2f / 2.75f)) {
            return (7.5625f * (value -= (1.5f / 2.75f)) * value + 0.75f);
        } else if (value < (2.5 / 2.75)) {
            return (7.5625f * (value -= (2.25f / 2.75f)) * value + 0.9375f);
        } else {
            return (7.5625f * (value -= (2.625f / 2.75f)) * value + 0.984375f);
        }
    };

    public static final EaseFunction inElastic = new Invert(outElastic);
    public static final EaseFunction inBounce = new Invert(outBounce);

    public static final EaseFunction outLinear = new Invert(inLinear);
    public static final EaseFunction outQuad = new Invert(inQuad);
    public static final EaseFunction outCubic = new Invert(inCubic);
    public static final EaseFunction outQuart = new Invert(inQuart);
    public static final EaseFunction outQuint = new Invert(inQuint);

    public static final EaseFunction inOutLinear = new InOut(inLinear, outLinear);
    public static final EaseFunction inOutQuad = new InOut(inQuad, outQuad);
    public static final EaseFunction inOutCubic = new InOut(inCubic, outCubic);
    public static final EaseFunction inOutQuart = new InOut(inQuart, outQuart);
    public static final EaseFunction inOutQuint = new InOut(inQuint, outQuint);
    public static final EaseFunction inOutElastic = new InOut(inElastic, outElastic);
    public static final EaseFunction inOutBounce = new InOut(inBounce, outBounce);
    
    public static final EaseFunction smoothStep = (float t) -> t * t * (3f - 2f * t);
    public static final EaseFunction smootherStep = (float t) -> t * t * t * (t * (t * 6f - 15f) + 10f);
    
    public static EaseFunction inOut(EaseFunction in, EaseFunction out) {
        return new InOut(in, out);
    }
    public static EaseFunction inOut(EaseFunction in, EaseFunction out, float center) {
        assert center >= 0 && center <= 1;
        return new InOut(in, out, center);
    }
    public static EaseFunction invert(EaseFunction func) {
        return new Invert(func);
    }
    public static EaseFunction sequence(EaseFunction... funcs) {
        return new Sequence(funcs);
    }
    
    public static class InOut implements EaseFunction {

        private final EaseFunction in, out;
        private final float center;

        private InOut(EaseFunction in, EaseFunction out) {
            this(in, out, .5f);
        }
        private InOut(EaseFunction in, EaseFunction out, float center) {
            this.in = in;
            this.out = out;
            this.center = center;
        }
        
        @Override
        public float apply(float value) {
            if (value < center) {
                value /= center;
                return in.apply(value);
            } else {
                value = (value-center)/(1f-center);
                return out.apply(value);
            }
        }
        
    }
    public static class Invert implements EaseFunction {

        private final EaseFunction func;

        public Invert(EaseFunction func) {
            this.func = func;
        }
        
        @Override
        public float apply(float value) {
            return func.apply(1f-value);
        }
        
    }
    public static class Sequence implements EaseFunction {
        
        private final EaseFunction[] funcs;

        public Sequence(EaseFunction... funcs) {
            this.funcs = funcs;
        }
        
        @Override
        public float apply(float value) {
            int i = Math.min((int)(value*funcs.length), funcs.length-1);
            float step = 1f/funcs.length;
            float min = i*step, max = min+step;
            value = FastMath.clamp(value, min, max);
            return funcs[i].apply(FastMath.unInterpolateLinear(value, min, max));
        }
        
    }
    
}
