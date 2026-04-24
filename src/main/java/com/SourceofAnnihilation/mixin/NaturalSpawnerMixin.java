package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.NaturalSpawner;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Mob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {
	@Inject(method = "getMobForSpawn", at = @At("HEAD"),cancellable = true)
	@Nullable
   private static void getMobForSpawn(ServerLevel level, EntityType<?> p_46990_,CallbackInfoReturnable<Mob> cir) {
   	if (SourceOfAnnihilationModVariables.MapVariables.get(level).sb){
   		cir.setReturnValue(null);
   		cir.cancel();
   	}
   }
}
