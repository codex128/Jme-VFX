
#import "Common/ShaderLib/GLSLCompat.glsllib"

#ifdef COLORMAP
    uniform sampler2D m_ColorMap;
    varying vec4 texCoord;
    #ifdef ALPHA_DISCARD
        uniform float m_AlphaDiscard;
    #endif
#endif

varying vec4 vertexColor;

void main() {
    
    #ifdef COLORMAP
        #ifndef POINT_SPRITE
            vec2 uv = texCoord.xy;
        #else
            vec2 uv = mix(texCoord.xy, texCoord.zw, gl_PointCoord.xy);
        #endif
        gl_FragColor = texture2D(m_ColorMap, uv) * vertexColor;
        #ifdef ALPHA_DISCARD
            if (gl_FragColor.a < m_AlphaDiscard) {
                discard;
            }
        #endif
    #else
        gl_FragColor = vertexColor;
    #endif
    
}
