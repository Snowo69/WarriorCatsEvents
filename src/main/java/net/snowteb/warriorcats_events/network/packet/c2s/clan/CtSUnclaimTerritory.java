package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.custom.TreeStumpBlock;
import net.snowteb.warriorcats_events.block.entity.TreeStumpBlockEntity;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;

import java.util.UUID;
import java.util.function.Supplier;

public class CtSUnclaimTerritory {
    private final ChunkPos chunkPos;

    public CtSUnclaimTerritory(ChunkPos chunkPos) {
        this.chunkPos = chunkPos;
    }

    public static CtSUnclaimTerritory decode(FriendlyByteBuf buf) {
        int x = buf.readInt();
        int z = buf.readInt();
        ChunkPos chunkPos = new ChunkPos(x, z);
        return new CtSUnclaimTerritory(chunkPos);
    }

    public static void encode(CtSUnclaimTerritory packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.chunkPos.x);
        buf.writeInt(packet.chunkPos.z);
    }

    public static void handle(CtSUnclaimTerritory packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel overworldLevel = player.getServer().overworld();

            UUID uuid = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

            String morphName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getMorphName).orElse(player.getGameProfile().getName());

            if (player.level().dimension() != Level.OVERWORLD) {
                player.sendSystemMessage(Component.literal("Territory not available in other dimensions.").withStyle(ChatFormatting.RED));
                return;
            }

            if (uuid.equals(ClanData.EMPTY_UUID)) {
                player.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.RED));
                return;
            }

            ClanData data = ClanData.get(overworldLevel);
            ClanData.Clan clan = data.getClan(uuid);
            if (clan == null) {
                player.sendSystemMessage(Component.literal("Invalid clan provided.").withStyle(ChatFormatting.RED));
                return;
            }

            ChunkPos currentPosition = packet.chunkPos;

            if (!data.canManage(clan, player.getUUID())) {
                player.sendSystemMessage(Component.literal("You don't have permission to unclaim territory.").withStyle(ChatFormatting.RED));
                return;
            }

            if (!clan.claimedTerritory.containsKey(currentPosition)) {
                player.sendSystemMessage(Component.literal("Territory not claimed.").withStyle(ChatFormatting.RED));
                return;
            }

            Component log = Component.empty()
                    .append(Component.literal(morphName).withStyle(ChatFormatting.GOLD))
                    .append(Component.literal(" [").withStyle(ChatFormatting.DARK_GRAY))
                    .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.DARK_GRAY))
                    .append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY))
                    .append(" has unclaimed territory ")
                    .append(Component.literal(clan.claimedTerritory.get(currentPosition).name).withStyle(ChatFormatting.LIGHT_PURPLE))
                    .append(" at: ")
                    .append(Component.literal(
                            String.format("X=%d, Z=%d", currentPosition.x, currentPosition.z)
                    ).withStyle(ChatFormatting.AQUA));


            boolean result = data.unclaimChunk(clan.clanUUID, currentPosition, overworldLevel);
            if (result) {
                data.registerLog(overworldLevel, clan.clanUUID, log);
                player.sendSystemMessage(Component.literal("Territory successfully unclaimed."));
                data.syncTerritoriesToClients(overworldLevel);
            } else {
                player.sendSystemMessage(Component.literal("Territory couldn't be accessed.").withStyle(ChatFormatting.RED));
            }

        });

        ctx.get().setPacketHandled(true);
    }

}
