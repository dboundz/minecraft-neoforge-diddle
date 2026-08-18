package com.spawnportrait;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Places the 2D portrait as a one-block-thick wall far behind the player.
 */
public final class PortraitPlacer {
    private static final int DISTANCE = 300;
    private static final int BLOCKS_PER_TICK = 512;

    private PortraitPlacer() {
    }

    public static void placeBehindPlayer(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        Direction facing = Direction.fromYRot(player.getYRot());
        Direction behind = facing.getOpposite();
        Direction left = facing.getCounterClockWise();
        BlockPos origin = player.blockPosition().relative(behind, DISTANCE);

        int halfWidth = PortraitPattern.WIDTH / 2;
        BlockPos leftEdgeHint = origin.relative(left, halfWidth);
        BlockPos rightEdgeHint = origin.relative(left.getOpposite(), halfWidth);
        BlockPos frontHint = origin.relative(facing, AlterOfTheDiddle.DEPTH);
        preloadChunks(level, leftEdgeHint, rightEdgeHint);
        preloadChunks(level, origin, frontHint);

        int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, origin.getX(), origin.getZ());
        BlockPos altarOrigin = new BlockPos(origin.getX(), groundY, origin.getZ());
        AlterOfTheDiddle.place(level, altarOrigin, facing, left);

        BlockPos leftEdge = altarOrigin.above(AlterOfTheDiddle.HEIGHT).relative(left, halfWidth);

        List<BlockPos> positions = new ArrayList<>();
        List<BlockState> states = new ArrayList<>();
        for (int row = 0; row < PortraitPattern.HEIGHT; row++) {
            for (int col = 0; col < PortraitPattern.WIDTH; col++) {
                Block block = PortraitPattern.blockAt(row, col);
                if (block == Blocks.AIR) {
                    continue;
                }
                positions.add(leftEdge
                        .relative(left.getOpposite(), col)
                        .above(PortraitPattern.HEIGHT - 1 - row));
                states.add(block.defaultBlockState());
            }
        }

        placeBatch(player, level, altarOrigin, behind, positions, states, 0);
    }

    private static void preloadChunks(ServerLevel level, BlockPos a, BlockPos b) {
        int minCx = SectionPos.blockToSectionCoord(Math.min(a.getX(), b.getX()));
        int maxCx = SectionPos.blockToSectionCoord(Math.max(a.getX(), b.getX()));
        int minCz = SectionPos.blockToSectionCoord(Math.min(a.getZ(), b.getZ()));
        int maxCz = SectionPos.blockToSectionCoord(Math.max(a.getZ(), b.getZ()));
        for (int cx = minCx; cx <= maxCx; cx++) {
            for (int cz = minCz; cz <= maxCz; cz++) {
                level.getChunk(cx, cz);
            }
        }
    }

    private static void placeBatch(
            ServerPlayer player,
            ServerLevel level,
            BlockPos origin,
            Direction behind,
            List<BlockPos> positions,
            List<BlockState> states,
            int start
    ) {
        if (player.isRemoved() || level.getServer() == null) {
            return;
        }

        int end = Math.min(start + BLOCKS_PER_TICK, positions.size());
        for (int i = start; i < end; i++) {
            BlockPos pos = positions.get(i);
            BlockState existing = level.getBlockState(pos);
            if (!existing.isAir() && !existing.canBeReplaced()) {
                continue;
            }
            level.setBlock(pos, states.get(i), Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
        }

        if (end < positions.size()) {
            var server = level.getServer();
            server.schedule(new TickTask(server.getTickCount() + 1, () ->
                    placeBatch(player, level, origin, behind, positions, states, end)));
            return;
        }

        PacketDistributor.sendToPlayer(player, new PlayPortraitSoundPayload(origin));
        SpawnPortraitMod.LOGGER.info(
                "Placed {} and spawn portrait ({} blocks) {} blocks behind the player at {}",
                AlterOfTheDiddle.NAME,
                positions.size(),
                DISTANCE,
                origin
        );
    }
}
