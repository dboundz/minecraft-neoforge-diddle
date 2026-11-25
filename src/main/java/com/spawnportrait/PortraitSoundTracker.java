package com.spawnportrait;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public final class PortraitSoundTracker {
    private static final int RANGE = 320;
    private static final int CHUNK_RADIUS = (RANGE >> 4) + 2;
    private static final int SCAN_INTERVAL = 40;
    private static final int CHEST_INTERVAL = 5;

    private static final Map<UUID, TrackedAlter> NEARBY = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> PLAYING = new ConcurrentHashMap<>();

    private record TrackedAlter(BlockPos origin, BlockPos chestPos) {
    }

    private PortraitSoundTracker() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        UUID id = player.getUUID();
        if (player.tickCount % SCAN_INTERVAL == 0) {
            TrackedAlter found = findNearbyAlter(level, player.blockPosition());
            if (found == null) {
                NEARBY.remove(id);
            } else {
                NEARBY.put(id, found);
            }
        } else if (player.tickCount % CHEST_INTERVAL != 0) {
            return;
        }

        TrackedAlter tracked = NEARBY.get(id);
        boolean shouldPlay = tracked != null
                && player.blockPosition().closerThan(tracked.origin(), RANGE)
                && chestHasItem(level, tracked.chestPos());
        setPlaying(player, tracked != null ? tracked.origin() : player.blockPosition(), shouldPlay);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID id = event.getEntity().getUUID();
        NEARBY.remove(id);
        PLAYING.remove(id);
    }

    private static void setPlaying(ServerPlayer player, BlockPos pos, boolean shouldPlay) {
        Boolean wasPlaying = PLAYING.put(player.getUUID(), shouldPlay);
        if (wasPlaying != null && wasPlaying == shouldPlay) {
            return;
        }
        PacketDistributor.sendToPlayer(player, new PlayPortraitSoundPayload(pos, shouldPlay));
    }

    private static boolean chestHasItem(ServerLevel level, BlockPos chestPos) {
        ChunkPos chestChunk = new ChunkPos(chestPos);
        if (level.getChunkSource().getChunkNow(chestChunk.x, chestChunk.z) == null) {
            return false;
        }
        return level.getBlockEntity(chestPos) instanceof ChestBlockEntity chest && !chest.isEmpty();
    }

    private static TrackedAlter findNearbyAlter(ServerLevel level, BlockPos playerPos) {
        Structure structure = level.registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getHolder(ModStructures.ALTER_OF_THE_DIDDLE_KEY)
                .map(holder -> holder.value())
                .orElse(null);
        if (structure == null) {
            return null;
        }

        ChunkPos center = new ChunkPos(playerPos);
        for (int dx = -CHUNK_RADIUS; dx <= CHUNK_RADIUS; dx++) {
            for (int dz = -CHUNK_RADIUS; dz <= CHUNK_RADIUS; dz++) {
                int chunkX = center.x + dx;
                int chunkZ = center.z + dz;
                if (level.getChunkSource().getChunkNow(chunkX, chunkZ) == null) {
                    continue;
                }
                for (StructureStart start : level.structureManager()
                        .startsForStructure(SectionPos.of(new ChunkPos(chunkX, chunkZ), 0), structure)) {
                    if (!start.isValid()) {
                        continue;
                    }
                    AlterOfTheDiddlePiece piece = pieceOf(start);
                    if (piece != null && playerPos.closerThan(piece.origin(), RANGE)) {
                        return new TrackedAlter(piece.origin(), piece.chestPos());
                    }
                }
            }
        }
        return null;
    }

    private static AlterOfTheDiddlePiece pieceOf(StructureStart start) {
        for (StructurePiece piece : start.getPieces()) {
            if (piece instanceof AlterOfTheDiddlePiece diddle) {
                return diddle;
            }
        }
        return null;
    }
}
