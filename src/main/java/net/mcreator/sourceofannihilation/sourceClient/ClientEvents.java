package net.mcreator.sourceofannihilation.sourceClient;

import net.mcreator.sourceofannihilation.ProtectList;
import net.mcreator.sourceofannihilation.item.SourceOfAnnihilationItem;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.mcreator.sourceofannihilation.sourceClient.effect.SnowfallEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ClientEvents {
    public static float partialTick;
    public static float renderTick;

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onOpenScreen(ScreenEvent.Opening event) {
        Screen screen = event.getNewScreen();
        ProtectList myObj = ProtectList.getInstance();
        if (!SourceOfAnnihilationModVariables.MapVariables.get(Minecraft.getInstance().level).invul) {
            if (screen instanceof DeathScreen) {
                if (event != null && event.isCancelable()) {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.player != null) {
                        Player player = mc.player;
                        if (myObj.containsEntityType(player)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        } else {
            if (event != null && event.isCancelable()) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    Player player = mc.player;
                    if (myObj.containsEntityType(player)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onRenderScreen(ScreenEvent.Render.Pre event) {
        ProtectList myObj = ProtectList.getInstance();
        if (SourceOfAnnihilationModVariables.MapVariables.get(Minecraft.getInstance().level).invul) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (myObj.containsEntityType(player))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Level world = null;
        double x = 0;
        double y = 0;
        double z = 0;
        Player entity = Minecraft.getInstance().player;
        if (entity != null) {
            world = entity.level();
            x = entity.getX();
            y = entity.getY();
            z = entity.getZ();
        }
        if (SourceOfAnnihilationModVariables.MapVariables.get(world).invul) {
            ProtectList myObj = ProtectList.getInstance();
            if (myObj.containsEntityType(entity)) {
                Minecraft.getInstance().setScreen(null);
                entity.closeContainer();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onRenderTooltip(RenderTooltipEvent.Color event) {
        if (!event.getItemStack().isEmpty()) {

            ItemStack stack = event.getItemStack();

            if (stack.getItem() instanceof SourceOfAnnihilationItem) {
//                event.setBorderStart(0xFF4B0082);
//                event.setBorderEnd(0xFF8A2BE2);
//                event.setBackgroundStart(0xCC000033);
//                event.setBackgroundEnd(0xCC330033);

                event.setBorderStart(0);
                event.setBorderEnd(0);
                event.setBackgroundStart(0);
                event.setBackgroundEnd(0);
                SnowfallEffect.checkAndActivate(stack);
                SnowfallEffect.update();
                if (!SnowfallEffect.isEmpty()) {
                    SnowfallEffect.render(event.getGraphics().pose());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderGuiOverlay(RenderGuiEvent.Post event) {

    }

    @SubscribeEvent
    public static void onRenderLevel(RenderGuiEvent event){
        partialTick = event.getPartialTick();
    }

}
