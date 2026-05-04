package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;

import java.util.UUID;
import java.util.function.Supplier;

public class RenameClanPacket {

    private final String  name;

    public RenameClanPacket(String name) {
        this.name = name;
    }

    public static RenameClanPacket decode(FriendlyByteBuf buf) {

        String name = buf.readUtf();

        return new RenameClanPacket(name);
    }

    public static void encode(RenameClanPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.name);

    }

    public static void handle(RenameClanPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel().getServer().overworld();
            ClanData data = ClanData.get(level);



            ClanData.Clan clan = data.getClan(player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                    .map(WCEPlayerData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID));

            if (clan != null) {
                if (!data.canManage(clan, player.getUUID())) return;


                if (packet.name.isEmpty()) return;
                if (data.getClanByName(packet.name) != null) {
                    player.sendSystemMessage(Component.literal("A clan with this name already exists.").withStyle(ChatFormatting.YELLOW));
                    return;
                }

                String oldName = clan.name;

                boolean success = data.renameClan(clan, packet.name, level);
                if (!success) return;

                player.sendSystemMessage(Component.literal("Clan successfully renamed!").withStyle(ChatFormatting.GRAY));

                String morphName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getMorphName).orElse(player.getGameProfile().getName());

                Component clanCreatedLog = Component.empty()
                        .append(Component.literal(morphName).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal("(").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                        .append(" has renamed ")
                        .append(Component.literal(oldName).withStyle(Style.EMPTY.withColor(clan.color)))
                        .append(" to ")
                        .append(Component.literal(packet.name).withStyle(Style.EMPTY.withColor(clan.color)))
                        .append(".");

                data.registerLog(level, clan.clanUUID, clanCreatedLog);

                data.setDirty();

            } else {
                player.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY));
            }


        });

        ctx.get().setPacketHandled(true);
    }
}

