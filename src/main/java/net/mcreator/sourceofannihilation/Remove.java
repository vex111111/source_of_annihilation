/**
 * The code of this mod element is always locked.
 * <p>
 * You can register new events in this class too.
 * <p>
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.sourceofannihilation as this package is managed by MCreator.
 * <p>
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 * <p>
 * This class will be added in the mod root package.
 */
package net.mcreator.sourceofannihilation;

import com.SourceofAnnihilation.mixin.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.mcreator.sourceofannihilation.network.ListNetwork;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

public abstract class Remove {
    public static void Remove_Entity(Entity entity) {
        EntityProxy proxy = (EntityProxy) entity;
        EntityInLevelCallback callback = proxy.getEntityInLevelCallback();
        long pcurrentSectionKey = -1;
        EntitySection<EntityAccess> pcurrentSection = null;
        long tcurrentSectionKey = -1;
        EntitySection<EntityAccess> tcurrentSection = null;

        if (callback instanceof PCallbackProxy && entity.level() instanceof ServerLevel) {
            PCallbackProxy pCallback = (PCallbackProxy) callback;
            pcurrentSectionKey = pCallback.getCurrentSectionKey();
            pcurrentSection = pCallback.getCurrentSection();
        } else if (callback instanceof TCallbackProxy) {
            TCallbackProxy tCallback = (TCallbackProxy) callback;
            tcurrentSectionKey = tCallback.getCurrentSectionKey();
            tcurrentSection = tCallback.getCurrentSection();
        }

        if (proxy.getRemovalReason() == null)
            proxy.setRemovalReason(Entity.RemovalReason.KILLED);
        if (proxy.getRemovalReason().shouldDestroy())
            entity.stopRiding();
        entity.getPassengers().forEach(Entity::stopRiding);
        if (entity.level() instanceof ServerLevel serverLevel) {

            ServerLevelProxy proxy1 = (ServerLevelProxy) serverLevel;
            ChunkMap chunkMap1 = serverLevel.getChunkSource().chunkMap;
            ChunkMapProxy cMapProxy = (ChunkMapProxy) chunkMap1;
            Int2ObjectMap<?> cMapEntity = cMapProxy.getEntityMap();
            if (entity instanceof ServerPlayer serverplayer) {
                cMapProxy.invokeUpdatePlayerStatus(serverplayer, false);

                for (Object trackedEntity : cMapEntity.values()) {
                    ((TrackedEntityProxy) trackedEntity).invokeRemovePlayer(serverplayer);
                }
            }

            Object trackedEntity = cMapEntity.remove(entity.getId());
            if (trackedEntity != null) {
                ((TrackedEntityProxy) trackedEntity).invokeBroadcastRemoved();
            }

            EntityTickList sb = new SbList();
            //sb.pasteToList(proxy1.getEntityTickList());
            proxy1.getEntityTickList().forEach(sb::add);
            sb.remove(entity);
            proxy1.setEntityTickList((EntityTickList) sb);
            //proxy1.getEntityTickList().remove(entity);
            PersistentEntitySectionManagerRemove(entity, proxy1, serverLevel, pcurrentSection, pcurrentSectionKey);

            if (entity instanceof ServerPlayer serverplayer) {
                proxy1.getPlayers().remove(serverplayer);
                serverLevel.updateSleepingPlayerList();
            }
            if (entity instanceof Mob mob) {
                if (proxy1.isUpdatingNavigations())
                    Util.logAndPauseIfInIde("onTrackingStart called during navigation iteration", new IllegalStateException("onTrackingStart called during navigation iteration"));
                proxy1.getNavigatingMobs().remove(mob);
            }
            if (entity.isMultipartEntity())
                for (PartEntity<?> part : entity.getParts())
                    if (part != null)
                        proxy1.getDragonParts().remove(part.getId());
            entity.updateDynamicGameEventListener(DynamicGameEventListener::remove);
            entity.onRemovedFromWorld();
            ListNetwork.killList.remove(entity.getId());
        }
        if(entity.level().isClientSide()){
            clientRemoved(entity,tcurrentSection,tcurrentSectionKey);
        }
        entity.setLevelCallback(EntityInLevelCallback.NULL);
        entity.invalidateCaps();
    }

    @OnlyIn(Dist.CLIENT)
    private static void clientRemoved(Entity entity,EntitySection<EntityAccess> tcurrentSection,long tcurrentSectionKey){
        if (Minecraft.getInstance().level != null) {
            ClientLevel level = Minecraft.getInstance().level;
            ClientLevelProxy proxy1 = (ClientLevelProxy) level;

            if (entity.isAlwaysTicking()) {
                entity.unRide();
                proxy1.getPlayers().remove(entity);
            }
            entity.onRemovedFromWorld();

            EntityTickList sb1 = new SbList();
            proxy1.getTickingEntities().forEach(sb1::add);
            sb1.remove(entity);
            proxy1.setEntityTickList(sb1);
            //proxy1.getTickingEntities().remove(entity);

            if (entity.isMultipartEntity())
                for (net.minecraftforge.entity.PartEntity<?> part : entity.getParts())
                    proxy1.getPartEntities().remove(part.getId());
            TransientEntitySectionManagerRemove(entity, proxy1, tcurrentSection, tcurrentSectionKey);
        }
    }

    public static void PersistentEntitySectionManagerRemove(Entity entity, ServerLevelProxy proxy1, ServerLevel serverLevel, EntitySection<EntityAccess> pcurrentSection, long pcurrentSectionKey) {
        PersistentEntitySectionManagerProxy proxy2 = ((PersistentEntitySectionManagerProxy) proxy1.getEntityManager());
        EntityLookupProxy entityLookupproxy = (EntityLookupProxy) proxy2.getVisibleEntityStorage();

        ExtendsEntityLookup<EntityAccess> entitylookup = new ExtendsEntityLookup<EntityAccess>();
        entitylookup.setId(entityLookupproxy.getById());
        entitylookup.setUuid(entityLookupproxy.getByUuid());
        entitylookup.getById().remove(entity.getId());
        entitylookup.getByUuid().remove(entity.getUUID());
        entityLookupproxy.setById(entitylookup.getById());
        entityLookupproxy.setByUuid(entitylookup.getByUuid());
         /*entityLookupproxy.getById().remove(entity.getId());
         entityLookupproxy.getByUuid().remove(entity.getUUID());*/
        serverLevel.getScoreboard().entityRemoved(entity);
        proxy2.getKnownUuids().remove(entity.getUUID());

        if (pcurrentSection != null) {
            EntitySectionStorageProxy sectionstorage = (EntitySectionStorageProxy) proxy2.getSectionStorage();
            Long2ObjectMap<ExtendsEntitySection<EntityAccess>> thesectionstorage = sectionstorage.getSections();
            ExtendsClassInstanceMultiMap fakeclassins = new ExtendsClassInstanceMultiMap<>(EntityAccess.class);
            pcurrentSection.getEntities().forEach(a -> fakeclassins.add(a));
            fakeclassins.remove(entity);
            ((EntitySectionProxy) pcurrentSection).setStorage(fakeclassins);
            if (fakeclassins.isEmpty()) {
                thesectionstorage.remove(pcurrentSectionKey);
                sectionstorage.setSections(thesectionstorage);
            }
//            pcurrentSection.remove(entity);
//            if (pcurrentSection.isEmpty()) {
//                proxy2.getSectionStorage().remove(pcurrentSectionKey);
//            }
        }
    }

    public static void TransientEntitySectionManagerRemove(Entity entity, ClientLevelProxy proxy1, EntitySection<EntityAccess> tcurrentSection, long tcurrentSectionKey) {
        //((TransientEntitySectionManagerProxy) proxy1.getEntityStorage()).getEntityStorage().remove(entity);
        if (entity.level() instanceof ClientLevel) {
            TransientEntitySectionManagerProxy proxy2 = (TransientEntitySectionManagerProxy) proxy1.getEntityStorage();
            EntityLookupProxy entityLookupproxy = (EntityLookupProxy) proxy2.getEntityStorage();
            ExtendsEntityLookup<EntityAccess> entitylookup = new ExtendsEntityLookup<EntityAccess>();

            entitylookup.setId(entityLookupproxy.getById());
            entitylookup.setUuid(entityLookupproxy.getByUuid());
            entitylookup.getById().remove(entity.getId());
            entitylookup.getByUuid().remove(entity.getUUID());
            entityLookupproxy.setById(entitylookup.getById());
            entityLookupproxy.setByUuid(entitylookup.getByUuid());

            if (tcurrentSection != null) {
                EntitySectionStorageProxy sectionstorage = (EntitySectionStorageProxy) proxy2.getSectionStorage();
                Long2ObjectMap<ExtendsEntitySection<EntityAccess>> thesectionstorage = sectionstorage.getSections();
                ExtendsClassInstanceMultiMap fakeclassins = new ExtendsClassInstanceMultiMap<>(EntityAccess.class);
                tcurrentSection.getEntities().forEach(a -> fakeclassins.add(a));
                fakeclassins.remove(entity);
                ((EntitySectionProxy) tcurrentSection).setStorage(fakeclassins);
                if (fakeclassins.isEmpty()) {
                    thesectionstorage.remove(tcurrentSectionKey);
                    sectionstorage.setSections(thesectionstorage);
                }
//            tcurrentSection.remove(entity);
//            if (tcurrentSection.isEmpty()) {
//                ((TransientEntitySectionManagerProxy) proxy1.getEntityStorage()).getSectionStorage().remove(tcurrentSectionKey);
//            }
            }
        }
    }

    //    private static ExtendsEntitySection<EntityAccess> createSection(PersistentEntitySectionManagerProxy proxy2,long p_156902_) {
//        long i = ChunkPos.asLong(SectionPos.x(p_156902_), SectionPos.z(p_156902_));
//        Visibility visibility = (Visibility) ((EntitySectionStorageProxy) (proxy2.getSectionStorage())).getIntialSectionVisibility().get(i);
//        ((EntitySectionStorageProxy) (proxy2.getSectionStorage())).getSectionIds().add(p_156902_);
//        return new ExtendsEntitySection<EntityAccess>(EntityAccess.class, visibility);
//    }
    @Deprecated
    public static void kill(Entity entity, Player source) {
        if(entity instanceof Player player){
            killPlayer(player, source);
            return;
        }
        if((entity instanceof Entity && !(entity instanceof LivingEntity) || entity instanceof EnderDragon)) {
            entity.kill();
            return;
        }
        if(entity instanceof LivingEntity livingEntity){
            ListNetwork.killList.add(livingEntity.getId());
            ForceHurt.setHealth(livingEntity, 0);
            livingEntity.hurtTime = 10;
            livingEntity.getPersistentData().putBoolean("isdead", true);
            livingEntity.getPersistentData().putBoolean("jdead", true);
            livingEntity.setLastHurtByPlayer(source);
        }
    }

    public static void killPlayer(Player player, Player source) {
        ForceHurt.setHealth(player, -2147483647);
        player.hurtTime = 10;
        player.setLastHurtByPlayer(source);
        //ForceHurt.die(player, player.damageSources().playerAttack(source));
        if (player instanceof ServerPlayer serverplayer) {
            Death.openForOtherPlayer(serverplayer, false);
        }
    }
}