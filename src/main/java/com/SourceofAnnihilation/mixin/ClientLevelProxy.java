package com.SourceofAnnihilation.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraftforge.entity.PartEntity;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ClientLevel.class)
public interface ClientLevelProxy {
    @Accessor("tickingEntities")
    EntityTickList getTickingEntities();
    @Accessor("players")
    List<AbstractClientPlayer> getPlayers();
    @Accessor("partEntities")
    Int2ObjectMap<PartEntity<?>> getPartEntities();
    @Accessor("entityStorage")
    TransientEntitySectionManager<Entity> getEntityStorage();
    @Accessor("minecraft")
    Minecraft getMinecraft();
    @Accessor("tickingEntities")
    @Mutable
    void setEntityTickList(EntityTickList tickingEntities);
    @Invoker("getEntities")
    LevelEntityGetter<Entity> invokerGetEntities();
}