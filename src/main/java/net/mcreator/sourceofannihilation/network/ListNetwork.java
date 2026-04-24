package net.mcreator.sourceofannihilation.network;

import net.mcreator.sourceofannihilation.NoSpawnList;
import net.mcreator.sourceofannihilation.ProtectList;
import net.mcreator.sourceofannihilation.Remove;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ListNetwork {
    public static Set<Integer> killList = new HashSet<>();
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("source_of_annihilation", "list_sync"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerList() {
        int id = 0;
        INSTANCE.registerMessage(id++, ListPacket.class,
                ListPacket::encode,
                ListPacket::decode,
                ListPacket::handle);
        INSTANCE.registerMessage(id++, KillPacket.class,
                KillPacket::encode,
                KillPacket::decode,
                KillPacket::handle);
        INSTANCE.registerMessage(id++, RemovePacket.class,
                RemovePacket::encode,
                RemovePacket::decode,
                RemovePacket::handle);
    }

    @SubscribeEvent
    public static void registerChannels(FMLCommonSetupEvent event) {
        event.enqueueWork(ListNetwork::registerList);
    }

    public static void sendPlayer(ServerPlayer player, Set<String> nospawn, Set<String> protect) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new ListPacket(nospawn, protect));
    }

    public static void syncToAllClients(LevelAccessor world, Set<String> noSpawnList, Set<String> protectList) {
        if (!world.isClientSide()) {
            INSTANCE.send(PacketDistributor.ALL.noArg(), new ListPacket(noSpawnList, protectList));
        }
    }

    public static void syncToAllClients(LevelAccessor world, Set<Integer> kill) {
        if (!world.isClientSide()) {
            Set<Integer> killIds = kill.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
                    INSTANCE.send(PacketDistributor.ALL.noArg(),
                            new KillPacket(killIds));
        }
    }

    public static void syncToAllClients(LevelAccessor world, int entity) {
        if (!world.isClientSide() && world instanceof ServerLevel level) {
            INSTANCE.send(PacketDistributor.DIMENSION.with(level::dimension),
                    new RemovePacket(entity));
        }
    }

    public static class ListPacket {
        private final Set<String> nospawnlist;
        private final Set<String> protectlist;

        public ListPacket(Set<String> provideNospawnlist, Set<String> provideProtectlist) {
            synchronized (provideNospawnlist) {
                nospawnlist = new HashSet<>(provideNospawnlist);
            }
            synchronized (provideProtectlist) {
                protectlist = new HashSet<>(provideProtectlist);
            }
            
        }

        public static void encode(ListPacket packet, FriendlyByteBuf buf) {
            buf.writeVarInt(packet.nospawnlist.size());
            for (String s : packet.nospawnlist) {
                buf.writeUtf(s, 1000);
            }
            buf.writeVarInt(packet.protectlist.size());
            for (String s : packet.protectlist) {
                buf.writeUtf(s, 1000);
            }
        }

        public static ListPacket decode(FriendlyByteBuf buf) {
            int nospawnsize = buf.readVarInt();
            Set<String> inospawnlist = new HashSet<>();
            for (int i = 0; i < nospawnsize; i++) {
                inospawnlist.add(buf.readUtf(1000));
            }
            int protectsize = buf.readVarInt();
            Set<String> iprotectlist = new HashSet<>();
            for (int i = 0; i < protectsize; i++) {
                iprotectlist.add(buf.readUtf(1000));
            }
            return new ListPacket(inospawnlist, iprotectlist);
        }

        public static void handle(ListPacket packet,
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
        private static void handlePacketClient(ListPacket packet) {
            NoSpawnList.getInstance().put(packet.nospawnlist);
            ProtectList.getInstance().put(packet.protectlist);
        }
    }

    public static class KillPacket {
        private final Set<Integer> killList;

        public KillPacket(Set<Integer> provideKillList) {
            this.killList = new HashSet<>(provideKillList);
        }

        public static void encode(KillPacket packet, FriendlyByteBuf buf) {
            buf.writeVarInt(packet.killList.size());
            for (int i : packet.killList) {
                buf.writeInt(i);
            }
        }

        public static KillPacket decode(FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            Set<Integer> list = new HashSet<>();
            for (int i = 0; i < size; i++) {
                list.add(buf.readInt());
            }
            return new KillPacket(list);
        }

        public static void handle(KillPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    handlePacketClient(packet);
                });
            });
            context.setPacketHandled(true);
        }

        @OnlyIn(Dist.CLIENT)
        private static void handlePacketClient(KillPacket packet) {
            ListNetwork.killList.clear();
            ListNetwork.killList.addAll(packet.killList);
        }
    }

    public static class RemovePacket {
        private final int removedEntityId;

        public RemovePacket(int entityId) {
            this.removedEntityId = entityId;
        }

        public static void encode(RemovePacket packet, FriendlyByteBuf buf) {
            buf.writeVarInt(packet.removedEntityId);
        }

        public static RemovePacket decode(FriendlyByteBuf buf) {
            buf.readVarInt();
            return new RemovePacket(buf.readInt());
        }

        public static void handle(RemovePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
            contextSupplier.get().enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    handlePackClient(packet);
                });
            });
        }

        @OnlyIn(Dist.CLIENT)
        public static void handlePackClient(RemovePacket packet) {
            if (Minecraft.getInstance().level != null) {
                Remove.Remove_Entity(Minecraft.getInstance().level.getEntity(packet.removedEntityId));
            }
        }
    }
}
