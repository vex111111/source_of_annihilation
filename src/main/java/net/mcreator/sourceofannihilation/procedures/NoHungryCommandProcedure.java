package net.mcreator.sourceofannihilation.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class NoHungryCommandProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		SourceOfAnnihilationModVariables.MapVariables.get(world).nohungry = BoolArgumentType.getBool(arguments, "boolean");
		SourceOfAnnihilationModVariables.MapVariables.get(world).syncData(world);
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal(("nohungry" + BoolArgumentType.getBool(arguments, "boolean"))), false);
	}
}
