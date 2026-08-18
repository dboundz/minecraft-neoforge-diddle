package com.spawnportrait;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(SpawnPortraitMod.MOD_ID)
public class SpawnPortraitMod {
    public static final String MOD_ID = "spawnportrait";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SpawnPortraitMod(IEventBus modEventBus, ModContainer modContainer) {
        ModSounds.SOUNDS.register(modEventBus);
        modEventBus.addListener(this::registerPayloads);
        NeoForge.EVENT_BUS.register(this);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(
                PlayPortraitSoundPayload.TYPE,
                PlayPortraitSoundPayload.STREAM_CODEC
        );
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        schedulePlace(event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        schedulePlace(event.getEntity());
    }

    private static void schedulePlace(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        var server = serverPlayer.level().getServer();
        if (server == null) {
            return;
        }
        server.execute(() -> PortraitPlacer.placeBehindPlayer(serverPlayer));
    }
}
