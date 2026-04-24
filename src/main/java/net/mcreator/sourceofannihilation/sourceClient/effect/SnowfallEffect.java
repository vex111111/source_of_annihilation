package net.mcreator.sourceofannihilation.sourceClient.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.mcreator.sourceofannihilation.init.SourceOfAnnihilationModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SnowfallEffect {
    private static final List<SnowflakeParticle> particles = new ArrayList<>();
    private static boolean isActive = false;
    private static int activeTimer = 0;
    
    public static void checkAndActivate(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == SourceOfAnnihilationModItems.SOURCE_OF_ANNIHILATION.get()) {
            isActive = true;
            activeTimer = 60;
        }
    }
    
    public static void update() {
        if (isActive) {
            activeTimer--;
            if (activeTimer <= 0) {
                isActive = false;
            }
            
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) {
                isActive = false;
            }
            
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            
            if (particles.size() < 80 && Math.random() < 0.4) {
                particles.add(new SnowflakeParticle(screenWidth, screenHeight));
            }
        }
        
        Iterator<SnowflakeParticle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            SnowflakeParticle particle = iterator.next();
            particle.update();
            if (particle.isDead()) {
                iterator.remove();
            }
        }
    }
    
    public static void render(PoseStack poseStack) {
        if (particles.isEmpty()) return;
        
        Tesselator tesselator = Tesselator.getInstance();
        
        for (SnowflakeParticle particle : particles) {
            particle.render(poseStack, tesselator);
        }
    }
    
    public static boolean isEmpty() {
        return particles.isEmpty();
    }
    
    public static void clear() {
        particles.clear();
        isActive = false;
        activeTimer = 0;
    }
}
