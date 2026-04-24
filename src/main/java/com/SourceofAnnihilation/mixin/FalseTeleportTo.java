package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.entity.Entity;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.mcreator.sourceofannihilation.ProtectList;

@Mixin(Entity.class)
public abstract class FalseTeleportTo {

    @Inject(method = "teleportTo", at = @At("HEAD"), cancellable = true)
    public void teleporttoMixin(CallbackInfoReturnable<Boolean> cir) {

    	Entity entity = (Entity) (Object) this; 
    	ProtectList myObj = ProtectList.getInstance();
    	if (myObj.containsEntityType(entity)) {
    		cir.setReturnValue(false);
		}
    }
}
