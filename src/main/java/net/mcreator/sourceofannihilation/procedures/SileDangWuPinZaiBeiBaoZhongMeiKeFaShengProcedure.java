package net.mcreator.sourceofannihilation.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;
import net.minecraft.client.Minecraft;

import net.mcreator.sourceofannihilation.SourceOfAnnihilationMod;
import net.mcreator.sourceofannihilation.ExtendsDeathScreen;

import com.mojang.blaze3d.vertex.BufferUploader;

public class SileDangWuPinZaiBeiBaoZhongMeiKeFaShengProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Minecraft.getInstance().execute(() -> {
			BufferUploader.reset();
			ExtendsDeathScreen.setScreen(new ExtendsDeathScreen(null, false), Minecraft.getInstance());
		});
		if (entity instanceof LivingEntity _entity) {
			_entity.setHealth(0);
			_entity.deathTime = 19;
			_entity.hurtTime = 10;
			entity.kill();
		}
		if (!entity.level().isClientSide())
			entity.discard();
		entity.getPersistentData().putBoolean("isdead", true);
		entity.getPersistentData().putBoolean("jdead", true);
		entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
		SourceOfAnnihilationMod.queueServerWork(50, () -> {
			{
				Entity _ent = entity;
				if (!_ent.level().isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "kick @s");
				}
			}
		});
	}
}
