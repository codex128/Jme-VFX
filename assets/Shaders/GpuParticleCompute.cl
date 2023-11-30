
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

inline void writeVector3(__global float * buffer, float3 vector, int i) {
    buffer[i] = vector.x;
    buffer[i+1] = vector.y;
    buffer[i+2] = vector.z;
}

inline float3 readVector3(__global float * buffer, int i) {
    return (float3)(buffer[i], buffer[i+1], buffer[i+2]);
}

__kernel void initParticleData(__global float * position, __global float * velocity, float radius, float speed) {
    
    int i = get_global_id(0);
    float3 pos = randomBoxPosition(radius, (float3)(i, i+5, i+12));
    float3 vel = normalize(pos - (float3)(0.0, 1.0, 0.0)) * speed;
    
    writeVector3(position, pos, i * 3);
    writeVector3(velocity, vel, i * 3);
    
    //int j = i * 3;
    //index[j] = i;
    //index[j+1] = i+1;
    //index[j+2] = i+2;
    
}

__kernel void updateParticleData(__global float * position, __global float * velocity, float randomValue, float tpf) {
        
    const int i = get_global_id(0) * 3;
    
    //float3 pos = readVector3(position, i);
    //float3 vel = readVector3(velocity, i);    
    //pos = pos + vel * tpf;
    
    writeVector3(position, (float3)(random((float)(i) * randomValue), random((float)(i + 5) * randomValue), random((float)(i + 2) * randomValue)), i);
    
}
