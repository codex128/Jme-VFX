
#import "Common/ShaderLib/GLSLCompat.glsllib"

#ifdef COLORMAP
    varying vec4 vertexColor;
#endif

uniform vec4 m_Color;

void main() {
    
    #ifdef COLORMAP
        gl_FragColor = vertexColor * m_Color;
    #else
        gl_FragColor = m_Color;
    #endif
    
}
