package net.mcreator.sourceofannihilation.procedures;

import net.minecraftforge.common.MinecraftForge;

import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class EventBusCommandProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments) {
		if (BoolArgumentType.getBool(arguments, "boolean")) {
			MinecraftForge.EVENT_BUS.start();
		} else {
			MinecraftForge.EVENT_BUS.shutdown();
		}
	}
}
