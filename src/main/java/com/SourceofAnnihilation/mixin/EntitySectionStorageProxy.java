package com.SourceofAnnihilation.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.Visibility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntitySectionStorage.class)
public interface EntitySectionStorageProxy<T> {
    @Accessor("intialSectionVisibility")
    Long2ObjectFunction<Visibility> getIntialSectionVisibility();
    @Accessor("sectionIds")
    LongSortedSet getSectionIds();
    @Final
    @Mutable
    @Accessor("sections")
    void setSections(Long2ObjectMap<? extends EntitySection<EntityAccess>> long2ObjectMap);
    @Accessor("sections")
    Long2ObjectMap<EntitySection<EntityAccess>> getSections();
}
