package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.attachments.CapabilityManager;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.ClanObjects;
import net.snowteb.warriorcats_events.client.ClientClans;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.DisplayClanMenuMessage;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.UpdateClanEntryPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ManageJoinRequestPacket implements CustomPacketPayload {

    private final ClanObjects.JoinRequest request;
    private final String action;

    public ManageJoinRequestPacket(ClanObjects.JoinRequest request, String action) {
        this.request = request;
        this.action = action;
    }

    public static ManageJoinRequestPacket decode(FriendlyByteBuf buf) {
        HolderLookup.Provider provider = ((RegistryFriendlyByteBuf) buf).registryAccess();
        CompoundTag tag = buf.readNbt();
        String action = buf.readUtf();
        if (tag == null) {
            return new ManageJoinRequestPacket(new ClanObjects.JoinRequest(
                    null, null,
                    null, null,
                    ""), action);
        }
        ClanObjects.JoinRequest joinRequest = ClanObjects.JoinRequest.deserializeSingleRequest(tag, provider);

        return new ManageJoinRequestPacket(joinRequest, action);
    }

    public static void encode(ManageJoinRequestPacket packet, FriendlyByteBuf buf) {
        HolderLookup.Provider provider = ((RegistryFriendlyByteBuf) buf).registryAccess();
        buf.writeNbt(packet.request.serializeSingleRequest(provider));
        buf.writeUtf(packet.action);
    }

    public static void handle(ManageJoinRequestPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {


            ServerPlayer player = (ServerPlayer) ctx.player();

            ServerLevel level = player.serverLevel().getServer().overworld();

            ClanData data = ClanData.get(level);
            ClanData.Clan clan = data.getClan(packet.request.getClanUUID());
            if (clan == null) return;
            boolean canManage = data.canManage(clan, player.getUUID());
            if (!canManage) return;

            Component messageToDisplay = Component.empty();

            switch (packet.action) {
                case "accept" -> {
                    packet.request.accept(level, player);
                    messageToDisplay = Component.translatable("clan.join_request.accepted");
                }
                case "deny" -> {
                    packet.request.decline(level, player);
                    messageToDisplay = Component.translatable("clan.join_request.denied");
                }
            }

            if (!messageToDisplay.getString().isEmpty()) {
                ModPackets.sendToPlayer(new DisplayClanMenuMessage(messageToDisplay, false), player);
            }


            data.updateScreen(clan, player);

        });
    }

    public static final Type<ManageJoinRequestPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "manage_join_request"));

    public static final StreamCodec<FriendlyByteBuf, ManageJoinRequestPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

