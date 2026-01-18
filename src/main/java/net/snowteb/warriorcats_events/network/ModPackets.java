package net.snowteb.warriorcats_events.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.network.packet.*;
import net.snowteb.warriorcats_events.network.packet.c2s.*;
import net.snowteb.warriorcats_events.network.packet.s2c.StCFishingScreenPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.StCStealthSyncPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.SyncSkillDataPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.ThirstDataSyncStCPacket;
import net.snowteb.warriorcats_events.network.packet.zcatmodifiers.*;
import net.snowteb.warriorcats_events.network.packet.zcatmodifiers.SyncCatDataPacket;

/**
 * All about this or any packets, just ask me personally, i aint explaining all that
 */
public class ModPackets {
    public static SimpleChannel INSTANCE;
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
// 0
        net.messageBuilder(CtSHissPacket.class, 0, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSHissPacket::new)
                .encoder(CtSHissPacket::toBytes)
                .consumerMainThread(CtSHissPacket::handle)
                .add();
        net.messageBuilder(WaterPacket.class, 1, NetworkDirection.PLAY_TO_SERVER)
                .decoder(WaterPacket::new)
                .encoder(WaterPacket::toBytes)
                .consumerMainThread(WaterPacket::handle)
                .add();
        net.messageBuilder(ThirstDataSyncStCPacket.class, 2, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ThirstDataSyncStCPacket::new)
                .encoder(ThirstDataSyncStCPacket::toBytes)
                .consumerMainThread(ThirstDataSyncStCPacket::handle)
                .add();

        net.messageBuilder(CtSMoreSpeedPacket.class, 3, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreSpeedPacket::new)
                .encoder(CtSMoreSpeedPacket::toBytes)
                .consumerMainThread(CtSMoreSpeedPacket::handle)
                .add();
        net.messageBuilder(CtSMoreHPPacket.class, 4, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreHPPacket::new)
                .encoder(CtSMoreHPPacket::toBytes)
                .consumerMainThread(CtSMoreHPPacket::handle)
                .add();
        net.messageBuilder(CtSMoreDMGPacket.class, 5, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreDMGPacket::new)
                .encoder(CtSMoreDMGPacket::toBytes)
                .consumerMainThread(CtSMoreDMGPacket::handle)
                .add();
        net.messageBuilder(CtSMoreJumpPacket.class, 6, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreJumpPacket::new)
                .encoder(CtSMoreJumpPacket::toBytes)
                .consumerMainThread(CtSMoreJumpPacket::handle)
                .add();
        net.messageBuilder(CtSMoreArmorPacket.class, 7, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSMoreArmorPacket::new)
                .encoder(CtSMoreArmorPacket::toBytes)
                .consumerMainThread(CtSMoreArmorPacket::handle)
                .add();

        net.messageBuilder(SyncSkillDataPacket.class, 8, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncSkillDataPacket::new)
                .encoder(SyncSkillDataPacket::toBytes)
                .consumerMainThread(SyncSkillDataPacket::handle)
                .add();
        net.messageBuilder(ReqSkillDataPacket.class, 9, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ReqSkillDataPacket::new)
                .encoder(ReqSkillDataPacket::toBytes)
                .consumerMainThread(ReqSkillDataPacket::handle)
                .add();

        //10
        net.messageBuilder(ResetSkillsPacket.class, 10, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ResetSkillsPacket::new)
                .encoder(ResetSkillsPacket::toBytes)
                .consumerMainThread(ResetSkillsPacket::handle)
                .add();

        net.messageBuilder(CtSToggleStealthPacket.class, 11, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSToggleStealthPacket::new)
                .encoder(CtSToggleStealthPacket::toBytes)
                .consumerMainThread(CtSToggleStealthPacket::handle)
                .add();

        net.messageBuilder(StCStealthSyncPacket.class, 12, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StCStealthSyncPacket::new)
                .encoder(StCStealthSyncPacket::toBytes)
                .consumerMainThread(StCStealthSyncPacket::handle)
                .add();

        net.messageBuilder(StCFishingScreenPacket.class, 13, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StCFishingScreenPacket::new)
                .encoder(StCFishingScreenPacket::toBytes)
                .consumerMainThread(StCFishingScreenPacket::handle)
                .add();

        net.messageBuilder(CtSUnlockStealthPacket.class, 14, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CtSUnlockStealthPacket::encode)
                .decoder(CtSUnlockStealthPacket::decode)
                .consumerMainThread(CtSUnlockStealthPacket::handle)
                .add();

        net.messageBuilder(CtSFishSuccesful.class, 15, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CtSFishSuccesful::encode)
                .decoder(CtSFishSuccesful::decode)
                .consumerMainThread(CtSFishSuccesful::handle)
                .add();

        net.messageBuilder(CtSFishFailed.class, 16, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CtSFishFailed::encode)
                .decoder(CtSFishFailed::decode)
                .consumerMainThread(CtSFishFailed::handle)
                .add();

        net.messageBuilder(CtSSwitchShape.class, 17, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSSwitchShape::new)
                .encoder(CtSSwitchShape::toBytes)
                .consumerMainThread(CtSSwitchShape::handle)
                .add();

        net.messageBuilder(CtSSwitchStealthPacket.class, 18, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CtSSwitchStealthPacket::encode)
                .decoder(CtSSwitchStealthPacket::decode)
                .consumerMainThread(CtSSwitchStealthPacket::handle)
                .add();

        net.messageBuilder(SaveClanDataPacket.class, 19, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SaveClanDataPacket::encode)
                .decoder(SaveClanDataPacket::decode)
                .consumerMainThread(SaveClanDataPacket::handle)
                .add();

        //20
        net.messageBuilder(OpenClanSetupScreenPacket.class, 20, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OpenClanSetupScreenPacket::new)
                .encoder(OpenClanSetupScreenPacket::toBytes)
                .consumerMainThread(OpenClanSetupScreenPacket::handle)
                .add();

        net.messageBuilder(C2SSetVariantPacket.class, 21, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SSetVariantPacket::encode)
                .decoder(C2SSetVariantPacket::decode)
                .consumerMainThread(C2SSetVariantPacket::handle)
                .add();

        net.messageBuilder(S2CSyncClanDataPacket.class, 22, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CSyncClanDataPacket::encode)
                .decoder(S2CSyncClanDataPacket::decode)
                .consumerMainThread(S2CSyncClanDataPacket::handle)
                .add();

        net.messageBuilder(CtSTeleportToLocationPacket.class, 23, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CtSTeleportToLocationPacket::encode)
                .decoder(CtSTeleportToLocationPacket::decode)
                .consumerMainThread(CtSTeleportToLocationPacket::handle)
                .add();

//        net.messageBuilder(PerformInteractionTalkPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
//                .encoder(PerformInteractionTalkPacket::encode)
//                .decoder(PerformInteractionTalkPacket::decode)
//                .consumerMainThread(PerformInteractionTalkPacket::handle)
//                .add();
//
//        net.messageBuilder(PerformInteractionGivePreyPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
//                .encoder(PerformInteractionGivePreyPacket::encode)
//                .decoder(PerformInteractionGivePreyPacket::decode)
//                .consumerMainThread(PerformInteractionGivePreyPacket::handle)
//                .add();
//
//        net.messageBuilder(PerformInteractionShowAffectionPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
//                .encoder(PerformInteractionShowAffectionPacket::encode)
//                .decoder(PerformInteractionShowAffectionPacket::decode)
//                .consumerMainThread(PerformInteractionShowAffectionPacket::handle)
//                .add();

        net.messageBuilder(PerformInteractionPacket.class, 24, NetworkDirection.PLAY_TO_SERVER)
                .encoder(PerformInteractionPacket::encode)
                .decoder(PerformInteractionPacket::decode)
                .consumerMainThread(PerformInteractionPacket::handle)
                .add();

        net.messageBuilder(RetrieveLastCatModePacket.class, 25, NetworkDirection.PLAY_TO_SERVER)
                .encoder(RetrieveLastCatModePacket::encode)
                .decoder(RetrieveLastCatModePacket::decode)
                .consumerMainThread(RetrieveLastCatModePacket::handle)
                .add();

        net.messageBuilder(CatSetModePacket.class, 26, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CatSetModePacket::encode)
                .decoder(CatSetModePacket::decode)
                .consumerMainThread(CatSetModePacket::handle)
                .add();

        net.messageBuilder(CatHomeActionsPacket.class, 27, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CatHomeActionsPacket::encode)
                .decoder(CatHomeActionsPacket::decode)
                .consumerMainThread(CatHomeActionsPacket::handle)
                .add();

        net.messageBuilder(SyncCatDataPacket.class, 28, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SyncCatDataPacket::encode)
                .decoder(SyncCatDataPacket::decode)
                .consumerMainThread(SyncCatDataPacket::handle)
                .add();

        net.messageBuilder(ReqMorphStatsPacket.class, 29, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ReqMorphStatsPacket::new)
                .encoder(ReqMorphStatsPacket::toBytes)
                .consumerMainThread(ReqMorphStatsPacket::handle)
                .add();

        //30
        net.messageBuilder(SyncMorphStatsPacket.class, 30, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncMorphStatsPacket::toBytes)
                .decoder(SyncMorphStatsPacket::new)
                .consumerMainThread(SyncMorphStatsPacket::handle)
                .add();

        net.messageBuilder(UpdateClanDataPacket.class, 31, NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateClanDataPacket::decode)
                .encoder(UpdateClanDataPacket::encode)
                .consumerMainThread(UpdateClanDataPacket::handle)
                .add();

        net.messageBuilder(OpenCatDataScreenPacket.class, 32, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OpenCatDataScreenPacket::new)
                .encoder(OpenCatDataScreenPacket::toBytes)
                .consumerMainThread(OpenCatDataScreenPacket::handle)
                .add();

        net.messageBuilder(StCKitCreateScreenPacket.class, 33, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StCKitCreateScreenPacket::new)
                .encoder(StCKitCreateScreenPacket::toBytes)
                .consumerMainThread(StCKitCreateScreenPacket::handle)
                .add();

        net.messageBuilder(CtSCreateAndSpawnKitPacket.class, 34, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSCreateAndSpawnKitPacket::new)
                .encoder(CtSCreateAndSpawnKitPacket::toBytes)
                .consumerMainThread(CtSCreateAndSpawnKitPacket::handle)
                .add();

        net.messageBuilder(KittingInteractionPacket.class, 35, NetworkDirection.PLAY_TO_SERVER)
                .decoder(KittingInteractionPacket::decode)
                .encoder(KittingInteractionPacket::encode)
                .consumerMainThread(KittingInteractionPacket::handle)
                .add();

        net.messageBuilder(CtSPerformLeapPacket.class, 36, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CtSPerformLeapPacket::decode)
                .encoder(CtSPerformLeapPacket::encode)
                .consumerMainThread(CtSPerformLeapPacket::handle)
                .add();

        net.messageBuilder(OpenAncientStickScreenPacket.class, 37, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OpenAncientStickScreenPacket::decode)
                .encoder(OpenAncientStickScreenPacket::encode)
                .consumerMainThread(OpenAncientStickScreenPacket::handle)
                .add();

        net.messageBuilder(CommandCatsPacket.class, 38, NetworkDirection.PLAY_TO_SERVER)
                .decoder(CommandCatsPacket::decode)
                .encoder(CommandCatsPacket::encode)
                .consumerMainThread(CommandCatsPacket::handle)
                .add();



    }


    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
