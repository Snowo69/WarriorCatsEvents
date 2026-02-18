package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.util.ClanInviteManager;

import java.util.UUID;

public class ClanInviteDenyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("invite")
                                        .then(Commands.literal("deny")
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
                        Component.literal("No invites pending.").withStyle(ChatFormatting.GRAY)
                );
                return 0;
            }

            UUID clanUUID = invite.clanUUID;
            UUID inviterUUID = invite.inviter;

            ClanInviteManager.clear(player);

            ClanData data = ClanData.get(player.serverLevel());
            ClanData.Clan clan = data.getClan(clanUUID);

            String deniedPlayerName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getMorphName)
                    .orElse(player.getName().getString());

            if (clan != null) {
                player.sendSystemMessage(
                        Component.empty()
                                .append("You declined the invite to ")
                                .append(Component.literal(clan.name)
                                        .withStyle(Style.EMPTY.withColor(clan.color)))
                                .append(".")
                );
            }

            ServerPlayer inviter = player.server.getPlayerList().getPlayer(inviterUUID);
            if (inviter != null && clan != null) {
                inviter.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal(deniedPlayerName)
                                        .withStyle(ChatFormatting.GOLD))
                                .append(" declined your invite to ")
                                .append(Component.literal(clan.name)
                                        .withStyle(Style.EMPTY.withColor(clan.color)))
                                .append(".")
                );
            }

            return 1;
        }
    }


