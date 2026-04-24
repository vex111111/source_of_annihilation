package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.mcreator.sourceofannihilation.NoSpawnList;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.world.level.entity.LevelCallback;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LevelCallback.class)
@Implements(@Interface(iface = LevelCallback.class, prefix = "mixin$"))
public interface LevelCallbackMixin<T> {
@Overwrite
    default void onCreated(T t) {
    	if (t instanceof EntityAccess entity){
		if (!(entity instanceof Player aa) && (entity instanceof Entity aa)) {
				NoSpawnList myObj = NoSpawnList.getInstance();
				if (myObj.containsEntityType(aa) || SourceOfAnnihilationModVariables.MapVariables.get(aa.level()).sb) {
				return;
				}
			}
		}
	}
}
