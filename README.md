# Jme-VFX

VFX Library for JMonkeyEngine. This is a work in progress: APIs may change.

## Dependencies
This library requires:
* JMonkeyEngine 3.6
* Lemur 1.16
* Heart
* Boost (my personal utilities)
* Java 8 required, Java 17 suggested (see note below)

Demos additionally require:
* SkyControl

All, except Boost, are common libraries for JMonkeyEngine.

This library was intended to only require Java 8, but due to a mistake, Java 17 is required. However, by changing the source code to remove a single override annotation in `codex.vfx.utils.VfxRandomGenerator`, you can restore Java 8 compatibility. I am working on fixing this issue, but in the meantime, sorry for the inconvenience.
