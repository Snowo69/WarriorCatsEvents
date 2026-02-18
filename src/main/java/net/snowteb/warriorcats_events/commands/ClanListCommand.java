package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.client.ClanInfo;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.S2CClanListPacket;

import java.util.*;
import java.util.stream.Collectors;

public class ClanListCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("list")
                                        .executes(ctx -> getList(ctx.getSource(), ctx.getSource().getPlayerOrException()))
                                )
                        ));
    }

    private static int getList(CommandSourceStack source, ServerPlayer player) {

        ServerLevel level = player.serverLevel();
        ClanData data = ClanData.get(level);

        List<ClanInfo> list = new ArrayList<>();



        for (ClanData.Clan clan : data.clans.values()) {

            List<String> morphNames = clan.members.keySet().stream()
                    .map(uuid -> data.playerMorphNames.getOrDefault(uuid, "Unknown"))
                    .collect(Collectors.toList());


            List<ClanInfo.ClientClanCat> clientCats = new ArrayList<>();

            for (ClanData.ClanCat cat : clan.clanCats.values()) {
                clientCats.add(new ClanInfo.ClientClanCat(
                        cat.catUUID,
                        cat.catName.getString(),
                        cat.catGender,
                        cat.catRank,
                        cat.catAge.getString(),
                        cat.catVariant,
                        cat.catParents.getString()
                ));
            }

            List<ClanInfo.ClientLogEntry> clientLogList = new ArrayList<>();

            for (ClanData.ClanLogEntry log : clan.logs) {
                clientLogList.add(new ClanInfo.ClientLogEntry(log.gameTimeID, log.message));
            }

            boolean canManage = data.canManage(clan, player.getUUID());

            list.add(new ClanInfo(
                    clan.clanUUID,
                    clan.name,
                    clan.color,
                    clan.leaderName,
                    clan.clanBioSentence,
                    canManage,
                    clan.members.size(),
                    morphNames,
                    clientCats,
                    clientLogList,
                    clan.clanSymbolIndex
            ));

        }


        ModPackets.sendToPlayer(new S2CClanListPacket(list), player);
        return 1;
    }

}
