
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform sampler2D m_PositionMap0;
uniform sampler2D m_PositionMap1;
uniform bool m_ReadWriteValue;
uniform vec2 m_DataResolution;
uniform float m_Size;

#ifdef COLORMAP
    uniform sampler2D m_ColorMap;
    varying vec4 vertexColor;
#endif

attribute int inPosition;

vec2 calcPixelUv(vec2 res, float i) {    
    vec2 st = vec2(1.0) / res;
    st.y *= floor(i / res.x);
    if (st.y <= 1.0) {
        st.x *= mod(i, res.x);
    }
    return st;
}
vec4 readPixel(sampler2D image, vec2 uv) {
    if (uv.y <= 1.0) {
        return texture2D(image, uv);
    } else {
        return vec4(0.0);
    }
}

void main() {
    
    vec2 uv = calcPixelUv(m_DataResolution, gl_VertexID);    
    vec4 position = readPixel((m_ReadWriteValue ? m_PositionMap0 : m_PositionMap1), uv);
    
    #ifdef COLORMAP
        vertexColor = readPixel(m_ColorMap, uv);
    #endif
    
    position.w = 1.0;
    gl_Position = g_WorldViewProjectionMatrix * position;
    gl_PointSize = max(m_Size - gl_Position.z * 0.5, 1.0);
    //gl_PointSize = m_Size;
    
}
