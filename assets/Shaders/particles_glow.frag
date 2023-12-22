
#import "Common/ShaderLib/GLSLCompat.glsllib"

#ifdef GLOW
    #ifdef COLORMAP
        uniform sampler2D m_ColorMap;
        uniform bool m_Grayscale;
        varying vec4 texCoord;
    #endif
    #ifdef GLOWMAP
        uniform sampler2D m_GlowMap;
    #endif
    #ifdef ALPHA_DISCARD
        uniform float m_AlphaDiscard;
    #endif
    #ifdef POINT_SPRITE
        varying float distanceAlpha;
    #endif
    varying vec4 vertexColor;
#endif

void main() {    
    
    #ifdef GLOW    
        #if defined(COLORMAP) || defined(GLOWMAP)
            #ifndef POINT_SPRITE
                vec2 uv = texCoord.xy;
            #else
                vec2 uv = mix(texCoord.xy, texCoord.zw, gl_PointCoord.xy);
            #endif
        #endif
        #ifdef GLOWMAP
            gl_FragColor = texture2D(m_GlowMap, uv) * vertexColor;
        #else
            #ifdef COLORMAP
                gl_FragColor = texture2D(m_ColorMap, uv) * vertexColor;
                #ifdef GRAYSCALE
                    gl_FragColor.rgb = vec3((gl_FragColor.r + gl_FragColor.g + gl_FragColor.b) / 3);
                #endif
            #else
                gl_FragColor = vertexColor;
            #endif
        #endif
        #ifdef POINT_SPRITE
            gl_FragColor.a *= distanceAlpha;
        #endif
        #ifdef ALPHA_DISCARD
            if (gl_FragColor.a < m_AlphaDiscard) {
                discard;
            }
        #endif
    #else
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
    #endif
    
}