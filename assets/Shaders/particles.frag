
#import "Common/ShaderLib/GLSLCompat.glsllib"

#ifdef COLORMAP
    uniform sampler2D m_ColorMap;
    uniform float m_AlphaDiscard;
#endif

varying vec2 texCoord;
varying vec4 vertexColor;

void main() {
    
    #ifdef COLORMAP
        gl_FragColor = texture2D(m_ColorMap, texCoord) * vertexColor;
        if (gl_FragColor.a < m_AlphaDiscard) {
            discard;
        }
    #else
        gl_FragColor = vertexColor;
    #endif
    
}
