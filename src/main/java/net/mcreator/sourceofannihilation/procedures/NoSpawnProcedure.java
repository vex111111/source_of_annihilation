package net.mcreator.sourceofannihilation.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

import net.mcreator.sourceofannihilation.NoSpawnList;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;

import javax.annotation.Nullable;
import net.minecraftforge.eventbus.api.EventPriority;

@Mod.EventBusSubscriber
public class NoSpawnProcedure {
	@SubscribeEvent(priority = EventPriority.LOWEST )
	public static void onEntityJoin(EntityJoinLevelEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof Player)) {
			if (!(entity instanceof ServerPlayer)) {
				NoSpawnList myObj = NoSpawnList.getInstance();
				if (myObj.containsEntityType(entity)) {
					if (event != null && event.isCancelable()) {
						event.setCanceled(true);
					}
				}
				if(SourceOfAnnihilationModVariables.MapVariables.get(entity.level()).sb){
					if (event != null && event.isCancelable()) {
					event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
(priority = EventPriority.HIGHEST )
	public static void onEntityJoina(EntityJoinLevelEvent event) {
		executee(event, event.getEntity());
	}

	public static void executee(Entity entity) {
		executee(null, entity);
	}

	private static void executee(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof Player)) {
			if (!(entity instanceof ServerPlayer)) {
				NoSpawnList myObj = NoSpawnList.getInstance();
				if (myObj.containsEntityType(entity)) {
					if (event != null && event.isCancelable()) {
						event.setCanceled(true);
					}
				}
				if(SourceOfAnnihilationModVariables.MapVariables.get(entity.level()).sb){
					if (event != null && event.isCancelable()) {
					event.setCanceled(true);
					}
				}
			}
		}
	}
}
