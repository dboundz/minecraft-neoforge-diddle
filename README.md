# Spawn Portrait

Far from any hearth, in lands the maps leave blank, a quartz shrine waits. Those who find the **Alter of the Diddle** and leave an offering will hear a song that should not be. Empty the chest, and the melody dies as though it never was.

<img width="966" height="620" alt="Screenshot 2026-08-19 at 7 49 56 AM" src="https://github.com/user-attachments/assets/4231c42f-e722-4616-8a34-413fc81a0ea1" />

A NeoForge 1.21.1 Minecraft mod. It does **not** appear at spawn. The Alter generates in newly created Overworld chunks at woodland-mansion rarity, using a different roll so it never shares mansion spots or dark-forest biomes.

## Download

- [spawnportrait-1.0.1.jar](https://github.com/dboundz/minecraft-neoforge-diddle/raw/main/dist/spawnportrait-1.0.1.jar)

Needs Minecraft **1.21.1** and NeoForge **21.1.265**. Put the jar in the `mods` folder on the server and on every client, then restart.

## Find one

```
/locate structure spawnportrait:alter_of_the_diddle
```

Walk within about 320 blocks and the looping track starts while the altar chest has anything in it. Take the item out and the sound stops; put something back and it starts again. The generated offering is one random enchanted armor piece or weapon.

Existing worlds only get Alters in **new** chunks.

## Rarity

Same 80-chunk grid scale as woodland mansions, different logic:

- Linear jitter and salt `351728941` (mansions use triangular jitter and salt `10387319`)
- 3% keep chance after the candidate chunk is picked
- Overworld land only — never ocean, river, beach, dark forest, or woodland-mansion biomes
- Hard skip if a woodland mansion structure set is within 16 chunks

## Requirements

- JDK 21 (Gradle will download a toolchain if needed)
- NeoForge 21.1.265

## Run

```bash
COPYFILE_DISABLE=1 ./gradlew runClient
```

## Build

```bash
COPYFILE_DISABLE=1 ./gradlew build
```

The playable jar is written to `~/.cache/spawn-portrait-build/libs/` (Gradle build output is kept on the local disk so NeoForm is not broken by AppleDouble files on the external drive).
