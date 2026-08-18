package com.spawnportrait.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public final class PortraitSoundPlayer {
    private static PortraitLoopSound current;

    private PortraitSoundPlayer() {
    }

    public static void play(BlockPos pos) {
        var manager = Minecraft.getInstance().getSoundManager();
        if (current != null) {
            manager.stop(current);
        }
        current = new PortraitLoopSound(pos);
        manager.play(current);
    }
}
