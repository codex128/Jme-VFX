
inline float fract(float f) {
    return f - floor(f);
}
inline float random(float seed) {
    return fract(sin(seed + 2.0) * seed);
}
inline float randomRange(float a, float b, float seed) {
    return random(seed) * (b - a) + a;
}
inline float3 randomBoxPosition(float radius, float3 seed) {
    return (float3)(randomRange(-radius, radius, seed.x), randomRange(-radius, radius, seed.y), randomRange(-radius, radius, seed.z));
}


/***************** 
 *    KERNELS    *
 *****************/


__kernel void initParticleData(__write_only image2d_t posImage, write_only image2d_t velImage, float radius, float speed) {
    
    int i = get_global_id(0);
    int j = get_global_id(1);
    
    float4 pos = (float4)(randomBoxPosition(radius, (float3)(i+j, i*j, i+i*j+j)), 0.0);
    //float3 vel = normalize(pos - (float3)(0.0, 1.0, 0.0)) * speed;
    
    write_imagef(posImage, (int2)(i, j), pos);
    
}



__kernel void updateParticleData(__write_only image2d_t posImage, image2d_t velImage, float randomValue, float tpf) {
        
    const int i = get_global_id(0);
    const int j = get_global_id(1);
    
    float4 color = (float4)(random((float)(i+j) * randomValue), random((float)(i*j) * randomValue), random((float)(i+i*j+j) * randomValue), 0.0);
    
    write_imagef(posImage, (int2)(i, j), color);
    
}















