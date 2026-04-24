package net.mcreator.sourceofannihilation;

import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Death {
    private static final String PROTOCOL = "1";
    private static SimpleChannel CHANNEL;

    static {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("source_of_annihilation", "deathscreen"),
                () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals
        );

        CHANNEL.registerMessage(0,
                Packet.class,
                Packet::encode,
                Packet::decode,
                Packet::handle
        );
    }

    public static void openForOtherPlayer(ServerPlayer player, boolean isHardcore) {
        if (player != null) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new Packet(isHardcore));
        }
    }

    private static class Packet {
        private final boolean isHardcore;

        public Packet(boolean isHardcore) {
            this.isHardcore = isHardcore;
        }

        public static void encode(Packet packet, FriendlyByteBuf buffer) {
            buffer.writeBoolean(packet.isHardcore);
        }

        public static Packet decode(FriendlyByteBuf buffer) {
            return new Packet(buffer.readBoolean());
        }

        public static void handle(Packet packet,
                                  java.util.function.Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    handlePacketClient(packet);
                });
            });
            context.setPacketHandled(true);
        }

        @OnlyIn(Dist.CLIENT)
        private static void handlePacketClient(Packet packet) {
            net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
            minecraft.execute(() -> {
                if (!(minecraft.screen instanceof DeathScreen)) {
                com.mojang.blaze3d.vertex.BufferUploader.reset();
                ExtendsDeathScreen.setScreen(
                        new ExtendsDeathScreen(null, packet.isHardcore),
                        minecraft
                );}
            });
        }
    }
}