
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform vec4 m_Color;
varying vec4 vertexColor;

void main() {
    
    gl_FragColor = vertexColor * m_Color;
    
}
