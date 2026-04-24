package net.mcreator.sourceofannihilation;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.mcreator.sourceofannihilation.SourceOfAnnihilationMod;
import net.mcreator.sourceofannihilation.init.SourceOfAnnihilationModItems;

public class Util {
    public static boolean isBlocking(@NotNull Player entity) {
        return entity.getUseItem().getItem() == SourceOfAnnihilationModItems.SOURCE_OF_ANNIHILATION.get().getDefaultInstance().getItem() && entity.isUsingItem() && entity.getUseItem().getItem().getUseAnimation(entity.getUseItem()) == Util.getUseAnim();
    }
   	public static UseAnim getUseAnim() {
        return UseAnim.valueOf(SourceOfAnnihilationMod.MODID + ":BLOCK");
    }
}
