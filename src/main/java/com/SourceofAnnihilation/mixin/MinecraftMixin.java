package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.mcreator.sourceofannihilation.ProtectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import javax.annotation.Nullable;
import com.SourceofAnnihilation.mixin.MinecraftProxy;
import net.minecraft.client.gui.screens.Overlay;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Inject(method = "setScreen",at = @At("HEAD"),cancellable = true)
    private void render(@Nullable Screen screen, CallbackInfo ci) {
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
            ci.cancel();
        }
        if (screen instanceof DeathScreen && protectList.containsEntityType(player)) {
            ci.cancel();
            return;
        }
    }
    @Inject(method = "tick",at = @At("HEAD"),cancellable = true)
	private void tick(CallbackInfo ci) {
    	Minecraft client = Minecraft.getInstance();
    	MinecraftProxy proxy = (MinecraftProxy) client;
    	if (client.level == null || client.player == null) {
            return;
        }
        Overlay overlay = client.getOverlay();
        ProtectList protectList = ProtectList.getInstance();
       	if (SourceOfAnnihilationModVariables.MapVariables.get(Minecraft.getInstance().level).invul && protectList.containsEntityType(client.player)){
    	client.screen = null;overlay = null;
    	if(client.screen != null || overlay != null){
    		proxy.invokeHandleKeybinds();
    		System.out.println("jiba");
    		}
       	}
    }
    /*@Inject(method = "tick",at = @At("HEAD"),cancellable = true)
    private void handleKeybinds(CallbackInfo ci){
    	
    }*/
}