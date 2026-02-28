package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.client.ClanInfo;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CManageClanPacket;

import java.util.*;

public class ManageClanCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("manage")
                                        .executes(ctx -> method(ctx.getSource(), ctx.getSource().getPlayerOrException()))
                                )
                        ));
    }

    private static int method(CommandSourceStack source, ServerPlayer player) {

        ServerLevel level = player.serverLevel();
        ClanData data = ClanData.get(level);

        UUID clanUUID = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

        if (clanUUID.equals(ClanData.EMPTY_UUID)) {
            player.sendSystemMessage(
                    Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY)
            );
        }

        ClanData.Clan clan = data.getClan(clanUUID);
        if (clan != null) {
            boolean can = data.canManage(clan, player.getUUID());
            if (can) {

                Map<UUID, ClanInfo.Member> playersInClan = new HashMap<>();
                for (UUID uuid : clan.members.keySet()) {
                    ServerPlayer clanMember = player.serverLevel().getServer()
                            .getPlayerList().getPlayer(uuid);

                    String morphName = data.playerMorphNames.getOrDefault(uuid, "Unknown");
                    int morphData = data.playerMorphData.getOrDefault(uuid, 0);
                    String rank = String.valueOf(clan.members.get(uuid));
                    String perms = String.valueOf(clan.memberPerms.get(uuid));
                    String age = "Undefined";
                    boolean isOnline = false;

                    if (clanMember != null) {
                        age = String.valueOf(clanMember.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                                .map(PlayerClanData::getMorphAge).orElse(PlayerClanData.Age.ADULT));
                        isOnline = true;
                    }

                    ClanInfo.Member member = new ClanInfo.Member(uuid, morphName, rank, perms, age, isOnline, morphData);

                    playersInClan.put(uuid, member);
                }


                ClanInfo clanInfo = new ClanInfo(clan.clanUUID, clan.name, clan.color, clan.leaderName, clan.clanBioSentence,
                        clan.members.size(), playersInClan, clan.clanSymbolIndex);

                ModPackets.sendToPlayer(new S2CManageClanPacket(clanInfo), player);

                return 1;

            } else {
                player.sendSystemMessage(Component.literal("You don't have permission to do that.").withStyle(ChatFormatting.RED));

            }
        }
        return 0;
    }

}
