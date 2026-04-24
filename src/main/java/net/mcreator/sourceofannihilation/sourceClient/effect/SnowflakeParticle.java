package net.mcreator.sourceofannihilation.sourceClient.effect;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

import java.util.Random;

public class SnowflakeParticle {
    private float x, y;
    private float vx, vy;
    private float size;
    private float rotation;
    private float rotationSpeed;
    private float alpha;
    private int lifetime;
    private int age;
    private float zRotation;
    private float zRotationSpeed;
    
    private static final Random RANDOM = new Random();
    private static final int CIRCLE_SEGMENTS = 8;
    
    public SnowflakeParticle(int screenWidth, int screenHeight) {
        this.x = RANDOM.nextFloat() * screenWidth;
        this.y = -10;
        this.vx = (RANDOM.nextFloat() - 0.5f) * 1.5f;
        this.vy = RANDOM.nextFloat() * 1.5f + 0.5f;
        this.size = RANDOM.nextFloat() * 6 + 3;
        this.rotation = RANDOM.nextFloat() * 360;
        this.rotationSpeed = (RANDOM.nextFloat() - 0.5f) * 3;
        this.zRotation = RANDOM.nextFloat() * 360;
        this.zRotationSpeed = (RANDOM.nextFloat() - 0.5f) * 5;
        this.alpha = RANDOM.nextFloat() * 0.4f + 0.6f;
        this.lifetime = 150 + RANDOM.nextInt(100);
        this.age = 0;
    }
    
    public void update() {
        x += vx;
        y += vy;
        rotation += rotationSpeed;
        zRotation += zRotationSpeed;
        age++;
        
        vx += (RANDOM.nextFloat() - 0.5f) * 0.05f;
        
        if (vx > 1.5) vx = 1.5f;
        if (vx < -1.5) vx = -1.5f;
    }
    
    public boolean isDead() {
        return age >= lifetime || y > Minecraft.getInstance().getWindow().getGuiScaledHeight() + 10;
    }
    
    public void render(PoseStack poseStack, Tesselator tesselator) {
        float fadeAlpha = alpha;
        if (age < 15) {
            fadeAlpha = alpha * (age / 15.0f);
        } else if (age > lifetime - 15) {
            fadeAlpha = alpha * ((lifetime - age) / 15.0f);
        }
        
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        
        Matrix4f matrix = poseStack.last().pose();
        BufferBuilder buffer = tesselator.getBuilder();
        
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        
        float cosRot = (float) Math.cos(Math.toRadians(rotation));
        float sinRot = (float) Math.sin(Math.toRadians(rotation));
        float cosZ = (float) Math.cos(Math.toRadians(zRotation));
        float sinZ = (float) Math.sin(Math.toRadians(zRotation));
        
        int colorA = (int)(fadeAlpha * 255);
        if (colorA < 0) colorA = 0;
        if (colorA > 255) colorA = 255;
        
        buffer.vertex(matrix, x, y, 0)
              .color(255, 255, 255, colorA).endVertex();
        
        for (int i = 0; i <= CIRCLE_SEGMENTS; i++) {
            float angle = (float) (i * 2 * Math.PI / CIRCLE_SEGMENTS);
            float baseX = (float) Math.cos(angle) * size / 2;
            float baseY = (float) Math.sin(angle) * size / 2;
            
            float rotatedX = baseX * cosZ - baseY * sinZ;
            float rotatedY = baseX * sinZ + baseY * cosZ;

            
            float finalX = rotatedX * cosRot - rotatedY * sinRot;
            float finalY = rotatedX * sinRot + rotatedY * cosRot;
            
            buffer.vertex(matrix, x + finalX, y + finalY, zRotation)
                  .color(255, 255, 255, colorA).endVertex();
        }
        
        tesselator.end();
        
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
