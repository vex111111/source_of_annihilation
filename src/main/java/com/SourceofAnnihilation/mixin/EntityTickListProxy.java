package com.SourceofAnnihilation.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityTickList.class)
public interface EntityTickListProxy {
    @Accessor("active")
    Int2ObjectMap<Entity> getActive();
    @Accessor("passive")
    Int2ObjectMap<Entity> getPassive();
    @Accessor("iterated")
    Int2ObjectMap<Entity> getIterated();
}
