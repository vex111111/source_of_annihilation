package net.mcreator.sourceofannihilation;

import com.SourceofAnnihilation.mixin.FontManagerProxy;
import com.SourceofAnnihilation.mixin.MinecraftProxy;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class SpecialFont extends Font {
    private static final float DEFAULT_WAVE_AMPLITUDE = 1.5f;
    private static final float DEFAULT_WAVE_SPEED = 0.003f;
    private static final float DEFAULT_SHADOW_OPACITY = 0.3F;
    private static final float DEFAULT_RAINBOW_SPEED = 0.0003F;
    private static final float DEFAULT_RAINBOW_SATURATION = 0.8F;
    private static final float DEFAULT_RAINBOW_BRIGHTNESS = 0.9F;

    private static final float CHAR_PHASE_OFFSET = 0.5F;
    private static final float RAINBOW_HUE_OFFSET = 0.05F;

    private static final float[][] SHADOW_LAYERS = {
            {1.0F, 1.0F},
            {0.5F, 0.5F},
            {0.25F, 0.25F}
    };

    private float waveAmplitude = DEFAULT_WAVE_AMPLITUDE;
    private float waveSpeed = DEFAULT_WAVE_SPEED;
    private boolean enableEffects = true;

    private boolean enableRotation = true;
    private float rotationAmplitude = 8.0f;  // 旋转幅度（度）
    private float rotationSpeed = 0.005f;    // 旋转速度
    private static final float ROTATION_PHASE_OFFSET = 0.6f;

    private boolean enableRainbow = true;
    private float rainbowSpeed = DEFAULT_RAINBOW_SPEED;
    private float rainbowSaturation = DEFAULT_RAINBOW_SATURATION;
    private float rainbowBrightness = DEFAULT_RAINBOW_BRIGHTNESS;

    public SpecialFont(){
        super(create(), false);
        resetEffects();
    }

    public SpecialFont(Function<ResourceLocation, FontSet> p_243253_, boolean p_243245_) {
        super(p_243253_, p_243245_);
    }

    public static Function<ResourceLocation, FontSet> create(){
        FontManagerProxy fontManager = (FontManagerProxy) ((MinecraftProxy) Minecraft.getInstance()).getFontManager();
        return location -> fontManager.getFontSets().getOrDefault(location, fontManager.getMissingFontSet());
    }

    @Override
    public int drawInBatch(FormattedCharSequence formattedCharSequence, float x, float y, int rgb,
                           boolean dropShadow, Matrix4f matrix4f, MultiBufferSource bufferSource,
                           DisplayMode displayMode, int backgroundColor, int packedLight) {

        if (!enableEffects) {
            return super.drawInBatch(formattedCharSequence, x, y, rgb, dropShadow, matrix4f,
                    bufferSource, displayMode, backgroundColor, packedLight);
        }

        String text = getText(formattedCharSequence);
        float currentX = x;
        long currentTime = Util.getMillis();

        float baseHue = enableRainbow ? (float) currentTime * rainbowSpeed % 1.0F : 0.0F;

        for (int index = 0; index < text.length(); index++) {
            String character = String.valueOf(text.charAt(index));
            float waveOffset = calculateWaveOffset(index, currentTime);
            float rotation = calculateRotation(index, currentTime);  // 计算旋转角度

            int characterColor = enableRainbow
                    ? getRainbowColor((baseHue + index * RAINBOW_HUE_OFFSET) % 1.0F, rgb)
                    : rgb;

            // 创建变换矩阵的副本
            Matrix4f transformedMatrix = new Matrix4f(matrix4f);

            // 应用旋转效果（如果需要）
            if (enableRotation && rotation != 0) {
                float charWidth = super.width(character);
                float charHeight = super.lineHeight;

                // 计算字符中心点
                float centerX = currentX + charWidth / 2.0f;
                float centerY = y + waveOffset + charHeight / 2.0f;

                // 平移 -> 旋转 -> 平移回去
                transformedMatrix.translate(centerX, centerY, 0);
                transformedMatrix.rotateZ((float) Math.toRadians(rotation));
                transformedMatrix.translate(-centerX, -centerY, 0);
            }

            // 渲染字符（使用变换后的矩阵）
            super.drawInBatch(character, currentX, y + waveOffset, characterColor, dropShadow,
                    transformedMatrix, bufferSource, displayMode, backgroundColor, packedLight);

            if (dropShadow) {
                renderCharacterShadows(character, currentX, y + waveOffset, characterColor,
                        transformedMatrix, bufferSource, displayMode, backgroundColor, packedLight);
            }

            currentX += super.width(character);
        }

        return (int) currentX;
    }

    @Override
    public int drawInBatch(String text, float x, float y, int rgb, boolean dropShadow,
                           Matrix4f matrix4f, MultiBufferSource bufferSource,
                           DisplayMode displayMode, int backgroundColor, int packedLight) {
        return this.drawInBatch(Component.literal(text).getVisualOrderText(), x, y, rgb,
                dropShadow, matrix4f, bufferSource, displayMode, backgroundColor, packedLight);
    }

    @Override
    public int drawInBatch(Component component, float x, float y, int rgb, boolean dropShadow,
                           Matrix4f matrix4f, MultiBufferSource bufferSource,
                           DisplayMode displayMode, int backgroundColor, int packedLight) {
        return this.drawInBatch(component.getVisualOrderText(), x, y, rgb, dropShadow,
                matrix4f, bufferSource, displayMode, backgroundColor, packedLight);
    }

    private float calculateRotation(int charIndex, long currentTime) {
        if (!enableRotation) return 0;
        float time = currentTime * rotationSpeed;
        float phase = charIndex * ROTATION_PHASE_OFFSET;
        return Mth.sin(time + phase) * rotationAmplitude;
    }

    private float calculateWaveOffset(int charIndex, long currentTime) {
        float time = currentTime * waveSpeed;
        float phase = charIndex * CHAR_PHASE_OFFSET;
        return Mth.sin(time + phase) * waveAmplitude;
    }

    private String getText(FormattedCharSequence formattedCharSequence) {
        StringBuilder stringBuilder = new StringBuilder();
        formattedCharSequence.accept((index, style, codePoint) -> {
            stringBuilder.appendCodePoint(codePoint);
            return true;
        });
        return stringBuilder.toString();
    }

    private void renderCharacterShadows(String character, float x, float y, int baseColor,
                                        Matrix4f matrix4f, MultiBufferSource bufferSource,
                                        DisplayMode displayMode, int backgroundColor, int packedLight) {

        int shadowColor = darkenColor(baseColor);

        for (float[] layer : SHADOW_LAYERS) {
            super.drawInBatch(character, x + layer[0], y + layer[1], shadowColor, false,
                    matrix4f, bufferSource, displayMode, backgroundColor, packedLight);
        }
    }

    private int darkenColor(int color) {
        int red = (int) (((color >> 16) & 0xFF) * 0.6);
        int green = (int) (((color >> 8) & 0xFF) * 0.6);
        int blue = (int) ((color & 0xFF) * 0.6);
        return 0xFF000000 | (red << 16) | (green << 8) | blue;
    }

    private int getRainbowColor(float hue, int originalColor) {
        int rainbowRGB = Mth.hsvToRgb(hue, rainbowSaturation, rainbowBrightness);
        int alpha = (originalColor >> 24) & 0xFF;
        return (alpha << 24) | (rainbowRGB & 0x00FFFFFF);
    }

    public boolean isRainbowEnabled() {
        return enableRainbow;
    }

    public void setRainbowEnabled(boolean enabled) {
        this.enableRainbow = enabled;
    }

    public float getRainbowSpeed() {
        return rainbowSpeed;
    }

    public void setRainbowSpeed(float speed) {
        this.rainbowSpeed = speed;
    }

    public float getRainbowSaturation() {
        return rainbowSaturation;
    }

    public void setRainbowSaturation(float saturation) {
        this.rainbowSaturation = Mth.clamp(saturation, 0.0F, 1.0F);
    }

    public float getRainbowBrightness() {
        return rainbowBrightness;
    }

    public void setRainbowBrightness(float brightness) {
        this.rainbowBrightness = Mth.clamp(brightness, 0.0F, 1.0F);
    }

    public void setRainbowEffect(boolean enabled, float speed, float saturation, float brightness) {
        this.enableRainbow = enabled;
        this.rainbowSpeed = speed;
        this.rainbowSaturation = Mth.clamp(saturation, 0.0F, 1.0F);
        this.rainbowBrightness = Mth.clamp(brightness, 0.0F, 1.0F);
    }

    public float getWaveAmplitude() {
        return waveAmplitude;
    }

    public void setWaveAmplitude(float amplitude) {
        this.waveAmplitude = amplitude;
    }

    public float getWaveSpeed() {
        return waveSpeed;
    }

    public void setWaveSpeed(float speed) {
        this.waveSpeed = speed;
    }

    public boolean isEffectsEnabled() {
        return enableEffects;
    }

    public void setEffectsEnabled(boolean enabled) {
        this.enableEffects = enabled;
    }

    public void resetEffects() {
        this.waveAmplitude = DEFAULT_WAVE_AMPLITUDE;
        this.waveSpeed = DEFAULT_WAVE_SPEED;
        this.enableEffects = true;

        this.enableRainbow = true;
        this.rainbowSpeed = DEFAULT_RAINBOW_SPEED;
        this.rainbowSaturation = DEFAULT_RAINBOW_SATURATION;
        this.rainbowBrightness = DEFAULT_RAINBOW_BRIGHTNESS;
    }
}
