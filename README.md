# Spawn Portrait

A NeoForge 26.2 Minecraft mod that places a 2D block-pixel portrait behind you whenever you join a world or respawn.

## Download

- [spawnportrait-1.0.0.jar](https://github.com/dboundz/minecraft-neoforge-diddle/raw/main/dist/spawnportrait-1.0.0.jar)
- [spawnportrait-1.0.0.zip](https://github.com/dboundz/minecraft-neoforge-diddle/raw/main/dist/spawnportrait-1.0.0.zip) (jar plus a short install note)

Needs Minecraft **26.2** and [NeoForge 26.2](https://neoforged.net/). Put the jar in `.minecraft/mods` and launch the NeoForge 26.2 profile. On a server, the host and every player need the same jar.

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
