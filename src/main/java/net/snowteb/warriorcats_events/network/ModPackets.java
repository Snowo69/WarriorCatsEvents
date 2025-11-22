package net.snowteb.warriorcats_events.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.network.packet.CtSwaterPacket;
import net.snowteb.warriorcats_events.network.packet.ThirstDataSyncStCPacket;
import net.snowteb.warriorcats_events.network.packet.WaterPacket;

public class ModPackets {
    private static SimpleChannel INSTANCE;
    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(WarriorCatsEvents.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        //System.out.println("Registering packets...");


        INSTANCE = net;

        net.messageBuilder(CtSwaterPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSwaterPacket::new)
                .encoder(CtSwaterPacket::toBytes)
                .consumerMainThread(CtSwaterPacket::handle)
                .add();
        net.messageBuilder(WaterPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(WaterPacket::new)
                .encoder(WaterPacket::toBytes)
                .consumerMainThread(WaterPacket::handle)
                .add();
        net.messageBuilder(ThirstDataSyncStCPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ThirstDataSyncStCPacket::new)
                .encoder(ThirstDataSyncStCPacket::toBytes)
                .consumerMainThread(ThirstDataSyncStCPacket::handle)
                .add();



    }


    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
