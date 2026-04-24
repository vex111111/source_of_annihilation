package net.mcreator.sourceofannihilation.procedures;

import net.mcreator.sourceofannihilation.NoSpawnList;
import net.minecraft.world.level.Level;

public class ClearAllNoSpawnCommandProcedure {
	public static void execute(Level level) {
		NoSpawnList.getInstance().clearToList(level);
	}
}
