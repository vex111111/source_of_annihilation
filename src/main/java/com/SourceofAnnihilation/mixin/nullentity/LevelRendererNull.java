package com.SourceofAnnihilation.mixin.nullentity;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.injection.Inject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererNull {
@Shadow private ClientLevel level;
	/*@ModifyVariable(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z"), ordinal = 0, require = 0)
    private Entity modifyEntity(Entity original) {
        return original == null ? null : original;
    }*/
        @Inject(method = "renderLevel",at = @At("HEAD"),cancellable = true)
    private void render(PoseStack p_109600_, float p_109601_, long p_109602_, boolean p_109603_, Camera p_109604_, GameRenderer p_109605_, LightTexture p_109606_, Matrix4f p_254120_,CallbackInfo ci) {
		    	for(Entity entity : this.level.entitiesForRendering()){
    		if(entity == null){
    		System.out.println("man! what can I say? entity is null.");
    		ci.cancel();
    		}
    	}
    }
        @Redirect(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;hasIndirectPassenger(Lnet/minecraft/world/entity/Entity;)Z"
        )
    )
    private boolean onHasIndirectPassenger(Entity entity, Entity passenger) {
        if (entity == null || passenger == null) {
            return false;
        }
        return entity.hasIndirectPassenger(passenger);
    }
    
    @Redirect(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z"
        )
    )
    private boolean onShouldRender(EntityRenderDispatcher dispatcher, Entity entity, 
                                   Frustum frustum, double camX, double camY, double camZ) {
        if (entity == null) {
            return false;
        }
        return dispatcher.shouldRender(entity, frustum, camX, camY, camZ);
    }

}
