package com.SourceofAnnihilation.mixin;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.server.level.ServerPlayer;

@Mixin(ChunkMap.class)
public interface ChunkMapProxy {
 @Accessor("entityMap")
 Int2ObjectMap<?> getEntityMap();
 @Invoker("updatePlayerStatus")
 void invokeUpdatePlayerStatus(ServerPlayer player,boolean p_140194_);
}