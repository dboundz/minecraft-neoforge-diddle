package com.spawnportrait;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSounds {
    private ModSounds() {
    }

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, SpawnPortraitMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> DIDDLY = SOUNDS.register(
            "diddly",
            () -> SoundEvent.createFixedRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(SpawnPortraitMod.MOD_ID, "diddly"),
                    320.0F
            )
    );
}
