package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.Entity;
import net.mcreator.sourceofannihilation.NoSpawnList;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import java.util.Optional;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> {

@Inject(method = "create(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;", at = @At("HEAD"), cancellable = true)
    private void onCreate(Level level, CallbackInfoReturnable<T> cir) {
            EntityType<?> entityType = (EntityType<?>) (Object) this;
            if (NoSpawnList.getInstance().containsEntityType(entityType) || SourceOfAnnihilationModVariables.MapVariables.get(level).sb) {
			if(!(entityType == EntityType.PLAYER))
                cir.setReturnValue(null);
                cir.cancel();
        }
    }
/*@Inject(method = "create(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private static void onCreate(CompoundTag tag, Level level, CallbackInfoReturnable<Optional<Entity>> cir) {
            String entityId = tag.getString("id");
            if (NoSpawnList.getInstance().containsEntityType(entityId) || SourceOfAnnihilationModVariables.MapVariables.get(level).sb) {
			if(!(entityId == "minecraft:player"))
                cir.cancel();
        }
    }*/
}