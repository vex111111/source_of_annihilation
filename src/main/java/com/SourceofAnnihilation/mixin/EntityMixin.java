package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.Entity;
import net.mcreator.sourceofannihilation.ProtectList;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.mcreator.sourceofannihilation.NoSpawnList;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
    private int remainingFireTicks;

    @Inject(method = "teleportTo(DDD)V", at = @At("HEAD"), cancellable = true)
    private void ofa(double p_19887_, double p_19888_, double p_19889_, CallbackInfo ci) {
    	Entity entity = (Entity) (Object) this; 

        if (ProtectList.getInstance().containsEntityType(entity)){
            ci.cancel();
		}
	}
	@Inject(method = "setLevelCallback", at = @At("HEAD"), cancellable = true)
    private void wx(EntityInLevelCallback callback, CallbackInfo ci) {
    	Entity entity = (Entity) (Object) this; 

		if (!(entity instanceof Player) && callback != EntityInLevelCallback.NULL) {
				NoSpawnList myObj = NoSpawnList.getInstance();
				if (myObj.containsEntityType(entity) || SourceOfAnnihilationModVariables.MapVariables.get(entity.level()).sb) {
				ci.cancel();
			}
		}
	}
	@Inject(method = "getRemainingFireTicks", at = @At("HEAD"), cancellable = true)
    private void wf(CallbackInfoReturnable<Integer> cir) {
    	Entity entity = (Entity) (Object) this; 

        if (ProtectList.getInstance().containsEntityType(entity) && SourceOfAnnihilationModVariables.MapVariables.get(entity.level()).nofire){
			this.remainingFireTicks = -10;
            cir.setReturnValue(-10);
		}
	}
		@Inject(method = "isOnFire", at = @At("HEAD"), cancellable = true)
    private void fu(CallbackInfoReturnable<Boolean> cir) {
    	Entity entity = (Entity) (Object) this; 

        if (ProtectList.getInstance().containsEntityType(entity) && SourceOfAnnihilationModVariables.MapVariables.get(entity.level()).nofire){
            cir.setReturnValue(false);
		}
	}
	@Inject(method = "setRemoved", at = @At("HEAD"), cancellable = true)
	private void setRemoved(CallbackInfo ci) {
		Entity entity = (Entity) (Object) this;
		if (ProtectList.getInstance().containsEntityType(entity) && !(entity instanceof Player))
			ci.cancel();
	}
}
