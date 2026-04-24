package net.mcreator.sourceofannihilation.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.mcreator.sourceofannihilation.SourceOfAnnihilationMod;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class ResetchunksRangeCommandProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		if (DoubleArgumentType.getDouble(arguments, "range") <= 6) {
			SourceOfAnnihilationModVariables.MapVariables.get(world).resetrange = DoubleArgumentType.getDouble(arguments, "range");
			SourceOfAnnihilationModVariables.MapVariables.get(world).syncData(world);
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("resetrange" + DoubleArgumentType.getDouble(arguments, "range"))), false);
		} else {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("the maximum is 6"), false);
			SourceOfAnnihilationMod.LOGGER.info("the maximum is 6");
		}
	}
}
