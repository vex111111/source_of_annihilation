package com.SourceofAnnihilation.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.level.entity.EntityInLevelCallback;

@Mixin(Entity.class)
public interface EntityProxy {
    @Accessor("removalReason")
    Entity.RemovalReason getRemovalReason();
    @Accessor("removalReason")
    void setRemovalReason(Entity.RemovalReason reason);
    @Accessor("levelCallback")
    EntityInLevelCallback getEntityInLevelCallback();
    @Accessor("entityData")
    SynchedEntityData  getEntityData();
}
