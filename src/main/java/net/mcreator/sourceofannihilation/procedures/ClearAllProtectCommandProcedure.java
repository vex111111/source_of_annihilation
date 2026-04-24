package net.mcreator.sourceofannihilation.procedures;

import net.mcreator.sourceofannihilation.ProtectList;
import net.minecraft.world.level.Level;

public class ClearAllProtectCommandProcedure {
	public static void execute(Level level) {
		ProtectList.getInstance().clearToList(level);
	}
}
