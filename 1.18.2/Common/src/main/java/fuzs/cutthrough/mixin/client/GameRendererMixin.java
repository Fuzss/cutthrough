package fuzs.cutthrough.mixin.client;

import fuzs.cutthrough.CutThrough;
import fuzs.cutthrough.client.core.ClientAbstractions;
import fuzs.cutthrough.client.helper.GameRendererPickHelper;
import fuzs.cutthrough.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Nullable
    @Unique
    private HitResult combatnouveau$originalHitResult;

    @Inject(
            method = "pick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getEyePosition(F)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    public void pick$0(float partialTicks, CallbackInfo callback) {
        if (this.minecraft.hitResult != null) {
            Entity entity = this.minecraft.getCameraEntity();
            Objects.requireNonNull(entity, "camera entity is null");
            double pickRange = ClientAbstractions.INSTANCE.getPickRange(this.minecraft);
            HitResult hitResult = GameRendererPickHelper.pick(entity, pickRange, partialTicks);
            Vec3 eyePosition = entity.getEyePosition(partialTicks);
            if (hitResult.getLocation().distanceToSqr(eyePosition) > this.minecraft.hitResult.getLocation().distanceToSqr(eyePosition)) {
                this.combatnouveau$originalHitResult = this.minecraft.hitResult;
                this.minecraft.hitResult = hitResult;
            }
        }
    }

    @Inject(method = "pick", at = @At("TAIL"))
    public void pick$1(float partialTicks, CallbackInfo callback) {
        if (this.combatnouveau$originalHitResult != null) {
            if (this.minecraft.hitResult != null && this.minecraft.hitResult.getType() != HitResult.Type.ENTITY) {
                Entity entity = this.minecraft.getCameraEntity();
                Objects.requireNonNull(entity, "camera entity is null");
                Vec3 eyePosition = entity.getEyePosition(partialTicks);
                if (this.combatnouveau$originalHitResult.getLocation()
                        .distanceToSqr(eyePosition) < this.minecraft.hitResult.getLocation()
                        .distanceToSqr(eyePosition)) {
                    this.minecraft.hitResult = this.combatnouveau$originalHitResult;
                }
            }
            this.combatnouveau$originalHitResult = null;
        }
    }

    @SuppressWarnings("target")
    @Inject(method = "lambda$pick$56(Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private static void isPickable(Entity entity, CallbackInfoReturnable<Boolean> callback) {
        if (CutThrough.CONFIG.get(ClientConfig.class).targetAliveOnly && !entity.isAlive()) callback.setReturnValue(false);
    }
}
