package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.mcreator.sourceofannihilation.NoSpawnList;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;

@Mixin(ClassInstanceMultiMap.class)
public abstract class ClassInstanceMultiMapMixin<T> {
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void addentityuuid(T t,CallbackInfoReturnable<Boolean> cir) {
    	if (t instanceof EntityAccess entity){
		if (!(entity instanceof Player aa) && (entity instanceof Entity aa)) {
				NoSpawnList myObj = NoSpawnList.getInstance();
				if (myObj.containsEntityType(aa) || SourceOfAnnihilationModVariables.MapVariables.get(aa.level()).sb) {
				cir.cancel();
				}
			}
		}
	}
}
