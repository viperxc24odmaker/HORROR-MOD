package com.v24.horror;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;

public class HorrorClient implements ClientModInitializer {
    private static final Random RNG = Random.create();
    private static int timer;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.isPaused()) return;
            if (++timer > 240 + RNG.nextInt(900)) {
                timer = 0;
                if (RNG.nextInt(100) < 55) {
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ENTITY_ENDERMAN_STARE, 0.25f + RNG.nextFloat() * 0.4f, 0.55f + RNG.nextFloat() * 0.45f));
                }
                if (RNG.nextInt(100) < 22) {
                    client.player.sendMessage(net.minecraft.text.Text.literal("§8Something moved."), true);
                }
            }
        });
    }
}
