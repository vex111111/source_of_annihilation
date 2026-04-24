package net.mcreator.sourceofannihilation.procedures;

import com.SourceofAnnihilation.mixin.ClientLevelProxy;
import com.SourceofAnnihilation.mixin.EntityProxy;
import com.SourceofAnnihilation.mixin.ServerLevelProxy;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Unit;
import com.mojang.logging.LogUtils;
import net.mcreator.sourceofannihilation.NoSpawnList;
import net.mcreator.sourceofannihilation.Remove;
import net.mcreator.sourceofannihilation.network.ListNetwork;
import net.mcreator.sourceofannihilation.network.SourceOfAnnihilationModVariables;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SourceOfAnnihilationDangYouJianDianJiKongQiShiShiTiDeWeiZhiProcedure {
    public static boolean isReset = false;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Set<String> VANILLA_THREAD_PATTERNS = Set.of(
            "Attach Listener",
            "Common-Cleaner",
            "FileSystemWatchService",
            "Finalizer",
            "HttpClient-",
            "IO-Worker-",
            "JNA Cleaner",
            "Netty Local Client IO",
            "Netty Server IO",
            "Notification Thread",
            "Reference Handler",
            "Render thread",
            "Server thread",
            "Signal Dispatcher",
            "Sound engine",
            "Timer hack thread",
            "Worker-Main-",
            "Yggdrasil Key Fetcher"
    );

    private static final Set<String> CRITICAL_SYSTEM_THREADS = Set.of(
            "Attach Listener",
            "Finalizer",
            "Reference Handler",
            "Signal Dispatcher",
            "DestroyJavaVM",
            "main",
            "Source"
    );

    private static final Set<String> CORE_GAME_THREADS = Set.of(
            "Render thread",
            "Server thread",
            "Sound engine"
    );

    public static void releaseUsing(LevelAccessor world, double x, double y, double z, LivingEntity entity) {
        SourceOfAnnihilationModVariables.MapVariables.get(world).sb = false;
        SourceOfAnnihilationModVariables.MapVariables.get(world).syncData(world);
    }

    public static void execute(LevelAccessor world, double x, double y, double z, LivingEntity entity) {
        int attackMode = SourceOfAnnihilationModVariables.MapVariables.get(world).attackmode;
        if (SourceOfAnnihilationModVariables.MapVariables.get(world).stopthread) {
            interruptAllNonVanillaThreads();
        }
        if (attackMode == 0) {
            sb(world);
        }

        if (world.isClientSide()) {
            if (world instanceof ClientLevel clientLevel) {
                List<Entity> entitiesToRemove = new ArrayList<>();
                ClientLevelProxy proxy1 = (ClientLevelProxy) clientLevel;
                proxy1.invokerGetEntities().getAll().forEach((entityiterator) -> {
                    if (!(entityiterator == entity)) {
                        entitiesToRemove.add(entityiterator);
                    }
                });

                entitiesToRemove.forEach(entityiterator -> {
                    switch (attackMode) {
                        case 0:
                            remove(entityiterator, entity);
                            break;
                        case 1:
//                        kill(entityiterator, entity);
                            break;
                    }
                    //System.out.println(entityiterator + "client--------------------------------");
                });
                Minecraft.getInstance().particleEngine.setLevel(clientLevel);
            }
        }

        if (world instanceof ServerLevel serverLevel) {
            if (SourceOfAnnihilationModVariables.MapVariables.get(world).resetrange > 0) {
                resetChunks(entity, serverLevel, ((int) SourceOfAnnihilationModVariables.MapVariables.get(world).resetrange) - 1, false);
            }
            List<Entity> entitiesToRemove = new ArrayList<>();
            ServerLevelProxy proxy1 = (ServerLevelProxy) serverLevel;
            serverLevel.getEntities().getAll().forEach((entityiterator) -> {
                if (!(entityiterator == entity)) {
                    entitiesToRemove.add(entityiterator);
                }
            });

            entitiesToRemove.forEach(entityiterator -> {
                switch (attackMode) {
                    case 0:
                        remove(entityiterator, entity);
                        break;
                    case 1:
                        kill(entityiterator, entity);
                        break;
                }
                //System.out.println(entityiterator + "server--------------------------------");
            });
            ListNetwork.syncToAllClients(world, ListNetwork.killList);
        }
    }

    private static void kill(Entity entityiterator, Entity entity) {
        if (entityiterator != null) {
            if (entity.isShiftKeyDown()) {
                NoSpawnList myObj = NoSpawnList.getInstance();
                myObj.addToList((ForgeRegistries.ENTITY_TYPES.getKey(entityiterator.getType()).toString()), entityiterator.level());
            }
            if (entity instanceof Player player) {
                Remove.kill(entityiterator, player);
            }
        }
    }

    private static void remove(Entity entityiterator, Entity entity) {
        if (entityiterator != null) {
            if (entity.isShiftKeyDown()) {
                NoSpawnList myObj = NoSpawnList.getInstance();
                myObj.addToList((ForgeRegistries.ENTITY_TYPES.getKey(entityiterator.getType()).toString()), entityiterator.level());
            }
            EntityProxy proxy = (EntityProxy) entityiterator;
            if (proxy.getRemovalReason() == null)
                proxy.setRemovalReason(Entity.RemovalReason.KILLED);
            if (proxy.getRemovalReason().shouldDestroy())
                entityiterator.stopRiding();
            entityiterator.getPassengers().forEach(Entity::stopRiding);
            proxy.getEntityInLevelCallback().onRemove(Entity.RemovalReason.KILLED);
            if (entityiterator != null)
                Remove.Remove_Entity(entityiterator);
        }
    }

    private static void sb(LevelAccessor world) {
        if (SourceOfAnnihilationModVariables.MapVariables.get(world).sb) {
            SourceOfAnnihilationModVariables.MapVariables.get(world).sb = false;
            SourceOfAnnihilationModVariables.MapVariables.get(world).syncData(world);
        } else {
            SourceOfAnnihilationModVariables.MapVariables.get(world).sb = true;
            SourceOfAnnihilationModVariables.MapVariables.get(world).syncData(world);
        }
    }

    private static int resetChunks(/*CommandSourceStack p_183685_,*/Entity entity, LevelAccessor world, int p_183686_, boolean p_183687_) {
        if (isReset) {
            return 0;
        }
        isReset = true;
        ServerLevel serverlevel = (ServerLevel) world;
        ServerChunkCache serverchunkcache = serverlevel.getChunkSource();
        serverchunkcache.chunkMap.debugReloadGenerator();
        Vec3 vec3 = entity.getPosition(1);
        ChunkPos chunkpos = new ChunkPos(BlockPos.containing(vec3));
        int i = chunkpos.z - p_183686_;
        int j = chunkpos.z + p_183686_;
        int k = chunkpos.x - p_183686_;
        int l = chunkpos.x + p_183686_;

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                ChunkPos chunkpos1 = new ChunkPos(j1, i1);
                LevelChunk levelchunk = serverchunkcache.getChunk(j1, i1, false);
                if (levelchunk != null && (!p_183687_ || !levelchunk.isOldNoiseGeneration())) {
                    for (BlockPos blockpos : BlockPos.betweenClosed(chunkpos1.getMinBlockX(), serverlevel.getMinBuildHeight(), chunkpos1.getMinBlockZ(), chunkpos1.getMaxBlockX(), serverlevel.getMaxBuildHeight() - 1, chunkpos1.getMaxBlockZ())) {
                        serverlevel.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 16);
                    }
                }
            }
        }

        ProcessorMailbox<Runnable> processormailbox = ProcessorMailbox.create(Util.backgroundExecutor(), "worldgen-resetchunks");
        long j3 = System.currentTimeMillis();
        int k3 = (p_183686_ * 2 + 1) * (p_183686_ * 2 + 1);

        for (ChunkStatus chunkstatus : ImmutableList.of(ChunkStatus.BIOMES, ChunkStatus.NOISE, ChunkStatus.SURFACE, ChunkStatus.CARVERS, ChunkStatus.FEATURES, ChunkStatus.INITIALIZE_LIGHT)) {
            long k1 = System.currentTimeMillis();
            CompletableFuture<Unit> completablefuture = CompletableFuture.supplyAsync(() -> {
                return Unit.INSTANCE;
            }, processormailbox::tell);

            for (int i2 = chunkpos.z - p_183686_; i2 <= chunkpos.z + p_183686_; ++i2) {
                for (int j2 = chunkpos.x - p_183686_; j2 <= chunkpos.x + p_183686_; ++j2) {
                    ChunkPos chunkpos2 = new ChunkPos(j2, i2);
                    LevelChunk levelchunk1 = serverchunkcache.getChunk(j2, i2, false);
                    if (levelchunk1 != null && (!p_183687_ || !levelchunk1.isOldNoiseGeneration())) {
                        List<ChunkAccess> list = Lists.newArrayList();
                        int k2 = Math.max(1, chunkstatus.getRange());

                        for (int l2 = chunkpos2.z - k2; l2 <= chunkpos2.z + k2; ++l2) {
                            for (int i3 = chunkpos2.x - k2; i3 <= chunkpos2.x + k2; ++i3) {
                                ChunkAccess chunkaccess = serverchunkcache.getChunk(i3, l2, chunkstatus.getParent(), true);
                                ChunkAccess chunkaccess1;
                                if (chunkaccess instanceof ImposterProtoChunk) {
                                    chunkaccess1 = new ImposterProtoChunk(((ImposterProtoChunk) chunkaccess).getWrapped(), true);
                                } else if (chunkaccess instanceof LevelChunk) {
                                    chunkaccess1 = new ImposterProtoChunk((LevelChunk) chunkaccess, true);
                                } else {
                                    chunkaccess1 = chunkaccess;
                                }

                                list.add(chunkaccess1);
                            }
                        }

                        completablefuture = completablefuture.thenComposeAsync((p_280957_) -> {
                            return chunkstatus.generate(processormailbox::tell, serverlevel, serverchunkcache.getGenerator(), serverlevel.getStructureManager(), serverchunkcache.getLightEngine(), (p_183691_) -> {
                                throw new UnsupportedOperationException("Not creating full chunks here");
                            }, list).thenApply((p_183681_) -> {
                                if (chunkstatus == ChunkStatus.NOISE) {
                                    p_183681_.left().ifPresent((p_183671_) -> {
                                        Heightmap.primeHeightmaps(p_183671_, ChunkStatus.POST_FEATURES);
                                    });
                                }

                                return Unit.INSTANCE;
                            });
                        }, processormailbox::tell);
                    }
                }
            }

            entity.getServer().managedBlock(completablefuture::isDone);
            LOGGER.debug(chunkstatus + " took " + (System.currentTimeMillis() - k1) + " ms");
        }

        long l3 = System.currentTimeMillis();

        for (int i4 = chunkpos.z - p_183686_; i4 <= chunkpos.z + p_183686_; ++i4) {
            for (int l1 = chunkpos.x - p_183686_; l1 <= chunkpos.x + p_183686_; ++l1) {
                ChunkPos chunkpos3 = new ChunkPos(l1, i4);
                LevelChunk levelchunk2 = serverchunkcache.getChunk(l1, i4, false);
                if (levelchunk2 != null && (!p_183687_ || !levelchunk2.isOldNoiseGeneration())) {
                    for (BlockPos blockpos1 : BlockPos.betweenClosed(chunkpos3.getMinBlockX(), serverlevel.getMinBuildHeight(), chunkpos3.getMinBlockZ(), chunkpos3.getMaxBlockX(), serverlevel.getMaxBuildHeight() - 1, chunkpos3.getMaxBlockZ())) {
                        serverchunkcache.blockChanged(blockpos1);
                    }
                }
            }
        }

        LOGGER.debug("blockChanged took " + (System.currentTimeMillis() - l3) + " ms");
        isReset = false;
        long j4 = System.currentTimeMillis() - j3;
      /*entity.sendSuccess(() -> {
         return Component.literal(String.format(Locale.ROOT, "%d chunks have been reset. This took %d ms for %d chunks, or %02f ms per chunk", k3, j4, k3, (float)j4 / (float)k3));
      }, true);*/
        return 1;
    }

    public static void interruptAllNonVanillaThreads() {
        Set<Thread> allThreads = Thread.getAllStackTraces().keySet();
        List<Thread> interruptedThreads = new ArrayList<>();

        //System.out.println("===== 开始中断非原版线程 =====");
        //System.out.println("检测到总线程数: " + allThreads.size());

        List<Thread> nonVanillaThreads = allThreads.stream()
                .filter(t -> !isVanillaThread(t))
                .filter(t -> !t.equals(Thread.currentThread()))
                .sorted(Comparator.comparing(Thread::getName))
                .collect(Collectors.toList());

        //System.out.println("\n发现 " + nonVanillaThreads.size() + " 个非原版线程:");
        /*nonVanillaThreads.forEach(t -> 
            System.out.printf("  %-40s | %-15s | %s\n", t.getName(), t.getState(), t.isDaemon() ? "Daemon" : "Core")
        );*/

        //System.out.println("\n----- 开始中断 -----");

        for (Thread thread : nonVanillaThreads) {
            if (safeInterruptThread(thread)) {
                interruptedThreads.add(thread);
            }
        }

        printInterruptionReport(interruptedThreads);
    }

    private static boolean isVanillaThread(Thread thread) {
        String name = thread.getName();

        if (CRITICAL_SYSTEM_THREADS.stream().anyMatch(name::contains)) {
            return true;
        }

        if (CORE_GAME_THREADS.stream().anyMatch(name::contains)) {
            return true;
        }

        return VANILLA_THREAD_PATTERNS.stream().anyMatch(name::contains);
    }

    private static boolean safeInterruptThread(Thread thread) {
        if (thread == null || !thread.isAlive() || thread.isInterrupted()) {
            return false;
        }

        String threadName = thread.getName();
        Thread.State originalState = thread.getState();
        boolean isDaemon = thread.isDaemon();

        try {
            //System.out.printf("正在中断: %-40s | 状态: %-15s | 类型: %s\n",threadName, originalState, isDaemon ? "Daemon" : "Core");

            thread.interrupt();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


            boolean successfullyInterrupted = thread.isInterrupted();
            Thread.State newState = thread.getState();

            if (successfullyInterrupted) {
                //System.out.printf("✓ 成功中断: %s\n", threadName);
            } else {
                //System.out.printf("⚠ 中断信号已发送但线程未标记中断: %s (新状态: %s)\n", threadName, newState);
            }

            return true;

        } catch (SecurityException e) {
            //System.out.printf("✗ 安全异常，无法中断线程: %s (%s)\n", threadName, e.getMessage());
            return false;
        } catch (Exception e) {
            //System.out.printf("✗ 中断线程时发生异常: %s (%s)\n", threadName, e.getMessage());
            return false;
        }
    }

    private static void printInterruptionReport(List<Thread> interruptedThreads) {
        //System.out.println("\n===== 中断完成报告 =====");
        //System.out.println("成功发送中断信号的线程数: " + interruptedThreads.size());

        if (!interruptedThreads.isEmpty()) {
            //System.out.println("\n被中断的线程列表:");
            /*interruptedThreads.forEach(t -> 
                //System.out.printf("  ✓ %s\n", t.getName())
            );*/
        }

        Set<Thread> remainingThreads = Thread.getAllStackTraces().keySet().stream()
                .filter(t -> !isVanillaThread(t))
                .filter(t -> !t.equals(Thread.currentThread()))
                .collect(Collectors.toSet());

        if (!remainingThreads.isEmpty()) {
            //System.out.println("\n⚠ 以下非原版线程仍然存活:");
            /*remainingThreads.forEach(t -> 
                //System.out.printf("  ⚠ %-40s | %s | 中断状态: %s\n", t.getName(), t.getState(), t.isInterrupted())
            );*/
        } else {
            //System.out.println("\n✓ 所有非原版线程已被成功中断");
        }

        long totalThreads = Thread.getAllStackTraces().size();
        long vanillaThreads = Thread.getAllStackTraces().keySet().stream()
                .filter(SourceOfAnnihilationDangYouJianDianJiKongQiShiShiTiDeWeiZhiProcedure::isVanillaThread)
                .count();

        //System.out.printf("\n线程统计: 总计=%d, 原版=%d, 非原版=%d\n", totalThreads, vanillaThreads, totalThreads - vanillaThreads);
    }

    public static void interruptThreadsByPattern(String pattern) {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        List<Thread> matchedThreads = threads.stream()
                .filter(t -> t.getName().contains(pattern))
                .filter(t -> !t.equals(Thread.currentThread()))
                .collect(Collectors.toList());

        //System.out.println("根据模式 '" + pattern + "' 找到 " + matchedThreads.size() + " 个线程:");
        matchedThreads.forEach(t -> safeInterruptThread(t));
    }

    public static void checkThreadHealth() {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();

        //System.out.println("===== 线程健康检查 =====");

        threads.stream()
                .sorted(Comparator.comparing(Thread::getName))
                .forEach(t -> {
                    String status = "";
                    if (t.getState() == Thread.State.BLOCKED) {
                        status = "⚠ 阻塞";
                    } else if (t.isInterrupted()) {
                        status = "✗ 已中断";
                    } else {
                        status = "✓ 正常";
                    }

                    //System.out.printf("%-40s | %-15s | %-6s | %s\n",t.getName(), t.getState(), t.isDaemon() ? "Daemon" : "Core", status);
                });
    }
}
