package com.SourceofAnnihilation.mixin;

import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntitySection.class)
public interface EntitySectionProxy<T> {
    @Final
    @Mutable
    @Accessor("storage")
    void setStorage(ClassInstanceMultiMap<EntityAccess> storage);
    @Accessor("storage")
    ClassInstanceMultiMap<EntityAccess> getStorage();
}
