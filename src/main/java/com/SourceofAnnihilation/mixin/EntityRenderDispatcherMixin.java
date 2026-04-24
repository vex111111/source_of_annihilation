package com.SourceofAnnihilation.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Shadow
    public Camera camera;

    @Inject(method = "getRenderer(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;", at = @At("HEAD"), cancellable = true)
    private void onGetRenderer(Entity entity, CallbackInfoReturnable<EntityRenderer<?>> cir) {
        if (entity == null) {
            System.out.println("getRenderer entity is null");
            cir.cancel();
        }
    }

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void shouldRender(E entity, Frustum f, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (entity == null) {
            System.out.println("shouldRender entity is null");
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "distanceToSqr(Lnet/minecraft/world/entity/Entity;)D", at = @At("HEAD"), cancellable = true)
    private void distanceToSqr(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (camera == null) {

        }
    }
}