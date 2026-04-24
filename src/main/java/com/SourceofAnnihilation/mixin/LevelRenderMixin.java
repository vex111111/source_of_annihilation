package com.SourceofAnnihilation.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LevelRenderer.class)
public abstract class LevelRenderMixin {
    @Shadow
    protected abstract void renderChunkLayer(RenderType renderType, PoseStack poseStack, double x, double y, double z, Matrix4f projectionMatrix);
    @Shadow
    private ClientLevel level;
    @Shadow
    private int ticks;
    @Unique
    private static List<Vector3f> source_of_annihilation$sphereVertices = null;
    @Unique
    private static float source_of_annihilation$rotationAngle = 0;

    @Inject(method = "renderLevel", at = @At("HEAD"), cancellable = true)
    private void render(PoseStack poseStack, float p_109601_, long p_109602_, boolean p_109603_, Camera p_109604_, GameRenderer p_109605_, LightTexture p_109606_, Matrix4f p_254120_, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        if (SourceOfAnnihilationModVariables.MapVariables.get(world).sb) {
            RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
            renderSky(poseStack);

            Vec3 vec3 = p_109604_.getPosition();
            double d0 = vec3.x();
            double d1 = vec3.y();
            double d2 = vec3.z();
            this.renderChunkLayer(RenderType.solid(), poseStack, d0, d1, d2, p_254120_);
            this.renderChunkLayer(RenderType.cutoutMipped(), poseStack, d0, d1, d2, p_254120_);
            this.renderChunkLayer(RenderType.cutout(), poseStack, d0, d1, d2, p_254120_);
            this.renderChunkLayer(RenderType.translucent(), poseStack, d0, d1, d2, p_254120_);
            ci.cancel();
        }
    }

    private void renderSky(PoseStack poseStack){
        if (source_of_annihilation$sphereVertices == null) {
            source_of_annihilation$sphereVertices = source_of_annihilation$createSphere(2); // 细分级别2，三角形数量适中
        }

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        float renderTick = ticks;
        source_of_annihilation$rotationAngle = renderTick;

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        poseStack.translate(cameraPos.x, cameraPos.y, cameraPos.z);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        poseStack.mulPose(Axis.YP.rotationDegrees(source_of_annihilation$rotationAngle));
        poseStack.mulPose(Axis.XP.rotationDegrees(source_of_annihilation$rotationAngle * 0.3f));
        float scale = 2.0f;
        poseStack.scale(scale, scale, scale);
        Matrix4f matrix = poseStack.last().pose();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        for (Vector3f v : source_of_annihilation$sphereVertices) {
            float r = (float) (Math.sin(v.x() * 2 + source_of_annihilation$rotationAngle * 0.1) * 0.5 + 0.5);
            float g = (float) (Math.sin(v.y() * 2 + source_of_annihilation$rotationAngle * 0.2) * 0.5 + 0.5);
            float b = (float) (Math.sin(v.z() * 2 + source_of_annihilation$rotationAngle * 0.3) * 0.5 + 0.5);
            buffer.vertex(matrix, v.x(), v.y(), v.z())
                    .color(r, g, b, 0.8f)
                    .endVertex();
        }
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    @Unique
    private static List<Vector3f> source_of_annihilation$createSphere(int subdivisionLevel) {
        List<Vector3f> vertices = new ArrayList<>();
        float t = (float) ((1.0f + Math.sqrt(5.0f)) / 2.0f);
        List<Vector3f> baseV = new ArrayList<>();
        source_of_annihilation$addVertex(baseV, -1, t, 0);
        source_of_annihilation$addVertex(baseV, 1, t, 0);
        source_of_annihilation$addVertex(baseV, -1, -t, 0);
        source_of_annihilation$addVertex(baseV, 1, -t, 0);
        source_of_annihilation$addVertex(baseV, 0, -1, t);
        source_of_annihilation$addVertex(baseV, 0, 1, t);
        source_of_annihilation$addVertex(baseV, 0, -1, -t);
        source_of_annihilation$addVertex(baseV, 0, 1, -t);
        source_of_annihilation$addVertex(baseV, t, 0, -1);
        source_of_annihilation$addVertex(baseV, t, 0, 1);
        source_of_annihilation$addVertex(baseV, -t, 0, -1);
        source_of_annihilation$addVertex(baseV, -t, 0, 1);
        int[][] faces = {
                {0, 11, 5}, {0, 5, 1}, {0, 1, 7}, {0, 7, 10}, {0, 10, 11},
                {1, 5, 9}, {5, 11, 4}, {11, 10, 2}, {10, 7, 6}, {7, 1, 8},
                {3, 9, 4}, {3, 4, 2}, {3, 2, 6}, {3, 6, 8}, {3, 8, 9},
                {4, 9, 5}, {2, 4, 11}, {6, 2, 10}, {8, 6, 7}, {9, 8, 1}
        };
        for (int[] face : faces) {
            source_of_annihilation$subdivide(baseV.get(face[0]), baseV.get(face[1]), baseV.get(face[2]),
                    subdivisionLevel, vertices);
        }

        return vertices;
    }

    @Unique
    private static void source_of_annihilation$addVertex(List<Vector3f> list, float x, float y, float z) {
        list.add(new Vector3f(x, y, z).normalize());
    }
    
    @Unique
    private static void source_of_annihilation$subdivide(Vector3f v1, Vector3f v2, Vector3f v3, int depth, List<Vector3f> result) {
        if (depth <= 0) {
            result.add(v1);
            result.add(v2);
            result.add(v3);
        } else {
            Vector3f v12 = new Vector3f(v1).add(v2).mul(0.5f).normalize();
            Vector3f v23 = new Vector3f(v2).add(v3).mul(0.5f).normalize();
            Vector3f v31 = new Vector3f(v3).add(v1).mul(0.5f).normalize();
            source_of_annihilation$subdivide(v1, v12, v31, depth - 1, result);
            source_of_annihilation$subdivide(v2, v23, v12, depth - 1, result);
            source_of_annihilation$subdivide(v3, v31, v23, depth - 1, result);
            source_of_annihilation$subdivide(v12, v23, v31, depth - 1, result);
        }
    }
}