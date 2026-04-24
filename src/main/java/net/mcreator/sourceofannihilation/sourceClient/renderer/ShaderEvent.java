package net.mcreator.sourceofannihilation.sourceClient.renderer;

import com.SourceofAnnihilation.mixin.RenderStateShardProxy;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.mcreator.sourceofannihilation.SourceOfAnnihilationMod;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = "source_of_annihilation", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ShaderEvent {
    private static ShaderInstance rendertypeEndPortalShader;
    private static ShaderInstance cosmicShader;

    public static final RenderType END_PORTAL_OVERLAY = RenderType.create(
            "end_portal_overlay",
            DefaultVertexFormat.POSITION,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(ShaderEvent::getRendertypeEndPortalShader))
                    .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                            .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                            .add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false)
                            .build())
                    .setTransparencyState(RenderStateShardProxy.getTRANSLUCENT_TRANSPARENCY())
                    .setCullState(RenderStateShardProxy.getNO_CULL())
                    .setDepthTestState(RenderStateShardProxy.getLEQUAL_DEPTH_TEST())
                    .createCompositeState(false)
    );

    @SubscribeEvent(priority = EventPriority.LOWEST,receiveCanceled = true)
    public static void onShaders(RegisterShadersEvent event){
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation("source_of_annihilation","end_portal"), DefaultVertexFormat.POSITION), (p_172778_) -> {
                rendertypeEndPortalShader = p_172778_;
            });
            SourceOfAnnihilationMod.LOGGER.info("Shader Loaded!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ShaderInstance getRendertypeEndPortalShader() {
        return rendertypeEndPortalShader;
    }
}
