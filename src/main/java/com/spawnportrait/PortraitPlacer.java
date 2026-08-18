package com.spawnportrait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Places the altar and 2D mural inside the current structure chunk box.
 */
public final class PortraitPlacer {
    private static final int FLAGS = Block.UPDATE_CLIENTS;

    private PortraitPlacer() {
    }

    public static void place(
            WorldGenLevel level,
            BlockPos origin,
            Direction facing,
            BoundingBox chunkBox,
            RandomSource random
    ) {
        Direction left = facing.getCounterClockWise();
        AlterOfTheDiddle.place(level, origin, facing, left, chunkBox, random);

        BlockPos leftEdge = origin.above(AlterOfTheDiddle.HEIGHT).relative(left, PortraitPattern.WIDTH / 2);
        for (int row = 0; row < PortraitPattern.HEIGHT; row++) {
            for (int col = 0; col < PortraitPattern.WIDTH; col++) {
                Block block = PortraitPattern.blockAt(row, col);
                if (block == Blocks.AIR) {
                    continue;
                }
                BlockPos pos = leftEdge
                        .relative(left.getOpposite(), col)
                        .above(PortraitPattern.HEIGHT - 1 - row);
                if (!chunkBox.isInside(pos)) {
                    continue;
                }
                BlockState existing = level.getBlockState(pos);
                if (!existing.isAir() && !existing.canBeReplaced()) {
                    continue;
                }
                level.setBlock(pos, block.defaultBlockState(), FLAGS);
            }
        }
    }
}
