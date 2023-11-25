# Jme-VFX

VFX Library for JMonkeyEngine. This is a work in progress: APIs may change.

## Current Features

### Trailing Geometry
![demo of trailing geometry](https://github.com/codex128/Jme-VFX/blob/master/screenshots/demo-trailing-effect.png?raw=true&scale=0.5)

```java
ParticleGroup particles = new ParticleGroup<>(50);
particles.setOverflowHint(ParticleGroup.OverflowHint.CullOld);

TrailingGeometry geometry = new TrailingGeometry(particles, new ParticleSpawner() {
    @Override
    public ParticleData createParticle(Vector3f position, ParticleGroup group) {
        ParticleData p = new ParticleData(1f);
        p.setPosition(position);
        p.setRadius(.1f);
        return p;
    }
});
geometry.setFaceCamera(true);
geometry.setLocalTranslation(0f, 2f, 0f);
geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
geometry.setCullHint(Spatial.CullHint.Never);

Material mat = new Material(assetManager, "MatDefs/trail.j3md");
TextureKey texKey = new TextureKey("Textures/wispy-trail.png");
texKey.setGenerateMips(false);
Texture tex = assetManager.loadTexture(texKey);
mat.setTexture("Texture", tex);
mat.setFloat("Speed", 1f);
mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
mat.setTransparent(true);
geometry.setMaterial(mat);

rootNode.attachChild(geometry);

```
This uses a rudementary particle system which is still in progress. The geometry's mesh generates two
vertices for each particle and connects them to adjacent particles' vertices. By generating static particles
wherever the geometry moves, we can create a trail that follows the geometry.

Note that mipmapping must be disabled for the corresponding demo, otherwise artifacts will appear where the texture coordinates wrap.
