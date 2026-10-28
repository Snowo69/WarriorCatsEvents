package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.ClanObjects;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.DisplayClanMenuMessage;

import java.util.UUID;

public class CtSRenameTerritoryChunk implements CustomPacketPayload {
    private final ChunkPos chunkPos;
    private final String newName;

    public CtSRenameTerritoryChunk(ChunkPos chunkPos, String newName) {
        this.chunkPos = chunkPos;
        this.newName = newName;
    }

    public static CtSRenameTerritoryChunk decode(FriendlyByteBuf buf) {
        int x = buf.readInt();
        int z = buf.readInt();
        ChunkPos chunkPos = new ChunkPos(x, z);
        String newName = buf.readUtf(30);
        return new CtSRenameTerritoryChunk(chunkPos, newName);
    }

    public static void encode(CtSRenameTerritoryChunk packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.chunkPos.x);
        buf.writeInt(packet.chunkPos.z);
        buf.writeUtf(packet.newName);
    }

    public static void handle(CtSRenameTerritoryChunk packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();

            ServerLevel overworldLevel = player.getServer().overworld();

            UUID uuid = player.getData(ModAttachments.PLAYER_WCE_DATA).getCurrentClanUUID();

            if (player.level().dimension() != Level.OVERWORLD) {
                ModPackets.sendToPlayer(new DisplayClanMenuMessage(Component.translatable("territory.not_available_in_dimensions"),true), player);
                return;
            }

            if (uuid.equals(ClanData.EMPTY_UUID)) {
                ModPackets.sendToPlayer(new DisplayClanMenuMessage(Component.translatable("clan.player_not_clan"),true), player);
                return;
            }

            ClanData data = ClanData.get(overworldLevel);
            ClanData.Clan clan = data.getClan(uuid);
            if (clan == null) {
                ModPackets.sendToPlayer(new DisplayClanMenuMessage(Component.literal("Invalid clan provided."),true), player);
                return;
            }

            ChunkPos currentPosition = packet.chunkPos;

            if (!data.canManage(clan, player.getUUID())) {
                ModPackets.sendToPlayer(new DisplayClanMenuMessage(Component.translatable("clan.no_permissions"),true), player);
                return;
            }

            if (!clan.claimedTerritory.containsKey(currentPosition)) {
                ModPackets.sendToPlayer(new DisplayClanMenuMessage(Component.translatable("territory.not_claimed"),true), player);
                return;
            }

            Component log = Component.translatable("territory.territory_renamed_log",
                    ClanData.logFormattedPlayerName(player),
                    Component.literal(clan.claimedTerritory.get(currentPosition).name).withStyle(ChatFormatting.LIGHT_PURPLE),
                    Component.literal(
                            String.format("X=%d, Z=%d", currentPosition.x, currentPosition.z)
                    ).withStyle(ChatFormatting.AQUA),
                    Component.literal(packet.newName).withStyle(ChatFormatting.LIGHT_PURPLE));


            boolean result = data.renameChunk(clan.clanUUID, currentPosition, overworldLevel, packet.newName);
            if (result) {
                data.registerLog(overworldLevel, clan.clanUUID, log, ClanObjects.ClanLogEntry.Type.OTHERS);
                ModPackets.sendToPlayer(new DisplayClanMenuMessage(Component.translatable("territory.success_renamed"),false), player);
                data.syncTerritoriesToClients(overworldLevel);
            } else {
                player.sendSystemMessage(Component.literal("Territory couldn't be accessed.").withStyle(ChatFormatting.RED));
            }

        });
    }

    public static final Type<CtSRenameTerritoryChunk> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "rename_territory_chunk"));

    public static final StreamCodec<FriendlyByteBuf, CtSRenameTerritoryChunk> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
