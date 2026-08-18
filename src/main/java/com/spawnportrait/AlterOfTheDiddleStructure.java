package com.spawnportrait;

import java.util.Optional;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class AlterOfTheDiddleStructure extends Structure {
    public static final MapCodec<AlterOfTheDiddleStructure> CODEC = simpleCodec(AlterOfTheDiddleStructure::new);
    private static final int MIN_SURFACE_Y = 60;

    public AlterOfTheDiddleStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int y = context.chunkGenerator().getFirstOccupiedHeight(
                x,
                z,
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState()
        );
        if (y < MIN_SURFACE_Y) {
            return Optional.empty();
        }
        BlockPos origin = new BlockPos(x, y, z);
        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(context.random());
        return Optional.of(new Structure.GenerationStub(origin, builder ->
                builder.addPiece(new AlterOfTheDiddlePiece(origin, facing))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.ALTER_OF_THE_DIDDLE.get();
    }
}
