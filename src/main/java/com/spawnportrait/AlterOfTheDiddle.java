package com.spawnportrait;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Pedestal under the mural. Always the same layout; the chest offering changes.
 */
public final class AlterOfTheDiddle {
    public static final String NAME = "Alter of the Diddle";
    public static final int WIDTH = 11;
    public static final int DEPTH = 7;
    public static final int HEIGHT = 4;

    private static final int HALF_WIDTH = WIDTH / 2;
    private static final int CHEST_SLOT = 13;

    private static final Item[] OFFERINGS = {
            Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS,
            Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS,
            Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
            Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
            Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS,
            Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS,
            Items.TURTLE_HELMET,
            Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD,
            Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD,
            Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE,
            Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE,
            Items.BOW, Items.CROSSBOW, Items.TRIDENT, Items.MACE
    };

    private static final BlockState BLACK = Blocks.BLACK_CONCRETE.defaultBlockState();
    private static final BlockState PINK = Blocks.PINK_CONCRETE.defaultBlockState();
    private static final BlockState QUARTZ = Blocks.SMOOTH_QUARTZ.defaultBlockState();
    private static final BlockState CHISELED = Blocks.CHISELED_QUARTZ_BLOCK.defaultBlockState();
    private static final BlockState PILLAR = Blocks.QUARTZ_PILLAR.defaultBlockState()
            .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
    private static final BlockState GOLD = Blocks.GOLD_BLOCK.defaultBlockState();
    private static final BlockState GILDED = Blocks.GILDED_BLACKSTONE.defaultBlockState();
    private static final BlockState BLACKSTONE = Blocks.POLISHED_BLACKSTONE.defaultBlockState();

    private AlterOfTheDiddle() {
    }

    public static BlockPos chestPos(BlockPos origin, Direction facing) {
        return origin.relative(facing, 2).above(HEIGHT - 1);
    }

    public static void place(
            WorldGenLevel level,
            BlockPos origin,
            Direction facing,
            Direction left,
            BoundingBox chunkBox,
            RandomSource random
    ) {
        Direction right = left.getOpposite();

        for (int across = -HALF_WIDTH; across <= HALF_WIDTH; across++) {
            for (int front = 0; front < DEPTH; front++) {
                set(level, chunkBox, at(origin, left, facing, across, 0, front), foundationAt(across, front));
            }
        }

        for (int across = -HALF_WIDTH; across <= HALF_WIDTH; across++) {
            for (int front = 0; front < DEPTH - 1; front++) {
                set(level, chunkBox, at(origin, left, facing, across, 1, front), midAt(across, front));
            }
        }

        for (int across = -HALF_WIDTH + 1; across <= HALF_WIDTH - 1; across++) {
            for (int front = 0; front < DEPTH - 2; front++) {
                set(level, chunkBox, at(origin, left, facing, across, 2, front), riserAt(across, front));
            }
        }

        BlockState stairs = Blocks.SMOOTH_QUARTZ_STAIRS.defaultBlockState()
                .setValue(StairBlock.FACING, facing);
        for (int across = -3; across <= 3; across++) {
            set(level, chunkBox, at(origin, left, facing, across, 1, DEPTH - 1), stairs);
        }

        set(level, chunkBox, at(origin, left, facing, -HALF_WIDTH + 1, 2, 1), PILLAR);
        set(level, chunkBox, at(origin, left, facing, HALF_WIDTH - 1, 2, 1), PILLAR);
        set(level, chunkBox, at(origin, left, facing, -2, 2, DEPTH - 3), PINK);
        set(level, chunkBox, at(origin, left, facing, 2, 2, DEPTH - 3), PINK);

        for (int across = -2; across <= 2; across++) {
            for (int front = 0; front <= 2; front++) {
                set(level, chunkBox, at(origin, left, facing, across, 3, front), QUARTZ);
            }
        }

        BlockPos chestPos = chestPos(origin, facing);
        if (set(level, chunkBox, chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing))) {
            fillChest(level, chestPos, random);
        }

        set(level, chunkBox, chestPos.relative(left), Blocks.SOUL_LANTERN.defaultBlockState());
        set(level, chunkBox, chestPos.relative(right), Blocks.SOUL_LANTERN.defaultBlockState());

        BlockPos plaque = at(origin, left, facing, 0, 2, DEPTH - 3);
        BlockPos signPos = plaque.relative(facing);
        if (set(level, chunkBox, signPos, Blocks.OAK_WALL_SIGN.defaultBlockState().setValue(WallSignBlock.FACING, facing))) {
            if (level.getBlockEntity(signPos) instanceof SignBlockEntity sign) {
                writeSignText(level, sign);
            }
        }
    }

    private static BlockState foundationAt(int across, int front) {
        boolean edge = Math.abs(across) == HALF_WIDTH || front == 0 || front == DEPTH - 1;
        boolean goldTooth = Math.abs(across) == HALF_WIDTH && (front == 0 || front == DEPTH - 1);
        if (goldTooth) {
            return GOLD;
        }
        return edge ? BLACK : BLACKSTONE;
    }

    private static BlockState midAt(int across, int front) {
        if (front == 2) {
            return BLACK;
        }
        if (Math.abs(across) == HALF_WIDTH) {
            return CHISELED;
        }
        return QUARTZ;
    }

    private static BlockState riserAt(int across, int front) {
        if (across == 0 && front == 1) {
            return GILDED;
        }
        if (Math.abs(across) == HALF_WIDTH - 1) {
            return CHISELED;
        }
        return QUARTZ;
    }

    private static void writeSignText(WorldGenLevel level, SignBlockEntity sign) {
        SignText text = new SignText()
                .setMessage(0, Component.literal("Alter of the"))
                .setMessage(1, Component.literal("Diddle"))
                .setColor(DyeColor.PINK)
                .setHasGlowingText(true);
        Tag encoded = SignText.DIRECT_CODEC
                .encodeStart(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), text)
                .getOrThrow();
        CompoundTag tag = new CompoundTag();
        tag.put("front_text", encoded);
        tag.putBoolean("is_waxed", true);
        sign.loadCustomOnly(tag, level.registryAccess());
        sign.setChanged();
    }

    private static void fillChest(WorldGenLevel level, BlockPos chestPos, RandomSource random) {
        if (!(level.getBlockEntity(chestPos) instanceof ChestBlockEntity chest)) {
            return;
        }

        chest.applyComponents(
                DataComponentMap.EMPTY,
                DataComponentPatch.builder()
                        .set(DataComponents.CUSTOM_NAME, Component.literal(NAME))
                        .build()
        );
        chest.setItem(CHEST_SLOT, randomOffering(level, random));
        chest.setChanged();
    }

    private static ItemStack randomOffering(WorldGenLevel level, RandomSource random) {
        ItemStack stack = new ItemStack(OFFERINGS[random.nextInt(OFFERINGS.length)]);
        level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getRandom(random)
                .ifPresent(enchant -> {
                    Enchantment value = enchant.value();
                    int min = value.getMinLevel();
                    int max = Math.max(min, value.getMaxLevel());
                    stack.enchant(enchant, min + random.nextInt(max - min + 1));
                });
        return stack;
    }

    private static BlockPos at(BlockPos origin, Direction left, Direction front, int across, int y, int depth) {
        return origin.relative(left, across).above(y).relative(front, depth);
    }

    private static boolean set(WorldGenLevel level, BoundingBox chunkBox, BlockPos pos, BlockState state) {
        if (!chunkBox.isInside(pos)) {
            return false;
        }
        level.setBlock(pos, state, Block.UPDATE_CLIENTS);
        return true;
    }
}
