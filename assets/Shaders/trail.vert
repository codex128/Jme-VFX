
#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Instancing.glsllib"

attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute float inTexCoord2;
attribute vec4 inTexCoord3;

uniform vec3 g_CameraPosition;
uniform bool m_FaceCamera;

varying vec3 wPosition;
varying vec2 texCoord;
varying float fadeCoord;
varying float leadingPair;

void main() {
    
    texCoord = inTexCoord;
    fadeCoord = inTexCoord2;
    
    if (m_FaceCamera) {
        vec3 outward = cross(normalize(inTexCoord3.xyz), normalize(g_CameraPosition - inPosition)) * inTexCoord3.w;
        wPosition = inPosition + outward;
        //wPosition = inPosition;
        gl_Position = g_WorldViewProjectionMatrix * vec4(wPosition, 1.0);
    }
    else {
        vec4 modelSpacePos = vec4(inPosition, 1.0);    
        wPosition = TransformWorld(modelSpacePos).xyz;
        gl_Position = TransformWorldViewProjection(modelSpacePos);
    }
    
}
