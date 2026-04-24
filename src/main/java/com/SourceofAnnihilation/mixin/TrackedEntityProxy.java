package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.server.level.ServerPlayer;

@Mixin(targets = "net.minecraft.server.level.ChunkMap$TrackedEntity")
public interface TrackedEntityProxy {
    @Invoker("removePlayer")
    void invokeRemovePlayer(ServerPlayer player);
    
    @Invoker("broadcastRemoved")
    void invokeBroadcastRemoved();
}