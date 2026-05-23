package net.snowteb.warriorcats_events.network.packet.c2s.skilltree;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.attachments.CapabilityManager;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.skilltree.SyncSkillDataPacket;

public class ReqSkillDataPacket implements CustomPacketPayload {

    public ReqSkillDataPacket() {}

    public ReqSkillDataPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();

            CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_SKILL, data -> {
                ModPackets.sendToPlayer(new SyncSkillDataPacket(data.getSpeedLevel(),
                        data.getHPLevel(), data.getDMGLevel(), data.getJumpLevel(),
                        data.getArmorLevel(), data.isClimbUnlocked()), player);
            });

        });
        return true;
    }

    public static final Type<ReqSkillDataPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "req_skill_data"));

    public static final StreamCodec<FriendlyByteBuf, ReqSkillDataPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new ReqSkillDataPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
