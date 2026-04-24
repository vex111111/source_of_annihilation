package com.SourceofAnnihilation.mixin;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@OnlyIn(Dist.CLIENT)
@Mixin(RenderStateShard.class)
public interface RenderStateShardProxy {

    // 着色器访问器
    @Accessor("RENDERTYPE_END_PORTAL_SHADER")
    static RenderStateShard.ShaderStateShard getRENDERTYPE_END_PORTAL_SHADER() {
        throw new UnsupportedOperationException();
    }

    @Accessor("RENDERTYPE_END_GATEWAY_SHADER")
    static RenderStateShard.ShaderStateShard getRENDERTYPE_END_GATEWAY_SHADER() {
        throw new UnsupportedOperationException();
    }

    // 透明度状态访问器
    @Accessor("NO_TRANSPARENCY")
    static RenderStateShard.TransparencyStateShard getNO_TRANSPARENCY() {
        throw new UnsupportedOperationException();
    }

    @Accessor("TRANSLUCENT_TRANSPARENCY")
    static RenderStateShard.TransparencyStateShard getTRANSLUCENT_TRANSPARENCY() {
        throw new UnsupportedOperationException();
    }

    @Accessor("ADDITIVE_TRANSPARENCY")
    static RenderStateShard.TransparencyStateShard getADDITIVE_TRANSPARENCY() {
        throw new UnsupportedOperationException();
    }

    @Accessor("GLINT_TRANSPARENCY")
    static RenderStateShard.TransparencyStateShard getGLINT_TRANSPARENCY() {
        throw new UnsupportedOperationException();
    }

    // 面剔除访问器
    @Accessor("CULL")
    static RenderStateShard.CullStateShard getCULL() {
        throw new UnsupportedOperationException();
    }

    @Accessor("NO_CULL")
    static RenderStateShard.CullStateShard getNO_CULL() {
        throw new UnsupportedOperationException();
    }

    // 深度测试访问器
    @Accessor("NO_DEPTH_TEST")
    static RenderStateShard.DepthTestStateShard getNO_DEPTH_TEST() {
        throw new UnsupportedOperationException();
    }

    @Accessor("EQUAL_DEPTH_TEST")
    static RenderStateShard.DepthTestStateShard getEQUAL_DEPTH_TEST() {
        throw new UnsupportedOperationException();
    }

    @Accessor("LEQUAL_DEPTH_TEST")
    static RenderStateShard.DepthTestStateShard getLEQUAL_DEPTH_TEST() {
        throw new UnsupportedOperationException();
    }

    // 光照贴图访问器
    @Accessor("LIGHTMAP")
    static RenderStateShard.LightmapStateShard getLIGHTMAP() {
        throw new UnsupportedOperationException();
    }

    @Accessor("NO_LIGHTMAP")
    static RenderStateShard.LightmapStateShard getNO_LIGHTMAP() {
        throw new UnsupportedOperationException();
    }

    // 叠加层访问器
    @Accessor("OVERLAY")
    static RenderStateShard.OverlayStateShard getOVERLAY() {
        throw new UnsupportedOperationException();
    }

    @Accessor("NO_OVERLAY")
    static RenderStateShard.OverlayStateShard getNO_OVERLAY() {
        throw new UnsupportedOperationException();
    }

    // 写入掩码访问器
    @Accessor("COLOR_WRITE")
    static RenderStateShard.WriteMaskStateShard getCOLOR_WRITE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("COLOR_DEPTH_WRITE")
    static RenderStateShard.WriteMaskStateShard getCOLOR_DEPTH_WRITE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("DEPTH_WRITE")
    static RenderStateShard.WriteMaskStateShard getDEPTH_WRITE() {
        throw new UnsupportedOperationException();
    }

    // 层级状态访问器
    @Accessor("NO_LAYERING")
    static RenderStateShard.LayeringStateShard getNO_LAYERING() {
        throw new UnsupportedOperationException();
    }

    @Accessor("POLYGON_OFFSET_LAYERING")
    static RenderStateShard.LayeringStateShard getPOLYGON_OFFSET_LAYERING() {
        throw new UnsupportedOperationException();
    }

    @Accessor("VIEW_OFFSET_Z_LAYERING")
    static RenderStateShard.LayeringStateShard getVIEW_OFFSET_Z_LAYERING() {
        throw new UnsupportedOperationException();
    }

    // 输出目标访问器
    @Accessor("MAIN_TARGET")
    static RenderStateShard.OutputStateShard getMAIN_TARGET() {
        throw new UnsupportedOperationException();
    }

    @Accessor("OUTLINE_TARGET")
    static RenderStateShard.OutputStateShard getOUTLINE_TARGET() {
        throw new UnsupportedOperationException();
    }

    @Accessor("TRANSLUCENT_TARGET")
    static RenderStateShard.OutputStateShard getTRANSLUCENT_TARGET() {
        throw new UnsupportedOperationException();
    }

    @Accessor("ITEM_ENTITY_TARGET")
    static RenderStateShard.OutputStateShard getITEM_ENTITY_TARGET() {
        throw new UnsupportedOperationException();
    }

    // 纹理变换访问器
    @Accessor("DEFAULT_TEXTURING")
    static RenderStateShard.TexturingStateShard getDEFAULT_TEXTURING() {
        throw new UnsupportedOperationException();
    }

    @Accessor("GLINT_TEXTURING")
    static RenderStateShard.TexturingStateShard getGLINT_TEXTURING() {
        throw new UnsupportedOperationException();
    }

    @Accessor("ENTITY_GLINT_TEXTURING")
    static RenderStateShard.TexturingStateShard getENTITY_GLINT_TEXTURING() {
        throw new UnsupportedOperationException();
    }
}