
#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"
#import "ShaderLib/kernel.glsllib"

// uniforms filled by jme
uniform COLORTEXTURE m_Texture;

uniform vec2 m_SampleStep;
uniform float m_SampleFactor;
uniform mat3 m_Kernel;

varying vec2 texCoord;

void main() {
    
    vec3 color = vec3(0.0);
    for(int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            float k = m_Kernel[i][j] * m_SampleFactor;
            vec2 offset = getKernelSampleOffset(i * 3 + j, 3, m_SampleStep);
            color += getColor(m_Texture, texCoord.xy + offset).rgb * k;
        }
    }
    
    gl_FragColor = vec4(color, 1.0);
}