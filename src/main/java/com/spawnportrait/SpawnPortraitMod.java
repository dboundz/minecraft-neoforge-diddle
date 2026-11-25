package com.spawnportrait;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import com.spawnportrait.client.PortraitSoundPlayer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(SpawnPortraitMod.MOD_ID)
public class SpawnPortraitMod {
    public static final String MOD_ID = "spawnportrait";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SpawnPortraitMod(IEventBus modEventBus, ModContainer modContainer) {
        ModSounds.SOUNDS.register(modEventBus);
        ModStructures.STRUCTURE_TYPES.register(modEventBus);
        ModStructures.PIECE_TYPES.register(modEventBus);
        modEventBus.addListener(this::registerPayloads);
        NeoForge.EVENT_BUS.register(PortraitSoundTracker.class);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        if (FMLEnvironment.dist == Dist.CLIENT) {
            registrar.playToClient(
                    PlayPortraitSoundPayload.TYPE,
                    PlayPortraitSoundPayload.STREAM_CODEC,
                    (payload, context) -> PortraitSoundPlayer.apply(payload.pos(), payload.playing())
            );
        } else {
            registrar.playToClient(
                    PlayPortraitSoundPayload.TYPE,
                    PlayPortraitSoundPayload.STREAM_CODEC,
                    (payload, context) -> {
                    }
            );
        }
    }
}
