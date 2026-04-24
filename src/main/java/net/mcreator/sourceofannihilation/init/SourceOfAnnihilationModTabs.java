
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.sourceofannihilation.init;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.Registries;

import net.mcreator.sourceofannihilation.SourceOfAnnihilationMod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SourceOfAnnihilationModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SourceOfAnnihilationMod.MODID);
	public static final RegistryObject<CreativeModeTab> SOA_TAB = REGISTRY.register("soa_tab",()->CreativeModeTab.builder().title(Component.translatable("tab.soa")).icon(()-> new ItemStack(SourceOfAnnihilationModItems.SOURCE_OF_ANNIHILATION.get())).displayItems(((p_270258_, tabData) -> {
		tabData.accept(SourceOfAnnihilationModItems.SOURCE_OF_ANNIHILATION.get());
		tabData.accept(SourceOfAnnihilationModItems.Converter.get());
		tabData.accept(SourceOfAnnihilationModItems.SILE.get());
		tabData.accept(SourceOfAnnihilationModItems.GOD_SPAWN_EGG.get());
	})).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
	}
}
