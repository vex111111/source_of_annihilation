package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.entity.EntityAccess;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.level.entity.EntitySection;

@Mixin(targets = "net.minecraft.world.level.entity.TransientEntitySectionManager$Callback")
public interface TCallbackProxy {
    @Accessor("currentSectionKey")
    long getCurrentSectionKey();
    @Accessor("currentSection")
    EntitySection<EntityAccess> getCurrentSection();
}