package com.SourceofAnnihilation.mixin;

import com.google.common.collect.Maps;
import net.mcreator.sourceofannihilation.ForceHurt;
import net.mcreator.sourceofannihilation.ProtectList;
import net.mcreator.sourceofannihilation.Remove;
import net.mcreator.sourceofannihilation.network.ListNetwork;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    private final Map<MobEffect, MobEffectInstance> activeEffects = Maps.newHashMap();

    @Shadow
    protected abstract void onEffectRemoved(MobEffectInstance p_21126_);

    @Shadow
    public abstract AttributeMap getAttributes();

    @Unique
    private int source_of_annihilation$deadTime;

    @Inject(method = "removeAllEffects", at = @At("HEAD"), cancellable = true)
    public void getHealthMixin(CallbackInfoReturnable<Boolean> cir) {

        Entity entity = (Entity) (Object) this;
        if (ProtectList.getInstance().containsEntityType(entity) && SourceOfAnnihilationModVariables.MapVariables.get(entity.level()).noeffect) {
            if (entity.level().isClientSide) {
                Iterator<MobEffectInstance> iterator = this.activeEffects.values().iterator();

                boolean client;
                for (client = false; iterator.hasNext(); client = true) {
                    MobEffectInstance effect = iterator.next();
                    if (entity instanceof LivingEntity li)
                        effect.getEffect().removeAttributeModifiers(li, this.getAttributes(), effect.getAmplifier());
                    iterator.remove();
                }
                cir.setReturnValue(true);
            } else {
                Iterator<MobEffectInstance> iterator = this.activeEffects.values().iterator();

                boolean flag;
                for (flag = false; iterator.hasNext(); flag = true) {
                    MobEffectInstance effect = iterator.next();
                    this.onEffectRemoved(effect);
                    iterator.remove();
                }

                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "baseTick", at = @At("HEAD"), cancellable = true)
    public void basetick(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        LevelAccessor world = entity.level();
        if (ProtectList.getInstance().containsEntityType(entity)) {
            if (entity instanceof LivingEntity _entity) {
                _entity.setHealth(114514);
                _entity.deathTime = -2147483647;
                _entity.hurtTime = 0;
                if (SourceOfAnnihilationModVariables.MapVariables.get(_entity.level()).noeffect) {
                    _entity.removeAllEffects();
                }
                if (SourceOfAnnihilationModVariables.MapVariables.get(_entity.level()).nofire) {
                    _entity.clearFire();
                }
                if (SourceOfAnnihilationModVariables.MapVariables.get(_entity.level()).nocaps) {
                    CompoundTag persistentData = _entity.getPersistentData();
                    if (persistentData.contains(Player.PERSISTED_NBT_TAG)) {
                        CompoundTag playerData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
                        Set<String> allKeys = playerData.getAllKeys();
                        for (String key : allKeys) {
                            playerData.remove(key);
                        }
                        if (playerData.contains("ForgeData")) {
                            playerData.remove("ForgeData");
                        }
                        persistentData.remove(Player.PERSISTED_NBT_TAG);
                        _entity.invalidateCaps();
                    }
                }
                if (SourceOfAnnihilationModVariables.MapVariables.get(world).nohungry && _entity instanceof Player _player) {
                    _player.getFoodData().setSaturation(20);
                    _player.getFoodData().setFoodLevel(20);
                }
            }
        }
    }

    @Inject(method = "isAlive", at = @At("HEAD"), cancellable = true)
    public void isAlive(CallbackInfoReturnable<Boolean> cir) {

        Entity entity = (Entity) (Object) this;
        ProtectList myObj = ProtectList.getInstance();

        if (entity.getPersistentData().getBoolean("isdead")) {
            cir.setReturnValue(false);
        }
        if (myObj.containsEntityType(entity)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isDeadOrDying", at = @At("HEAD"), cancellable = true)
    public void isDeadOrDying(CallbackInfoReturnable<Boolean> cir) {

        Entity entity = (Entity) (Object) this;
        ProtectList myObj = ProtectList.getInstance();

        if (entity.getPersistentData().getBoolean("isdead")) {
            cir.setReturnValue(true);
        }
        if (myObj.containsEntityType(entity)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getHealth", at = @At("RETURN"), cancellable = true)
    public void getHealth(CallbackInfoReturnable<Float> cir) {

        Entity entity = (Entity) (Object) this;
        ProtectList myObj = ProtectList.getInstance();

        if (entity.getPersistentData().getBoolean("jdead")) {
            cir.setReturnValue(0.0f);
        }
        if (myObj.containsEntityType(entity)) {
            cir.setReturnValue(20f);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (ListNetwork.killList.contains(entity.getId()) && !(entity instanceof Player)) {
            entity.deathTime++;
            this.source_of_annihilation$deadTime++;
            if (entity.deathTime >= 20 || source_of_annihilation$deadTime >= 20) {
                ForceHurt.die(entity, entity.damageSources().playerAttack(((LivingEntityProxy) entity).getlastHurtByPlayer()));
                //entity.remove(Entity.RemovalReason.KILLED);
                Remove.Remove_Entity(entity);
            }
            ci.cancel();
        }
        if (ListNetwork.killList.contains(entity.getId()) && entity instanceof Player) {
            entity.deathTime++;
            if (entity.deathTime >= 20) {
                entity.remove(Entity.RemovalReason.KILLED);
            }
            ci.cancel();
        }
    }
}
