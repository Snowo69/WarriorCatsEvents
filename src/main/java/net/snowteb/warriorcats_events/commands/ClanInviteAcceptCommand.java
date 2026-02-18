package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.util.ClanInviteManager;

import java.util.List;
import java.util.UUID;

public class ClanInviteAcceptCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("invite")
                                        .then(Commands.literal("accept")
                                                .executes(ctx ->
                                                        method(ctx.getSource())
                                                )
                                        )
                                )
                        ));
    }

    private static int method(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        ClanInviteManager.ClanInvite invite = ClanInviteManager.getInvite(player);
        if (invite == null) {
            player.sendSystemMessage(
                    Component.literal("No invites pending.")
                            .withStyle(ChatFormatting.GRAY)
            );
            return 0;
        }

        UUID clanUUID = invite.clanUUID;
        ClanInviteManager.clear(player);

        ClanData clanData = ClanData.get(player.serverLevel());

        boolean joined = clanData.addMember(
                player,
                clanUUID,
                ClanData.ClanPlayerRank.WARRIOR
        );

        if (!joined) {
            player.sendSystemMessage(
                    Component.literal("Could not join the clan.")
                            .withStyle(ChatFormatting.RED)
            );
            return 0;
        }

        ClanData.Clan targetClan = clanData.getClan(clanUUID);
        if (targetClan != null) {

            String invitedPlayerMorphName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getMorphName)
                    .orElse(player.getName().getString());

            for (UUID memberUUID : targetClan.members.keySet()) {
                ServerPlayer member = player.server.getPlayerList().getPlayer(memberUUID);
                if (member == null) continue;

                member.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal(invitedPlayerMorphName)
                                        .withStyle(ChatFormatting.GOLD))
                                .append(" has joined ")
                                .append(Component.literal(targetClan.name)
                                        .withStyle(Style.EMPTY.withColor(targetClan.color)))
                                .append("!")
                );
            }
        }
        return 1;

    }

}
