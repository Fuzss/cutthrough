package fuzs.cutthrough.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import fuzs.cutthrough.CutThrough;
import fuzs.cutthrough.client.helper.GameRendererPickHelper;
import fuzs.cutthrough.config.ClientConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin {

    @ModifyVariable(
            method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;", at = @At(
            value = "STORE"
    )
    )
    private HitResult pick$0(HitResult hitResult, Entity entity, double blockInteractionRange, double entityInteractionRange, float partialTick, @Share(
            "originalHitResult"
    ) LocalRef<HitResult> originalHitResult) {
        double pickRange = Math.max(blockInteractionRange, entityInteractionRange);
        HitResult newHitResult = GameRendererPickHelper.pick(entity, pickRange, partialTick);
        Vec3 eyePosition = entity.getEyePosition(partialTick);
        if (newHitResult.getLocation().distanceToSqr(eyePosition) >
                hitResult.getLocation().distanceToSqr(eyePosition)) {
            originalHitResult.set(hitResult);
            return newHitResult;
        } else {
            return hitResult;
        }
    }

    @ModifyReturnValue(
            method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;",
            at = @At("TAIL")
    )
    private HitResult pick$1(HitResult hitResult, Entity entity, double blockInteractionRange, double entityInteractionRange, float partialTick, @Share(
            "originalHitResult"
    ) LocalRef<HitResult> originalHitResult) {
        if (originalHitResult.get() != null && hitResult.getType() != HitResult.Type.ENTITY) {
            Vec3 eyePosition = entity.getEyePosition(partialTick);
            if (originalHitResult.get().getLocation().distanceToSqr(eyePosition) <
                    hitResult.getLocation().distanceToSqr(eyePosition)) {
                return originalHitResult.get();
            }
        }

        return hitResult;
    }

    @Inject(method = "lambda$pick$57(Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private static void isPickable(Entity entity, CallbackInfoReturnable<Boolean> callback) {
        if (CutThrough.CONFIG.get(ClientConfig.class).targetAliveOnly && !entity.isAlive()) {
            callback.setReturnValue(false);
        }
    }
}
