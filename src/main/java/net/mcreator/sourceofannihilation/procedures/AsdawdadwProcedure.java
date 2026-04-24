package net.mcreator.sourceofannihilation.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;

public class AsdawdadwProcedure {
	public static void execute(LevelAccessor world) {
		if (SourceOfAnnihilationModVariables.MapVariables.get(world).sb) {
			SourceOfAnnihilationModVariables.MapVariables.get(world).sb = false;
			SourceOfAnnihilationModVariables.MapVariables.get(world).syncData(world);
		} else {
			SourceOfAnnihilationModVariables.MapVariables.get(world).sb = true;
			SourceOfAnnihilationModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
