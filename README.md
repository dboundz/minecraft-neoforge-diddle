# Spawn Portrait

A NeoForge 26.2 Minecraft mod that places a 2D block-pixel portrait in front of you whenever you join a world or respawn.

## Requirements

- JDK 25 (Gradle will download a toolchain if needed)
- NeoForge 26.2.0.59

## Run

```bash
COPYFILE_DISABLE=1 ./gradlew runClient
```

Join a world. The mural appears a few blocks ahead of you. Die and respawn to place it again at the new spawn point.

## Build

```bash
./gradlew build
```

The playable jar is written to `~/.cache/spawn-portrait-build/libs/` (Gradle build output is kept on the local disk so NeoForm is not broken by AppleDouble files on the external drive).
