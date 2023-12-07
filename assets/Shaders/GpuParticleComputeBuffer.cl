
inline float fract(float f) {
    return f - floor(f);
}
inline float random(float seed) {
    return fract(sin(seed + 2.0) * seed);
}
inline float randomRange(float a, float b, float seed) {
    return random(seed) * (b - a) + a;
}
inline float pow(float f, int n) {
    float product = 1;
    for (int i = 0; i < n; i++) {
        product *= f;
    }
    return product;
}
inline float lerp(float a, float b, float blend) {
    return (b-a) * blend + a;
}

inline void writeVector3(__global float * buffer, float3 vec, int i) {
    buffer[i] = vec.x;
    buffer[i+1] = vec.y;
    buffer[i+2] = vec.z;
}
inline void writeVector4(__global float * buffer, float4 vec, int i) {
    buffer[i] = vec.x;
    buffer[i+1] = vec.y;
    buffer[i+2] = vec.z;
    buffer[i+3] = vec.w;
}
inline void writeVector5(__global float * buffer, float4 vec, float a, int i) {
    buffer[i] = vec.x;
    buffer[i+1] = vec.y;
    buffer[i+2] = vec.z;
    buffer[i+3] = vec.w;
    buffer[i+4] = a;
}
inline float3 readVector3(__global float * buffer, int i) {
    return (float3)(buffer[i], buffer[i+1], buffer[i+2]);
}
inline float4 readVector4(__global float * buffer, int i) {
    return (float4)(buffer[i], buffer[i+1], buffer[i+2], buffer[i+3]);
}


/***************** 
 *    KERNELS    *
 *****************/


__kernel void initParticleData(__global float * posBuf,
                               __global float * clrBuf,
                               __global float * dataBuf) {
    
    int i = get_global_id(0);
    
    float3 pos = (float3)(
        0.0,
        0.0,
        0.0
    );
    float4 d = (float4)(0, 0, 0, 0);
    d.x = randomRange(0.0f, 6.283185307f, i);
    d.z = 4.9f;
    float initialY = 0;
    
    float2 speed = (float2)(
        randomRange(-1, 1, i*2),
        randomRange(-1, 1, i*4)
    );
    d.wy = normalize(speed) * randomRange(5, 20, i*3);
    
    float red = randomRange(0.8, 1.0, i+76);
    float4 color = (float4)(
        red, 1.0-red, 0.0, 1.0
    );
    
    writeVector3(posBuf, pos, i*3);
    writeVector4(clrBuf, color, i*4);
    writeVector5(dataBuf, d, initialY, i*5);
    //write_imagef(posImage, index, pos);
    //write_imagef(dataImage, index, vel);
    //write_imagef(clrImage, index, color);
    
}



__kernel void updateParticleData(__global float * posBuf,
                                 __global float * clrBuf,
                                 __global float * dataBuf,
                                 float seconds) {
    
    const int i = get_global_id(0);
    
    float3 pos = readVector3(posBuf, i*3);
    float4 d = readVector4(dataBuf, i*5);
    float initialY = dataBuf[i*5+4];
    
    // pos.w = initial y position
    // vel.x = angle
    // vel.y = initial y velocity
    // vel.z = accelleration due to gravity
    // vel.w = horizontal plane speed
    
    float t = seconds;
    
    pos.x = cos(d.x) * t * d.w;
    pos.z = sin(d.x) * t * d.w;
    pos.y = (d.z/6)*-pow(t, 3) + d.y*t + initialY;
    
    writeVector3(posBuf, pos, i*3);
    //write_imagef(writePosImage, index, pos);
    
}















