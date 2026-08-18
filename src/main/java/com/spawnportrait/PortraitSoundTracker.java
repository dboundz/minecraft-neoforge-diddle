package com.spawnportrait;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
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
    private static final int CHECK_INTERVAL = 40;
    private static final Set<UUID> PLAYING = ConcurrentHashMap.newKeySet();

    private PortraitSoundTracker() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (player.tickCount % CHECK_INTERVAL != 0) {
            return;
        }
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        BlockPos nearby = findNearbyOrigin(level, player.blockPosition());
        if (nearby != null) {
            if (PLAYING.add(player.getUUID())) {
                PacketDistributor.sendToPlayer(player, new PlayPortraitSoundPayload(nearby));
            }
        } else {
            PLAYING.remove(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PLAYING.remove(event.getEntity().getUUID());
    }

    private static BlockPos findNearbyOrigin(ServerLevel level, BlockPos playerPos) {
        var structure = level.registryAccess()
                .lookupOrThrow(Registries.STRUCTURE)
                .get(ModStructures.ALTER_OF_THE_DIDDLE_KEY)
                .map(holder -> holder.value())
                .orElse(null);
        if (structure == null) {
            return null;
        }

        ChunkPos center = ChunkPos.containing(playerPos);
        for (int dx = -CHUNK_RADIUS; dx <= CHUNK_RADIUS; dx++) {
            for (int dz = -CHUNK_RADIUS; dz <= CHUNK_RADIUS; dz++) {
                int chunkX = center.x() + dx;
                int chunkZ = center.z() + dz;
                if (level.getChunkSource().getChunkNow(chunkX, chunkZ) == null) {
                    continue;
                }
                for (StructureStart start : level.structureManager()
                        .startsForStructure(SectionPos.of(new ChunkPos(chunkX, chunkZ), 0), structure)) {
                    if (!start.isValid()) {
                        continue;
                    }
                    BlockPos origin = originOf(start);
                    if (origin != null && playerPos.closerThan(origin, RANGE)) {
                        return origin;
                    }
                }
            }
        }
        return null;
    }

    private static BlockPos originOf(StructureStart start) {
        for (StructurePiece piece : start.getPieces()) {
            if (piece instanceof AlterOfTheDiddlePiece diddle) {
                return diddle.origin();
            }
        }
        return start.getBoundingBox().getCenter();
    }
}
