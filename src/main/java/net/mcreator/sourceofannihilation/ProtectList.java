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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;

public class ProtectList {

    private static ProtectList instance;

    private final Set<String> myList = new HashSet<>();

    public static synchronized ProtectList getInstance() {
        if (instance == null) {
            instance = new ProtectList();
        }
        return instance;
    }

    public void addEntityToList(Entity item) {
        String sb = (item.getName().getString());
        myList.add(sb);
        ListNetwork.syncToAllClients(item.level(), NoSpawnList.getInstance().getMyList(), myList);
    }

    public void clearEntityToList(Entity item) {
        String sb = (item.getName().getString());
        myList.add(sb);
        ListNetwork.syncToAllClients(item.level(), NoSpawnList.getInstance().getMyList(), myList);

    }

    public void clearToList(Level level) {
        myList.clear();
        ListNetwork.syncToAllClients(level, NoSpawnList.getInstance().getMyList(), myList);

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
        if (entity != null) {
            String entityTypeKey = (entity.getDisplayName().getString());
            return myList.contains(entityTypeKey);
        }
        return false;
    }

    public void printAllElements(Player player) {
        int number = 0;
        for (String item : myList) {
            System.out.println(item);
            player.displayClientMessage(Component.literal(item), false);
            number++;
        }
        player.displayClientMessage(Component.literal("保护表有" + number + "个元素"), false);
    }
}