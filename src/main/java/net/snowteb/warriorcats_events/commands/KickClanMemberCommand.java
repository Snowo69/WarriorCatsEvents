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

import java.util.UUID;

public class KickClanMemberCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("kick")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx ->
                                                        method(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"))
                                                )
                                        )
                                )
                        ));
    }

    private static int method(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer sPlayer = source.getPlayerOrException();


        String hostMorphName = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName).orElse(sPlayer.getName().getString());
        String kickedMorphName = targetPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName).orElse(targetPlayer.getName().getString());

        UUID targetClanId = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);
        UUID currentMemberClanId = targetPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

        ClanData data = ClanData.get(targetPlayer.serverLevel());
        ClanData.Clan targetClan = data.getClan(targetClanId);
        ClanData.Clan currentMemberClan = data.getClan(currentMemberClanId);

        if (targetClanId.equals(ClanData.EMPTY_UUID)) {
            sPlayer.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY));
            return 0;
        }

        if (targetClan != null) {
            boolean canKick = data.canManage(targetClan, sPlayer.getUUID());
            boolean canKickThisOne = data.canManagePlayer(targetClanId, sPlayer.getUUID(), targetPlayer.getUUID());

            if (!canKick || !canKickThisOne) {
                sPlayer.sendSystemMessage(Component.literal("You don't have permission to do that.").withStyle(ChatFormatting.YELLOW));
                return 0;
            }
        }

        if (targetPlayer == sPlayer) {
            sPlayer.sendSystemMessage(Component.literal("You can't kick yourself.").withStyle(ChatFormatting.YELLOW));
            return 0;
        }

        if (!currentMemberClanId.equals(ClanData.EMPTY_UUID)) {
            if (data.getClan(currentMemberClanId) == null) {
                sPlayer.sendSystemMessage(Component.literal("The target is in a clan that doesn't exist. Resetting their clan data...").withStyle(ChatFormatting.GRAY));
                targetPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                    cap.setCurrentClanUUID(ClanData.EMPTY_UUID);
                });
                return 0;
            }
        }

        if (targetClan == currentMemberClan) {
            data.removeMember(targetPlayer,  targetClanId);
            data.setDirty();

            String morphName = data.playerMorphNames.getOrDefault(targetPlayer.getUUID(), "Unknown");

            sPlayer.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(kickedMorphName).withStyle(ChatFormatting.GOLD))
                            .append(" has been removed from ")
                            .append(Component.literal(targetClan.name).withStyle(Style.EMPTY.withColor(targetClan.color)))
            );
        }

        return 1;
    }

}
