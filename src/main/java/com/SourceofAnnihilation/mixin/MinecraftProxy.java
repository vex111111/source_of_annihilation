package com.SourceofAnnihilation.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.FontManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftProxy {
    @Invoker("handleKeybinds")
    void invokeHandleKeybinds();
    @Accessor("fontManager")
    FontManager getFontManager();
}
