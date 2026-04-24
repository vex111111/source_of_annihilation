package net.mcreator.sourceofannihilation;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.ServerLevelData;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Backtrack {
    private static final Map<Object, Object> serverSaved = new HashMap<>();
    private static final Map<Object, Object> clientSaved = new HashMap<>();
    private static final Set<Object> noServerRestore = new HashSet<>();
    static {
        noServerRestore.add(ServerChunkCache.class);
        noServerRestore.add(MinecraftServer.class);
        //noServerRestore.add(ServerLevelData.class);
        noServerRestore.add(List.class);
    }

    public static void save(LevelAccessor level) {
        if (level instanceof ServerLevel serverLevel) {
            Field[] fields = serverLevel.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                try {
                    if(noServerRestore.contains(field.get(serverLevel))){
                        continue;
                    }
                    serverSaved.put(field.getName(), field.get(serverLevel));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (level instanceof ClientLevel clientLevel) {
            Field[] fields = clientLevel.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                try {
                    clientSaved.put(field.getName(), field.get(clientLevel));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void serverRestore(ServerLevel serverLevel) {
        Field[] fields = serverLevel.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (serverSaved.containsKey(field.getName())) {
                try {
                    field.set(serverLevel, serverSaved.get(field.getName()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void clientRrestore(ClientLevel clientLevel) {
        Field[] fields = clientLevel.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (clientSaved.containsKey(field.getName())) {
                try {
                    field.set(clientLevel, clientSaved.get(field.getName()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

