package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.mcreator.sourceofannihilation.ProtectList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import com.SourceofAnnihilation.mixin.MinecraftProxy;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(
        method = "render",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onrender(GuiGraphics p_282884_, float p_282611_, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) {
            return;
        }
        Player player = client.player;
        if (player == null) {
            return;
        }
        ProtectList protectList = ProtectList.getInstance();
        if (SourceOfAnnihilationModVariables.MapVariables.get(Minecraft.getInstance().level).invul && protectList.containsEntityType(player)){
			Minecraft.getInstance().setScreen(null);
        	player.closeContainer();
        	MinecraftProxy proxy = (MinecraftProxy) Minecraft.getInstance();
			proxy.invokeHandleKeybinds();
            ci.cancel();
        }
    }
}