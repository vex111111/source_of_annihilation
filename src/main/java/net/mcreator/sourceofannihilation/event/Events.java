package net.mcreator.sourceofannihilation.event;

import net.mcreator.sourceofannihilation.Az;
import net.mcreator.sourceofannihilation.HackUtil;
import net.mcreator.sourceofannihilation.ProtectList;
import net.mcreator.sourceofannihilation.init.SourceOfAnnihilationModTabs;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber
public class Events {
    public static Set<Player> players = new HashSet<>();
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTp(EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        if (entity != null) {
            if (SourceOfAnnihilationModVariables.MapVariables.get(entity.level()).invul) {
                if (ProtectList.getInstance().containsEntityType(entity))
                    event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onTick(TickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            CreativeModeTab tab = SourceOfAnnihilationModTabs.SOA_TAB.get();
            HackUtil.unsafeSetFieldValue(CreativeModeTab.class,"displayName","f_40764_",tab, Az.makeColour2(Component.translatable("tab.soa")));

            for(Player player : players) {
                if(!player.level().isClientSide()){
                    ServerLevel serverLevel = (ServerLevel) player.level();
                    if(player.isRemoved()) {
                        serverLevel.addNewPlayer((ServerPlayer) player);
                    }
                }
                if (player.level().isClientSide()) {
                    ClientLevel clientLevel = (ClientLevel) player.level();
                    if (!clientLevel.players().contains(player)) {
                        clientLevel.addPlayer(player.getId(), (AbstractClientPlayer) player);
                    }
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedOutEvent event) {
            players.removeIf((player -> player == event.getEntity()));
    }
}
