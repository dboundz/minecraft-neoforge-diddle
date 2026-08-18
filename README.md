# Spawn Portrait

A NeoForge 26.2 Minecraft mod that plants a rare **Alter of the Diddle** — a 2D block-pixel mural on a named pedestal — in the Overworld.

It does **not** appear at spawn. It generates in newly created chunks at woodland-mansion rarity, using a different roll so it never shares mansion spots or dark-forest biomes.

## Download

- [spawnportrait-1.0.0.jar](https://github.com/dboundz/minecraft-neoforge-diddle/raw/main/dist/spawnportrait-1.0.0.jar)
- [spawnportrait-1.0.0.zip](https://github.com/dboundz/minecraft-neoforge-diddle/raw/main/dist/spawnportrait-1.0.0.zip) (jar plus a short install note)

Needs Minecraft **26.2** and [NeoForge 26.2](https://neoforged.net/). Put the jar in `.minecraft/mods` and launch the NeoForge 26.2 profile. On a server, the host and every player need the same jar.

## Find one

```
/locate structure spawnportrait:alter_of_the_diddle
```

Walk within about 320 blocks and the looping track starts. The chest always holds one random enchanted armor piece or weapon.

Existing worlds only get Alters in **new** chunks.

## Rarity

Same 80-chunk grid scale as woodland mansions, different logic:

- Linear jitter and salt `351728941` (mansions use triangular jitter and salt `10387319`)
- 3% keep chance after the candidate chunk is picked
- Overworld land only — never ocean, river, beach, dark forest, or woodland-mansion biomes
- Hard skip if a woodland mansion structure set is within 16 chunks

## Requirements

- JDK 25 (Gradle will download a toolchain if needed)
- NeoForge 26.2.0.59

## Run

```bash
COPYFILE_DISABLE=1 ./gradlew runClient
```

## Build

```bash
COPYFILE_DISABLE=1 ./gradlew build
```

The playable jar is written to `~/.cache/spawn-portrait-build/libs/` (Gradle build output is kept on the local disk so NeoForm is not broken by AppleDouble files on the external drive).
