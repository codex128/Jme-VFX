
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_ViewProjectionMatrix;
uniform mat4 g_WorldMatrix;
uniform vec3 g_CameraPosition;
uniform vec3 g_CameraUp;

attribute vec3 inPosition;
attribute vec4 inColor;

varying vec3 wPosition;
varying vec4 vertexColor;

#ifndef POINT_SPRITE
    attribute vec2 inTexCoord2;
    varying vec3 wNormal;
#else
    attribute float inSize;
    uniform float m_Quadratic;
    const float SIZE_MULTIPLIER = 4.0;
    varying float distanceAlpha;
    #ifdef ROTATE_TEX
        attribute float inTexCoord4; // rotation angle
        varying float angle;
    #endif
#endif

#ifdef COLORMAP
    #ifndef POINT_SPRITE
        attribute vec2 inTexCoord;
    #endif
    varying vec4 texCoord;
    #ifdef USE_SPRITE_SHEET
        attribute float inTexCoord3;
        uniform vec2 m_SpriteGrid;
    #endif
#endif

void main(){
    
    vec4 modelSpacePos = vec4(inPosition, 1.0);
    vertexColor = inColor;    
    wPosition = (g_WorldMatrix * modelSpacePos).xyz;
    
    #ifndef POINT_SPRITE
        wNormal = normalize(g_CameraPosition - wPosition);
        vec3 across = cross(wNormal, g_CameraUp);
        wPosition -= across * inTexCoord2.x - g_CameraUp * inTexCoord2.y;
    #else
        float d = distance(g_CameraPosition, wPosition);
        float size = (inSize * SIZE_MULTIPLIER * m_Quadratic) / d;
        gl_PointSize = max(1.0, size);
        distanceAlpha = min(1.0, size);
        #ifdef ROTATE_TEX
            angle = inTexCoord4;
        #endif
    #endif
    
    gl_Position = g_ViewProjectionMatrix * vec4(wPosition, 1.0);
    
    #ifdef COLORMAP
        #ifndef POINT_SPRITE
            texCoord = vec4(inTexCoord.x, inTexCoord.y, 0.0, 0.0);
        #else
            texCoord = vec4(0.0, 0.0, 1.0, 1.0);
        #endif
        #ifdef USE_SPRITE_SHEET
            #ifndef POINT_SPRITE
                texCoord.xy /= m_SpriteGrid;
            #endif
            float index = float(inTexCoord3);
            texCoord.x = mod(index, m_SpriteGrid.x) / m_SpriteGrid.x;
            texCoord.y = 1.0 - (floor(index / m_SpriteGrid.x) / m_SpriteGrid.y);
            #ifdef POINT_SPRITE
                texCoord.z = texCoord.x + (1.0 / m_SpriteGrid.x);
                texCoord.w = texCoord.y - (1.0 / m_SpriteGrid.y);
            #endif
        #endif
    #endif
    
}