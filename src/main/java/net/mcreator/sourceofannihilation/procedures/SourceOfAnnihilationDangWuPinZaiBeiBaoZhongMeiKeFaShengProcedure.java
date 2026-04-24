package net.mcreator.sourceofannihilation.procedures;

import net.mcreator.sourceofannihilation.ProtectList;
import net.mcreator.sourceofannihilation.event.Events;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

public class SourceOfAnnihilationDangWuPinZaiBeiBaoZhongMeiKeFaShengProcedure {
    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null)
            return;
        {
            double _setval = 200;
            entity.getCapability(SourceOfAnnihilationModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.fy = _setval;
                capability.syncPlayerVariables(entity);
            });
        }
        ProtectList myObj = ProtectList.getInstance();
        if (!myObj.contains(entity.getName().getString())) {
            myObj.addEntityToList(entity);
            if(entity instanceof Player player && !player.isRemoved()) {
                Events.players.add(player);
            }
        }
        entity.clearFire();
        if (SourceOfAnnihilationModVariables.MapVariables.get(world).invul) {
            if (entity instanceof LivingEntity _entity)
                _entity.removeAllEffects();
            if (entity instanceof Player _player)
                _player.closeContainer();
        }
        entity.setTicksFrozen(0);
    }
}
