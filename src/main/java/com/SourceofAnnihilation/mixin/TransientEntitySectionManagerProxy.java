package com.SourceofAnnihilation.mixin;

import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.level.entity.EntitySectionStorage;

@Mixin(TransientEntitySectionManager.class)
public interface TransientEntitySectionManagerProxy {
    @Accessor("entityStorage")
    EntityLookup<EntityAccess> getEntityStorage();
    @Accessor("sectionStorage")
    EntitySectionStorage<EntityAccess> getSectionStorage();
}