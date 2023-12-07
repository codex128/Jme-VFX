
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform float m_Size;

attribute vec3 inPosition;
attribute vec4 inColor;

varying vec4 vertexColor;

void main() {
    
    vertexColor = inColor;
    
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
    gl_PointSize = m_Size;
    
}
