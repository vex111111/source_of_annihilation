package com.SourceofAnnihilation.mixin.CosmicRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.sourceofannihilation.SourceOfAnnihilationMod;
import net.mcreator.sourceofannihilation.client.model.CosmicBakeModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Mixin(value = ItemRenderer.class,priority = 0x7fffffff)
public abstract class CosmicRenderer {
@Inject(method = "render", at = @At("HEAD"),cancellable = true)
    public void onRenderItem(ItemStack stack, ItemDisplayContext transformType, boolean leftHanded, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, BakedModel model, CallbackInfo ci){
    if(model instanceof CosmicBakeModel cosmicBakeModel){
        ci.cancel();
        poseStack.pushPose();
        final CosmicBakeModel renderer = (CosmicBakeModel) ForgeHooksClient.handleCameraTransforms(poseStack, cosmicBakeModel, transformType, leftHanded);
            poseStack.translate(-0.5, -0.5, -0.5);
        renderer.renderItem(stack, transformType, poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
}