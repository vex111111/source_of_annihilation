package net.mcreator.sourceofannihilation.procedures;

import net.mcreator.sourceofannihilation.Remove;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class AaaaaaaaaaDangShiTiBeiGaiWuPinJiZhongProcedure {
	public static void execute(double y, double z, Entity entity) {
		if (entity == null)
			return;
//		{
//			Entity _ent = entity;
//			_ent.teleportTo(Double.NaN, y, z);
//			if (_ent instanceof ServerPlayer _serverPlayer)
//				_serverPlayer.connection.teleport(Double.NaN, y, z, _ent.getYRot(), _ent.getXRot());
//		}
		Remove.kill((LivingEntity) entity,null);
	}
}
