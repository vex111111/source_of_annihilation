package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.mcreator.sourceofannihilation.NoSpawnList;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.world.level.entity.LevelCallback;

@Mixin(PersistentEntitySectionManager.class)
public abstract class PersistentEntitySectionManagerMixin<T extends EntityAccess> {
	
	@Final @Shadow protected LevelCallback<T> callbacks;

    @Inject(method = "addEntityUuid", at = @At("HEAD"), cancellable = true)
    private void addentityuuid(T entity,CallbackInfoReturnable<Boolean> cir) {
		if (!(entity instanceof Player aa) && (entity instanceof Entity aa)) {
				NoSpawnList myObj = NoSpawnList.getInstance();
				if (myObj.containsEntityType(aa) || SourceOfAnnihilationModVariables.MapVariables.get(aa.level()).sb) {
				cir.cancel();
			}
		}
	}
}