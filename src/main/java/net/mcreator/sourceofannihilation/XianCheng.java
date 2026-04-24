package net.mcreator.sourceofannihilation;

import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;

public class XianCheng {
    public static void add(LevelAccessor world) {
        Object lock = new Object();
        (new Thread(() -> {
            Thread.currentThread().setName("Source");
            if(world.isClientSide()) {
                while (Minecraft.getInstance().isRunning()) {
                    //use
                    for (Entity entityiterator : new ArrayList<>(world.players())) {
                        if (ProtectList.getInstance().containsEntityType(entityiterator)) {
                            if (entityiterator instanceof LivingEntity _entity) {
                                _entity.setHealth(114514);
                                _entity.deathTime = -2147483647;
                                _entity.hurtTime = 0;
				/*if (SourceOfAnnihilationModVariables.MapVariables.get(world).noeffect) {
				_entity.removeAllEffects();
				}*/
                                if (SourceOfAnnihilationModVariables.MapVariables.get(world).nofire) {
                                    _entity.clearFire();
                                }
                                if (SourceOfAnnihilationModVariables.MapVariables.get(_entity.level()).nocaps) {
                                    _entity.invalidateCaps();
                                }
                                if (SourceOfAnnihilationModVariables.MapVariables.get(world).nohungry && _entity instanceof Player _player) {
                                    _player.getFoodData().setSaturation(20);
                                    _player.getFoodData().setFoodLevel(20);
                                }
                            }
                        }
                    }
                    //useend
                    synchronized (lock) {
                        try {
                            lock.wait(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        })).start();
    }
}
