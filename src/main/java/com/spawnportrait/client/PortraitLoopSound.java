package com.spawnportrait.client;

import com.spawnportrait.ModSounds;
import com.spawnportrait.PortraitPattern;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;

public class PortraitLoopSound extends AbstractTickableSoundInstance {
    public PortraitLoopSound(BlockPos pos) {
        super(ModSounds.DIDDLY.get(), SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
        this.attenuation = SoundInstance.Attenuation.LINEAR;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.x = pos.getX() + 0.5;
        this.y = pos.getY() + PortraitPattern.HEIGHT / 2.0;
        this.z = pos.getZ() + 0.5;
    }

    @Override
    public void tick() {
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}
