package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.ClanObjects;
import net.snowteb.warriorcats_events.client.ClientClans;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.DisplayClanMenuMessage;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.UpdateClanEntryPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestClanJoinPacket implements CustomPacketPayload {

    private final String message;
    private final UUID clanUUID;

    public RequestClanJoinPacket(String message, UUID clanUUID) {
        this.message = message;
        this.clanUUID = clanUUID;
    }

    public static RequestClanJoinPacket decode(FriendlyByteBuf buf) {
        return new RequestClanJoinPacket(buf.readUtf(200), buf.readUUID());
    }

    public static void encode(RequestClanJoinPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.message);
        buf.writeUUID(packet.clanUUID);
    }

    public static void handle(RequestClanJoinPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = ((ServerPlayer) ctx.player());

            ServerLevel level = player.serverLevel().getServer().overworld();

            ClanData data = ClanData.get(level);
            ClanData.Clan clan = data.getClan(packet.clanUUID);

            Component messageToDisplay;

            if (data.requestJoin(player, packet.clanUUID, level, packet.message)) {
                messageToDisplay = Component.translatable("clan.join_request_sent");
                if (!messageToDisplay.getString().isEmpty()) {
                    ModPackets.sendToPlayer(new DisplayClanMenuMessage(messageToDisplay, false), player);
                }
            }

            data.updateScreen(clan, player);

        });
    }

    public static final Type<RequestClanJoinPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "request_clan_join"));

    public static final StreamCodec<FriendlyByteBuf, RequestClanJoinPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

