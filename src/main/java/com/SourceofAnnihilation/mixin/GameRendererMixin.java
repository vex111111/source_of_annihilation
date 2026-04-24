package com.SourceofAnnihilation.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.sourceofannihilation.init.SourceOfAnnihilationModItems;
import net.mcreator.sourceofannihilation.item.SourceOfAnnihilationItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "renderItemInHand", at = @At("HEAD"))
    private void renderItemInHand(PoseStack p_109121_, Camera p_109122_, float p_109123_, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        LocalPlayer localPlayer = mc.player;
        if (level == null || localPlayer == null) return;
        ItemStack stack = localPlayer.getMainHandItem();
        if(stack.is(SourceOfAnnihilationModItems.SOURCE_OF_ANNIHILATION.get())){

        }
    }
}