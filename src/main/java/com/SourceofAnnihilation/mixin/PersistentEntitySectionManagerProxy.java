package com.SourceofAnnihilation.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.world.level.entity.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Final;

@Mixin(PersistentEntitySectionManager.class)
public interface PersistentEntitySectionManagerProxy  {
    @Accessor("visibleEntityStorage")
    EntityLookup<EntityAccess> getVisibleEntityStorage();
    @Accessor("sectionStorage")
    EntitySectionStorage<EntityAccess> getSectionStorage();
    @Accessor("knownUuids")
    Set<UUID> getKnownUuids();
    @Mutable
    @Final
    @Accessor("visibleEntityStorage")
    void setEntityLookup(EntityLookup<EntityAccess> entitylookup);
    @Accessor("chunkVisibility")
    Long2ObjectMap<Visibility> getChunkVisibility();
}