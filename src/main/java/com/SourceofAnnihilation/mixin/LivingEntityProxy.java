package com.SourceofAnnihilation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mutable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.sounds.SoundEvent;

@Mixin(LivingEntity.class)
public interface LivingEntityProxy {
	@Accessor("noActionTime")
	int getnoActionTime();
	@Accessor("lastHurt")
	float getlastHurt();
	@Accessor("lastHurtByPlayerTime")
	int getlastHurtByPlayerTime();
	@Accessor("lastHurtByPlayer")
	Player getlastHurtByPlayer();
	@Accessor("lastDamageSource")
	DamageSource getlastDamageSource();
	@Accessor("lastDamageStamp")
	long getlastDamageStamp();
	@Accessor("DATA_HEALTH_ID")
	EntityDataAccessor<Float> getDATA_HEALTH_ID();
    @Accessor("dead")
    boolean isDead();
    @Accessor("deathScore")
    int getdeathScore();
	
	@Accessor("noActionTime")
    void setnoActionTime(int value);
    @Accessor("lastHurt")
    void setlastHurt(float value);
    @Accessor("lastHurtByPlayerTime")
    void setlastHurtByPlayerTime(int value);
    @Accessor("lastHurtByPlayer")
    void setlastHurtByPlayer(Player player);
    @Accessor("lastDamageSource")
    void setlastDamageSource(DamageSource damagesource);
    @Accessor("lastDamageStamp")
    void setlastDamageStamp(long value);
    @Accessor("dead")
    void setDead(boolean value);

    @Invoker("hurtHelmet")
    void invokeHurtHelmet(DamageSource source, float damage);
    @Invoker("checkTotemDeathProtection")
    boolean invokeCheckTotemDeathProtection(DamageSource source);
    @Invoker("getDeathSound")
    SoundEvent invokeGetDeathSound();
    @Invoker("getSoundVolume")
    float invokeGetSoundVolume();
    @Invoker("playHurtSound")
    void invokePlayHurtSound(DamageSource source);
    @Invoker("getDamageAfterArmorAbsorb")
    float invokeGetDamageAfterArmorAbsorb(DamageSource source, float amount);
    @Invoker("getDamageAfterMagicAbsorb")
    float invokeGetDamageAfterMagicAbsorb(DamageSource source, float amount);
    @Invoker("dropAllDeathLoot")
    void invokeDropAllDeathLoot(DamageSource source);
    @Invoker("createWitherRose")
    void invokeCreateWitherRose(LivingEntity livingentity);
}
