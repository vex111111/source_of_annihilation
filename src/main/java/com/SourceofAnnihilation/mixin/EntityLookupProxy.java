package com.SourceofAnnihilation.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.level.entity.EntityLookup;
import java.util.UUID;
import java.util.Map;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.level.entity.EntityAccess;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Final;

@Mixin(EntityLookup.class)
public interface EntityLookupProxy<T extends EntityAccess> {
    @Accessor("byId")
    Int2ObjectMap<T> getById();
    @Accessor("byUuid")
    Map<UUID, T> getByUuid();
    @Mutable
    @Final
    @Accessor("byId")
    void setById(Int2ObjectMap<T> int2ObjectMap);
    @Mutable
    @Final
    @Accessor("byUuid")
    void setByUuid(Map<UUID, T> map);
}
