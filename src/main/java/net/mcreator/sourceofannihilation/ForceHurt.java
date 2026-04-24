/**
 * The code of _entity mod element is always locked.
 *
 * You can register new events in _entity class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.ant as _entity package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt _entity file to these changes or remake it.
 *
 * _entity class will be added in the mod root package.
*/
package net.mcreator.sourceofannihilation;

import com.SourceofAnnihilation.mixin.EntityProxy;
import com.SourceofAnnihilation.mixin.LivingEntityProxy;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.gameevent.GameEvent;

import static net.mcreator.sourceofannihilation.SourceOfAnnihilationMod.LOGGER;

public class ForceHurt {
	  public static boolean hurt(LivingEntity _entity,DamageSource damage, float value) {
	  	LivingEntityProxy proxy = (LivingEntityProxy) _entity;
		if (_entity.level().isClientSide) {
         return false;
		}else {
         if (_entity.isSleeping() && !_entity.level().isClientSide) {
            _entity.stopSleeping();
         }

         proxy.setnoActionTime(0);
         float f = value;
         boolean flag = false;
         float f1 = 0.0F;

         if (value > 0.0F && _entity.isDamageSourceBlocked(damage)) {         /*
            net.minecraftforge.event.entity.living.ShieldBlockEvent ev = net.minecraftforge.common.ForgeHooks.onShieldBlock(_entity, damage, value);
            if(!ev.isCanceled()) {
            if(ev.shieldTakesDamage()) _entity.hurtCurrentlyUsedShield(value);
            f1 = ev.getBlockedDamage();
            value -= ev.getBlockedDamage();
            if (!damage.is(DamageTypeTags.IS_PROJECTILE)) {
               Entity entity = damage.getDirectEntity();
               if (entity instanceof LivingEntity) {
                  LivingEntity livingentity = (LivingEntity)entity;
                  _entity.blockUsingShield(livingentity);
               }
            }

            flag = value <= 0;
         }*/

         if (damage.is(DamageTypeTags.IS_FREEZING) && _entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
            value *= 5.0F;
         }
         }

         _entity.walkAnimation.setSpeed(1.5F);
         boolean flag1 = true;
         if ((float)_entity.invulnerableTime > 10.0F && !damage.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
            if (value <= proxy.getlastHurt()) {
               return false;
            }

            actuallyHurt(_entity,damage, value - proxy.getlastHurt());
            proxy.setlastHurt(value);
            flag1 = false;
         } else {
            proxy.setlastHurt(value);
            _entity.invulnerableTime = 20;
            actuallyHurt(_entity,damage, value);
            _entity.hurtDuration = 10;
            _entity.hurtTime = _entity.hurtDuration;
         }

         if (damage.is(DamageTypeTags.DAMAGES_HELMET) && !_entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            proxy.invokeHurtHelmet(damage, value);
            value *= 0.75F;
         }

         Entity entity1 = damage.getEntity();
         if (entity1 != null) {
            if (entity1 instanceof LivingEntity) {
               LivingEntity livingentity1 = (LivingEntity)entity1;
               if (!damage.is(DamageTypeTags.NO_ANGER)) {
                  _entity.setLastHurtByMob(livingentity1);
               }
            }

            if (entity1 instanceof Player) {
               Player player1 = (Player)entity1;
               proxy.setlastHurtByPlayerTime(100);
               proxy.setlastHurtByPlayer(player1);
            } else if (entity1 instanceof net.minecraft.world.entity.TamableAnimal tamableEntity) {
               if (tamableEntity.isTame()) {
                  proxy.setlastHurtByPlayerTime(100);
                  LivingEntity livingentity2 = tamableEntity.getOwner();
                  if (livingentity2 instanceof Player) {
                     Player player = (Player)livingentity2;
                     proxy.setlastHurtByPlayer(player);
                  } else {
                     proxy.setlastHurtByPlayer(null);
                  }
               }
            }
         }

         if (flag1) {
            if (flag) {
               _entity.level().broadcastEntityEvent(_entity, (byte)29);
            } else {
               _entity.level().broadcastDamageEvent(_entity, damage);
            }

            if (!damage.is(DamageTypeTags.NO_IMPACT) && (!flag || value > 0.0F)) {
               _entity.hurtMarked = true;
            }

            if (entity1 != null && !damage.is(DamageTypeTags.IS_EXPLOSION)) {
               double d0 = entity1.getX() - _entity.getX();

               double d1;
               for(d1 = entity1.getZ() - _entity.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                  d0 = (Math.random() - Math.random()) * 0.01D;
               }

               _entity.knockback((double)0.4F, d0, d1);
               if (!flag) {
                  _entity.indicateDamage(d0, d1);
               }
            }
         }

         if (_entity.isDeadOrDying()) {
            if (!proxy.invokeCheckTotemDeathProtection(damage)) {
               SoundEvent soundevent = proxy.invokeGetDeathSound();
               if (flag1 && soundevent != null) {
                  _entity.playSound(soundevent, proxy.invokeGetSoundVolume(), _entity.getVoicePitch());
               }

               _entity.die(damage);
            }
         } else if (flag1) {
            proxy.invokePlayHurtSound(damage);
         }

         boolean flag2 = !flag || value > 0.0F;
         if (flag2) {
            proxy.setlastDamageSource(damage);
			proxy.setlastDamageStamp(_entity.level().getGameTime());
         }

         if (_entity instanceof ServerPlayer) {
            CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer)_entity, damage, f, value, flag);
            if (f1 > 0.0F && f1 < 3.4028235E37F) {
               ((ServerPlayer)_entity).awardStat(Stats.CUSTOM.get(Stats.DAMAGE_BLOCKED_BY_SHIELD), Math.round(f1 * 10.0F));
            }
         }

         if (entity1 instanceof ServerPlayer) {
            CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)entity1, _entity, damage, f, value, flag);
         }

         return flag2;
      }
   }
   public static void actuallyHurt(LivingEntity _entity,DamageSource damage, float value) {
         if (value <= 0) return;/*
         value = _entity.getDamageAfterArmorAbsorb(damage, value);
         value = _entity.getDamageAfterMagicAbsorb(damage, value);*/
         float f1 = Math.max(value - _entity.getAbsorptionAmount(), 0.0F);
         _entity.setAbsorptionAmount(_entity.getAbsorptionAmount() - (value - f1));
         float f = value - f1;
         if (f > 0.0F && f < 3.4028235E37F) {
            Entity entity = damage.getEntity();
            if (entity instanceof ServerPlayer) {
               ServerPlayer serverplayer = (ServerPlayer)entity;
               serverplayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(f * 10.0F));
            }
         }

         if (f1 != 0.0F) {
            _entity.getCombatTracker().recordDamage(damage, f1);
            setHealth(_entity,_entity.getHealth() - f1);
            _entity.setAbsorptionAmount(_entity.getAbsorptionAmount() - f1);
            _entity.gameEvent(GameEvent.ENTITY_DAMAGE);
      }
   }
   public static void setHealth(LivingEntity _entity,float value) {
   EntityProxy proxy = (EntityProxy) _entity;
   LivingEntityProxy proxy2 = (LivingEntityProxy) _entity;
   proxy.getEntityData().set(proxy2.getDATA_HEALTH_ID(), Mth.clamp(value, 0.0F, _entity.getMaxHealth()));
   }
    public static void die(LivingEntity livingEntity,DamageSource p_21014_) {
        LivingEntityProxy proxy = (LivingEntityProxy) livingEntity;
        if (!livingEntity.isRemoved() && !proxy.isDead()) {
            Entity entity = p_21014_.getEntity();
            LivingEntity livingentity = livingEntity.getKillCredit();
            if (proxy.getdeathScore() >= 0 && livingentity != null) {
                livingentity.awardKillScore(livingEntity, proxy.getdeathScore(), p_21014_);
            }

            if (livingEntity.isSleeping()) {
                livingEntity.stopSleeping();
            }

            if (!livingEntity.level().isClientSide && livingEntity.hasCustomName()) {
                LOGGER.info("Named entity {} died: {}", livingEntity, livingEntity.getCombatTracker().getDeathMessage().getString());
            }

            proxy.setDead(true);
            livingEntity.getCombatTracker().recheckStatus();
            Level level = livingEntity.level();
            if (level instanceof ServerLevel) {
                ServerLevel serverlevel = (ServerLevel)level;
                if (entity == null || entity.killedEntity(serverlevel, livingEntity)) {
                    livingEntity.gameEvent(GameEvent.ENTITY_DIE);
                    proxy.invokeDropAllDeathLoot(p_21014_);
                    proxy.invokeCreateWitherRose(livingentity);
                }

                livingEntity.level().broadcastEntityEvent(livingEntity, (byte)3);
            }

            livingEntity.setPose(Pose.DYING);
        }
    }
}
