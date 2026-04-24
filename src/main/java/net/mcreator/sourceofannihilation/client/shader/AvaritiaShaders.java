package net.mcreator.sourceofannihilation.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.mcreator.sourceofannihilation.SourceOfAnnihilationMod;
import net.mcreator.sourceofannihilation.api.client.shader.CCShaderInstance;
import net.mcreator.sourceofannihilation.api.client.shader.CCUniform;
import net.mcreator.sourceofannihilation.util.AccessUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * Name: Avaritia-forge / AvaritiaShaders
 * Author: cnlimiter
 * CreateTime: 2023/9/18 1:37
 * Description:
 */
@Mod.EventBusSubscriber(modid = SourceOfAnnihilationMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class AvaritiaShaders {
    public static final float[] COSMIC_UVS = new float[40];
    public static boolean inventoryRender = false;

    public static int renderTime;
    public static float renderFrame;

    public static CCShaderInstance cosmicShader;

    public static CCUniform cosmicTime;
    public static CCUniform cosmicYaw;
    public static CCUniform cosmicPitch;
    public static CCUniform cosmicExternalScale;
    public static CCUniform cosmicOpacity;
    public static CCUniform cosmicUVs;

    public static RenderType COSMIC_RENDER_TYPE = RenderType.create("source_of_annihilation:cosmic",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false,
            RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> cosmicShader))
                    .setDepthTestState(AccessUtils.EQUAL_DEPTH_TEST)
                    .setLightmapState(AccessUtils.LIGHT_MAP)
                    .setTransparencyState(AccessUtils.TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(AccessUtils.BLOCK_SHEET_MIPPED)
                    .createCompositeState(true)
    );


    public static void onRegisterShaders(RegisterShadersEvent event){
        event.registerShader(CCShaderInstance.create(event.getResourceProvider(), new ResourceLocation("source_of_annihilation", "cosmic"), DefaultVertexFormat.BLOCK), e -> {
            cosmicShader = (CCShaderInstance)e;
            cosmicTime = Objects.requireNonNull(cosmicShader.getUniform("time"));
            cosmicYaw = Objects.requireNonNull(cosmicShader.getUniform("yaw"));
            cosmicPitch = Objects.requireNonNull(cosmicShader.getUniform("pitch"));
            cosmicExternalScale = Objects.requireNonNull(cosmicShader.getUniform("externalScale"));
            cosmicOpacity = Objects.requireNonNull(cosmicShader.getUniform("opacity"));
            cosmicUVs = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs"));
            cosmicTime.set((float)renderTime + renderFrame);
            cosmicShader.onApply(() -> {
                cosmicTime.set((float)renderTime + renderFrame);
            });
        });
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getInstance().isPaused() && event.phase == TickEvent.Phase.END) {
            ++renderTime;
        }
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (!Minecraft.getInstance().isPaused() && event.phase == TickEvent.Phase.START) {
            renderFrame = event.renderTickTime;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPre(final ScreenEvent.Render.Pre e) {
        AvaritiaShaders.inventoryRender = true;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPost(final ScreenEvent.Render.Post e) {
        AvaritiaShaders.inventoryRender = false;
    }
}
