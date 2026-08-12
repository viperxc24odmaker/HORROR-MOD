package com.v24.horror;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class HorrorMod implements ModInitializer {
    public static final String MOD_ID = "horror_mod";
    private static final Random RNG = Random.create();

    @Override
    public void onInitialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            handler.player.sendMessage(net.minecraft.text.Text.literal("§8...you are not alone."), false);
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.getRegistryKey() != World.OVERWORLD) return;
            for (PlayerEntity player : world.getPlayers()) {
                if (player.isSpectator() || player.isCreative()) continue;
                if (world.getTime() % 37 == 0 && RNG.nextInt(100) < 9) whisper(world, player);
                if (world.getTime() % 83 == 0 && RNG.nextInt(100) < 7) stalk(world, player);
            }
        });
    }

    private static void whisper(ServerWorld world, PlayerEntity player) {
        player.playSoundToPlayer(SoundEvents.AMBIENT_CAVE, SoundCategory.HOSTILE, 1.2f, 0.45f + RNG.nextFloat() * 0.35f);
        if (RNG.nextBoolean()) player.sendMessage(net.minecraft.text.Text.literal("§0§kYou hear breathing behind you."), true);
    }

    private static void stalk(ServerWorld world, PlayerEntity player) {
        Box area = player.getBoundingBox().expand(18, 8, 18);
        for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class, area, e -> e != player && e instanceof HostileEntity)) {
            if (e.squaredDistanceTo(player) < 7 * 7) continue;
            e.lookAt(net.minecraft.entity.EntityAnchorArgumentType.EntityAnchor.EYES, player.getPos());
            return;
        }
        if (RNG.nextInt(3) == 0) {
            player.playSoundToPlayer(SoundEvents.ENTITY_ENDERMAN_STARE, SoundCategory.HOSTILE, 0.9f, 0.65f);
        }
    }
}
