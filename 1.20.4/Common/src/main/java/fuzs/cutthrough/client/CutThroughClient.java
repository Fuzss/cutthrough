package fuzs.cutthrough.client;

import fuzs.cutthrough.CutThrough;
import fuzs.cutthrough.config.ClientConfig;
import fuzs.cutthrough.mixin.client.accessor.MultiPlayerGameModeAccessor;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.entity.player.InteractionInputEvents;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class CutThroughClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        InteractionInputEvents.ATTACK.register((Minecraft minecraft, LocalPlayer player, HitResult hitResult) -> {
            if (!CutThrough.CONFIG.get(ClientConfig.class).targetAliveOnly) return EventResult.PASS;
            // prevents breaking a block without collision (like tall grass) when hitting an entity through it and the entity dies or goes out of range
            // vanilla doesn't update the block breaking delay, so the block is broken in the next tick after the entity is gone when the block becomes the new pick result
            if (hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity()
                    .isAlive()) {
                ((MultiPlayerGameModeAccessor) minecraft.gameMode).combatnouveau$setDestroyDelay(5);
            }
            return EventResult.PASS;
        });
    }
}
