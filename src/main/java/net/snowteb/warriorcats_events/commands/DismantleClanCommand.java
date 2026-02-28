package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;

import java.util.UUID;

public class DismantleClanCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("disband")
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
            boolean can = clan.memberPerms.get(player.getUUID()) == ClanData.ClanPermissions.OWNER;
            if (can) {
                data.deleteClan(level, clan.clanUUID);
                player.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)))
                                .append(" has been disbanded.")
                );
                data.setDirty();
            } else {
                player.sendSystemMessage(
                        Component.empty()
                                .append("You are not leader of")
                                .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)))
                                .append(".")
                );
            }
        }

        return 1;
    }

}
