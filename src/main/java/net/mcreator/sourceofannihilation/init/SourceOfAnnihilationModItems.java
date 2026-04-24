
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.sourceofannihilation.init;

import net.mcreator.sourceofannihilation.SourceOfAnnihilationMod;
import net.mcreator.sourceofannihilation.item.AaaaaaaaaaItem;
import net.mcreator.sourceofannihilation.item.ConverterItem;
import net.mcreator.sourceofannihilation.item.SileItem;
import net.mcreator.sourceofannihilation.item.SourceOfAnnihilationItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SourceOfAnnihilationModItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SourceOfAnnihilationMod.MODID);
    public static final RegistryObject<Item> SOURCE_OF_ANNIHILATION = REGISTRY.register("source_of_annihilation", () -> new SourceOfAnnihilationItem());
    public static final RegistryObject<Item> AAAAAAAAAA = REGISTRY.register("aaaaaaaaaa", () -> new AaaaaaaaaaItem());
    public static final RegistryObject<Item> GOD_SPAWN_EGG = REGISTRY.register("god_spawn_egg", () -> new ForgeSpawnEggItem(SourceOfAnnihilationModEntities.GOD, -1, -1, new Item.Properties()));
    public static final RegistryObject<Item> SILE = REGISTRY.register("sile", () -> new SileItem());
    public static final RegistryObject<Item> Converter = REGISTRY.register("converter", () -> new ConverterItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.COMMON)));
}
