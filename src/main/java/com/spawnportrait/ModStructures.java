package com.spawnportrait;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModStructures {
    private ModStructures() {
    }

    public static final ResourceKey<Structure> ALTER_OF_THE_DIDDLE_KEY = ResourceKey.create(
            Registries.STRUCTURE,
            ResourceLocation.fromNamespaceAndPath(SpawnPortraitMod.MOD_ID, "alter_of_the_diddle")
    );

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, SpawnPortraitMod.MOD_ID);

    public static final DeferredRegister<StructurePieceType> PIECE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, SpawnPortraitMod.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<AlterOfTheDiddleStructure>> ALTER_OF_THE_DIDDLE =
            STRUCTURE_TYPES.register("alter_of_the_diddle", () -> () -> AlterOfTheDiddleStructure.CODEC);

    public static final DeferredHolder<StructurePieceType, StructurePieceType> ALTER_OF_THE_DIDDLE_PIECE =
            PIECE_TYPES.register("alter_of_the_diddle", () -> (StructurePieceType.ContextlessType) AlterOfTheDiddlePiece::new);
}
