
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_ViewProjectionMatrix;
uniform mat4 g_WorldMatrix;
uniform vec3 g_CameraPosition;
uniform vec3 g_CameraUp;

attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inTexCoord2;
attribute vec4 inColor;

varying vec3 wPosition;
varying vec3 wNormal;
varying vec2 texCoord;
varying vec4 vertexColor;

#if defined(COLORMAP) && defined(USE_SPRITE_SHEET)
    uniform vec2 m_SpriteGrid;
#endif

void main(){
    
    vec4 modelSpacePos = vec4(inPosition, 1.0);
    texCoord = inTexCoord;
    vertexColor = inColor;
    
    // billboard
    wPosition = (g_WorldMatrix * modelSpacePos).xyz;
    wNormal = normalize(g_CameraPosition - wPosition);
    vec3 across = cross(wNormal, g_CameraUp);
    wPosition -= across * inTexCoord2.x - g_CameraUp * inTexCoord2.y;
    gl_Position = g_ViewProjectionMatrix * vec4(wPosition, 1.0);
    
    //modelSpacePos.xy += inTexCoord2.xy;
    //gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
    
    // texture coordinates
    //#if defined(COLORMAP) && defined(USE_SPRITE_SHEET)
    //    texCoord /= m_SpriteGrid;
    //    texCoord.x += mod(inTexCoord2.z, m_SpriteGrid.x) / m_SpriteGrid.x;
    //    texCoord.y += floor(inTexCoord2.z / m_SpriteGrid.x) / m_SpriteGrid.y;
    //#endif
    
}