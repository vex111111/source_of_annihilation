package net.mcreator.sourceofannihilation.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;

public class GAnXiaAnJianShiProcedure {
	public static void execute(LevelAccessor world) {
		if (SourceOfAnnihilationModVariables.MapVariables.get(world).invul) {
			SourceOfAnnihilationModVariables.MapVariables.get(world).invul = false;
			SourceOfAnnihilationModVariables.MapVariables.get(world).syncData(world);
		} else {
			SourceOfAnnihilationModVariables.MapVariables.get(world).invul = true;
			SourceOfAnnihilationModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
