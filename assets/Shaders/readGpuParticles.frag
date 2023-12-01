
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform vec4 m_Color;
varying float index;

float random(float seed) {
    return fract(sin(seed) * (seed + 1043.503));
}
vec3 randomColor(float seed) {
    return vec3(random(seed), random(seed + 3402.34), random(seed - 413.39));
}

void main() {
    
    gl_FragColor.rgb = randomColor(index);
    
}
