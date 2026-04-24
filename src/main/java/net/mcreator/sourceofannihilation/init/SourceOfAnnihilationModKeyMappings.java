
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.sourceofannihilation.init;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.mcreator.sourceofannihilation.network.VMessage;
import net.mcreator.sourceofannihilation.network.GMessage;
import net.mcreator.sourceofannihilation.SourceOfAnnihilationMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class SourceOfAnnihilationModKeyMappings {
	public static final KeyMapping V = new KeyMapping("key.source_of_annihilation.v", GLFW.GLFW_KEY_V, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				SourceOfAnnihilationMod.PACKET_HANDLER.sendToServer(new VMessage(0, 0));
				VMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping G = new KeyMapping("key.source_of_annihilation.g", GLFW.GLFW_KEY_G, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				SourceOfAnnihilationMod.PACKET_HANDLER.sendToServer(new GMessage(0, 0));
				GMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(V);
		event.register(G);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				V.consumeClick();
			}
			G.consumeClick();
		}
	}
}
