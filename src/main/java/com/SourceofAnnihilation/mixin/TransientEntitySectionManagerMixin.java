package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.Entity;
import net.mcreator.sourceofannihilation.ProtectList;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.entity.EntityAccess;
import net.mcreator.sourceofannihilation.NoSpawnList;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.entity.player.Player;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.world.level.entity.TransientEntitySectionManager;

@Mixin(TransientEntitySectionManager.class)
public abstract class TransientEntitySectionManagerMixin<T extends EntityAccess> {

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void setRemoved(T entity,CallbackInfo ci) {
		if (!(entity instanceof Player aa) && (entity instanceof Entity aa)) {
				NoSpawnList myObj = NoSpawnList.getInstance();
				if (myObj.containsEntityType(aa) || SourceOfAnnihilationModVariables.MapVariables.get(aa.level()).sb) {
				ci.cancel();
			}
		}
	}
}