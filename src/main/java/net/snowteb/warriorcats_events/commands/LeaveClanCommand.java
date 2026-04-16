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
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;

import java.util.UUID;

public class LeaveClanCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("leave")
                                        .executes(ctx -> method(ctx.getSource()))
                                )
                        ));
    }

    private static int method(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer sPlayer = source.getPlayerOrException();


        String morphName = sPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphName).orElse(sPlayer.getName().getString());

        UUID targetClanId = sPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

        ClanData data = ClanData.get(sPlayer.serverLevel().getServer().overworld());
        ClanData.Clan targetClan = data.getClan(targetClanId);

        if (targetClanId.equals(ClanData.EMPTY_UUID)) {
            sPlayer.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY));
            return 0;
        } else {
            if (targetClan == null) {
                sPlayer.sendSystemMessage(Component.literal("The target is in a clan that doesn't exist. Resetting their clan data...").withStyle(ChatFormatting.GRAY));
                sPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                    cap.setCurrentClanUUID(ClanData.EMPTY_UUID);
                });
                return 0;
            }
        }

        boolean isOwner = (targetClan.members.get(sPlayer.getUUID()) == ClanData.ClanPlayerRank.LEADER)
                && (targetClan.memberPerms.get(sPlayer.getUUID()) == ClanData.ClanPermissions.OWNER);
        if (isOwner) {
            sPlayer.sendSystemMessage(Component.literal("You can't leave when you are a leader.").withStyle(ChatFormatting.YELLOW));
            return 0;
        }


        data.removeMember(sPlayer, targetClanId);
        data.setDirty();

        sPlayer.sendSystemMessage(
                Component.empty()
                        .append("You have left ")
                        .append(Component.literal(targetClan.name).withStyle(Style.EMPTY.withColor(targetClan.color)))
        );

        for (UUID memberUUID : targetClan.members.keySet()) {
            ServerPlayer member = sPlayer.server.getPlayerList().getPlayer(memberUUID);
            if (member == null) continue;

            member.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(morphName)
                                    .withStyle(ChatFormatting.GOLD))
                            .append(" has left ")
                            .append(Component.literal(targetClan.name)
                                    .withStyle(Style.EMPTY.withColor(targetClan.color)))
            );
        }

        return 1;
    }
}
