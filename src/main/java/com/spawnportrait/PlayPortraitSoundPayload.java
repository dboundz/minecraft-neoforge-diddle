package com.spawnportrait;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PlayPortraitSoundPayload(BlockPos pos, boolean playing) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayPortraitSoundPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(SpawnPortraitMod.MOD_ID, "play_sound"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayPortraitSoundPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    PlayPortraitSoundPayload::pos,
                    ByteBufCodecs.BOOL,
                    PlayPortraitSoundPayload::playing,
                    PlayPortraitSoundPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
