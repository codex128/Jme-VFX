
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform float g_Time;
uniform sampler2D m_Texture;
uniform vec4 m_Color1;
uniform vec4 m_Color2;
uniform float m_Speed;
uniform float m_Threshold;

varying vec2 texCoord;
varying float fadeCoord;

const float seedFloat = 1234.123;
const vec2 seedVec = vec2(14.3423, 76.8593);

float randomVecToFloat(vec2 vector, float seed) {
    return fract(sin(dot(vector.xy, seedVec)) * seed);
}

float noiseTexture(in vec2 uv, in float scale, in float seed) {
    uv *= scale;
    vec2 i = floor(uv);
    vec2 f = fract(uv);
    float a = randomVecToFloat(i, seed);
    float b = randomVecToFloat(i + vec2(1.0, 0.0), seed);
    float c = randomVecToFloat(i + vec2(0.0, 1.0), seed);
    float d = randomVecToFloat(i + vec2(1.0, 1.0), seed);
    vec2 u = f*f*(3.0-2.0*f);
    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
}

void main() {
    
    vec2 scrollUv = fract(vec2(texCoord.x + g_Time * m_Speed, texCoord.y));
    vec2 scrollUv2 = fract(vec2(texCoord.x + g_Time * m_Speed * 1.1, texCoord.y));
    vec4 texColor = texture2D(m_Texture, scrollUv);
    
    float coordFactor = 1.0 - texCoord.x;
    float fade = coordFactor + noiseTexture(scrollUv2, 20.0, seedFloat);
    
    float value = 3.0 * texColor.a * fade * fadeCoord + fadeCoord * 0.7;
    if (value < m_Threshold || texColor.a < 0.005) {
        discard;
    }
    
    gl_FragColor = mix(m_Color1, m_Color2, clamp(texColor.a, 0.0, 1.0));
    
    //gl_FragColor = m_Color1;
    
    //gl_FragColor = vec4(scrollUv.yyy, 1.0);
    
}
