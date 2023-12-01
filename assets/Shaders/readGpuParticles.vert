
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform sampler2D m_PositionTexture;
uniform vec2 m_DataResolution;
uniform float m_Size;

attribute int inPosition;

/**
 * Reads position data from the image of a given resolution at the vertex id.
 */
vec3 fetchPosition(sampler2D image, vec2 res, float i) {
    vec2 st = vec2(1.0) / res;
    st.y *= floor(i / res.x);
    if (st.y <= 1.0) {
        st.x *= mod(i, res.x);
        return texture2D(image, st).xyz;
    } else {
        return vec3(0.0);
    }
}

void main() {
    
    vec3 position = fetchPosition(m_PositionTexture, m_DataResolution, gl_VertexID);
    
    gl_Position = g_WorldViewProjectionMatrix * vec4(position * 100.0, 1.0);
    gl_PointSize = m_Size;
    
}
