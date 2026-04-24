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

import net.mcreator.sourceofannihilation.network.ListNetwork;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class NoSpawnList {

    private static NoSpawnList instance;

    private final Set<String> myList = new HashSet<>();

    public static synchronized NoSpawnList getInstance() {
        if (instance == null) {
            instance = new NoSpawnList();
        }
        return instance;
    }

    public void addEntityToList(Entity item) {
        String sb = (ForgeRegistries.ENTITY_TYPES.getKey(item.getType()).toString());
        myList.add(sb);
            ListNetwork.syncToAllClients(item.level(), myList, ProtectList.getInstance().getMyList());
    }

    public void clearEntityToList(Entity item) {
        String sb = (ForgeRegistries.ENTITY_TYPES.getKey(item.getType()).toString());
        myList.remove(sb);
            ListNetwork.syncToAllClients(item.level(), myList, ProtectList.getInstance().getMyList());

    }

    public void addToList(String item, Level level) {
        myList.add(item);
            ListNetwork.syncToAllClients(level, myList, ProtectList.getInstance().getMyList());

    }

    public void clearToList(Level level) {
        myList.clear();
        ListNetwork.syncToAllClients(level, myList, ProtectList.getInstance().getMyList());

    }

    public List<String> getList() {
        return new ArrayList<>(myList);
    }

    public Set<String> getMyList() {
        return myList;
    }

    public void put(Collection<String> c) {
        myList.clear();
        myList.addAll(c);
    }

    public boolean contains(String item) {
        return myList.contains(item);
    }

    public boolean containsEntityType(Entity entity) {
        String entityTypeKey = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        return myList.contains(entityTypeKey);
    }

    public boolean containsEntityType(EntityType entity) {
        String entityTypeKey = entity.toString();
        return myList.contains(entityTypeKey);
    }

    public boolean containsEntityType(String entity) {
        return myList.contains(entity);
    }

    public void printAllElements(Player player) {
        int number = 0;
        for (String item : myList) {
            System.out.println(item);
            player.displayClientMessage(Component.literal(item), false);
            number++;
        }
        player.displayClientMessage(Component.literal("禁生成列表有" + number + "个元素"), false);
    }
}