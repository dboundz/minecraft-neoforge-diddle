package com.spawnportrait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class AlterOfTheDiddlePiece extends StructurePiece {
    private final BlockPos origin;
    private final Direction facing;

    public AlterOfTheDiddlePiece(BlockPos origin, Direction facing) {
        super(ModStructures.ALTER_OF_THE_DIDDLE_PIECE.get(), 0, boundingBox(origin, facing));
        this.origin = origin;
        this.facing = facing;
        this.setOrientation(facing);
    }

    public AlterOfTheDiddlePiece(CompoundTag tag) {
        super(ModStructures.ALTER_OF_THE_DIDDLE_PIECE.get(), tag);
        BlockPos readOrigin = NbtUtils.readBlockPos(tag, "Origin").orElse(null);
        this.origin = readOrigin != null ? readOrigin : this.boundingBox.getCenter();
        Direction readFacing = Direction.byName(tag.getString("Facing"));
        this.facing = readFacing != null ? readFacing : Direction.SOUTH;
    }

    public BlockPos origin() {
        return this.origin;
    }

    public Direction facing() {
        return this.facing;
    }

    public BlockPos chestPos() {
        return AlterOfTheDiddle.chestPos(this.origin, this.facing);
    }

    static BoundingBox boundingBox(BlockPos origin, Direction facing) {
        Direction left = facing.getCounterClockWise();
        Direction right = left.getOpposite();
        int halfWidth = PortraitPattern.WIDTH / 2;
        BlockPos leftBack = origin.relative(left, halfWidth);
        BlockPos rightFront = origin
                .relative(right, halfWidth - 1)
                .relative(facing, AlterOfTheDiddle.DEPTH - 1);
        int minX = Math.min(leftBack.getX(), rightFront.getX());
        int maxX = Math.max(leftBack.getX(), rightFront.getX());
        int minZ = Math.min(leftBack.getZ(), rightFront.getZ());
        int maxZ = Math.max(leftBack.getZ(), rightFront.getZ());
        int maxY = origin.getY() + AlterOfTheDiddle.HEIGHT + PortraitPattern.HEIGHT - 1;
        return new BoundingBox(minX, origin.getY(), minZ, maxX, maxY, maxZ);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.put("Origin", NbtUtils.writeBlockPos(this.origin));
        tag.putString("Facing", this.facing.getSerializedName());
    }

    @Override
    public void postProcess(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox chunkBB,
            ChunkPos chunkPos,
            BlockPos referencePos
    ) {
        PortraitPlacer.place(level, this.origin, this.facing, chunkBB, random);
    }
}
