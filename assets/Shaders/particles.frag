
#import "Common/ShaderLib/GLSLCompat.glsllib"

#ifdef COLORMAP
    uniform sampler2D m_ColorMap;
    uniform bool m_Grayscale;
    varying vec4 texCoord;
#endif

#ifdef ALPHA_DISCARD
    uniform float m_AlphaDiscard;
#endif

#ifdef POINT_SPRITE
    varying float distanceAlpha;
    #ifdef ROTATE_TEX
        varying float angle;
        const vec2 center = vec2(0.5, 0.5);
        mat2 rotate(float a){
            return mat2(cos(a), -sin(a), sin(a), cos(a));
        }
    #endif
#endif

varying vec4 vertexColor;

void main() {
    
    #ifdef COLORMAP
        #ifndef POINT_SPRITE
            vec2 uv = texCoord.xy;
        #else
            vec2 uv = mix(texCoord.xy, texCoord.zw, gl_PointCoord.xy);
            #ifdef ROTATE_TEX
                uv = rotate(angle) * (uv - center) + center;
            #endif
        #endif
        gl_FragColor = texture2D(m_ColorMap, uv) * vertexColor;
        #ifdef GRAYSCALE
            gl_FragColor.rgb = vec3((gl_FragColor.r + gl_FragColor.g + gl_FragColor.b) / 3);
        #endif
    #else
        gl_FragColor = vertexColor;
    #endif
    
    #ifdef POINT_SPRITE
        gl_FragColor.a *= distanceAlpha;
    #endif
    
    #ifdef ALPHA_DISCARD
        if (gl_FragColor.a < m_AlphaDiscard) {
            discard;
        }
    #endif
    
}
