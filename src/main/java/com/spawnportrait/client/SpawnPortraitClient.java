package com.spawnportrait.client;

import com.spawnportrait.PlayPortraitSoundPayload;
import com.spawnportrait.SpawnPortraitMod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@Mod(value = SpawnPortraitMod.MOD_ID, dist = Dist.CLIENT)
public class SpawnPortraitClient {
    public SpawnPortraitClient(IEventBus modEventBus) {
        modEventBus.addListener(this::registerClientPayloads);
    }

    private void registerClientPayloads(RegisterClientPayloadHandlersEvent event) {
        event.register(PlayPortraitSoundPayload.TYPE, (payload, context) ->
                PortraitSoundPlayer.play(payload.pos()));
    }
}
