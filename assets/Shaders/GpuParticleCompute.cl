
inline float fract(float f) {
    return f - floor(f);
}
inline float random(float seed) {
    return fract(sin(seed + 2.0) * seed);
}
inline float randomRange(float a, float b, float seed) {
    return random(seed) * (b - a) + a;
}
inline float sq(float f) {
    return f*f;
}
inline float lerp(float a, float b, float blend) {
    return (b-a) * blend + a;
}


/***************** 
 *    KERNELS    *
 *****************/


__kernel void initParticleData(__write_only image2d_t posImage,
                               __write_only image2d_t velImage,
                               __write_only image2d_t clrImage) {
    
    int i = get_global_id(0);
    int j = get_global_id(1);
    int2 index = (int2)(i, j);
    
    float4 pos = (float4)(
        0.0,
        0.0,
        0.0,
        0.0
    );
    float4 vel = (float4)(0, 0, 0, 0);
    vel.x = randomRange(0.0f, 6.283185307f, i+j);
    vel.z = 4.9f;
    
    float2 speed = (float2)(
        randomRange(-1, 1, i+j*2),
        randomRange(-1, 1, i*4+j*3)
    );    
    vel.wy = normalize(speed) * randomRange(0, 10, j*10+i);
    
    float red = randomRange(0.8, 1.0, j*i+76);
    float4 color = (float4)(
        red, 1.0-red, 0.0, 1.0
    );
    
    write_imagef(posImage, index, pos);
    write_imagef(velImage, index, vel);
    write_imagef(clrImage, index, color);
    
}



__kernel void updateParticleData(__write_only image2d_t writePosImage,
                                 __read_only image2d_t readPosImage,
                                 __write_only image2d_t writeVelImage,
                                 __read_only image2d_t readVelImage,
                                 float randomValue, float time, float tpf) {
    
    const int i = get_global_id(0);
    const int j = get_global_id(1);
    const int2 index = (int2)(i, j);
    
    float4 pos = read_imagef(readPosImage, index);
    float4 vel = read_imagef(readVelImage, index);
    
    // pos.w = initial y position
    // vel.x = angle
    // vel.y = initial y velocity
    // vel.z = accelleration due to gravity
    // vel.w = horizontal plane speed
    
    float t = time;    
    pos.x = cos(vel.x) * t * vel.w;
    pos.z = sin(vel.x) * t * vel.w;
    pos.y = vel.z * 0.5f * -sq(t) + vel.y*t + pos.w;
    
    write_imagef(writePosImage, index, pos);
    write_imagef(writeVelImage, index, vel);
    
}















