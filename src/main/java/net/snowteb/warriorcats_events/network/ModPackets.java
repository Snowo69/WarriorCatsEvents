package net.snowteb.warriorcats_events.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.network.packet.*;

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


        INSTANCE = net;

        net.messageBuilder(CtSHissPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSHissPacket::new)
                .encoder(CtSHissPacket::toBytes)
                .consumerMainThread(CtSHissPacket::handle)
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

        net.messageBuilder(CtSMoreSpeedPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreSpeedPacket::new)
                .encoder(CtSMoreSpeedPacket::toBytes)
                .consumerMainThread(CtSMoreSpeedPacket::handle)
                .add();
        net.messageBuilder(CtSMoreHPPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreHPPacket::new)
                .encoder(CtSMoreHPPacket::toBytes)
                .consumerMainThread(CtSMoreHPPacket::handle)
                .add();
        net.messageBuilder(CtSMoreDMGPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreDMGPacket::new)
                .encoder(CtSMoreDMGPacket::toBytes)
                .consumerMainThread(CtSMoreDMGPacket::handle)
                .add();
        net.messageBuilder(CtSMoreJumpPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreJumpPacket::new)
                .encoder(CtSMoreJumpPacket::toBytes)
                .consumerMainThread(CtSMoreJumpPacket::handle)
                .add();
        net.messageBuilder(CtSMoreArmorPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreArmorPacket::new)
                .encoder(CtSMoreArmorPacket::toBytes)
                .consumerMainThread(CtSMoreArmorPacket::handle)
                .add();

        net.messageBuilder(SyncSkillDataPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncSkillDataPacket::new)
                .encoder(SyncSkillDataPacket::toBytes)
                .consumerMainThread(SyncSkillDataPacket::handle)
                .add();
        net.messageBuilder(ReqSkillDataPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ReqSkillDataPacket::new)
                .encoder(ReqSkillDataPacket::toBytes)
                .consumerMainThread(ReqSkillDataPacket::handle)
                .add();
        net.messageBuilder(ResetSkillsPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ResetSkillsPacket::new)
                .encoder(ResetSkillsPacket::toBytes)
                .consumerMainThread(ResetSkillsPacket::handle)
                .add();

        net.messageBuilder(CtSToggleStealthPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSToggleStealthPacket::new)
                .encoder(CtSToggleStealthPacket::toBytes)
                .consumerMainThread(CtSToggleStealthPacket::handle)
                .add();

        net.messageBuilder(StCStealthSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StCStealthSyncPacket::new)
                .encoder(StCStealthSyncPacket::toBytes)
                .consumerMainThread(StCStealthSyncPacket::handle)
                .add();

        net.messageBuilder(CtSUnlockStealthPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(CtSUnlockStealthPacket::encode)
                .decoder(CtSUnlockStealthPacket::decode)
                .consumerMainThread(CtSUnlockStealthPacket::handle)
                .add();

    }


    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
