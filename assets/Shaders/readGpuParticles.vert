
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform float m_Size;

in vec3 inPosition;

void main() {
    
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
    gl_PointSize = m_Size;
    
}
