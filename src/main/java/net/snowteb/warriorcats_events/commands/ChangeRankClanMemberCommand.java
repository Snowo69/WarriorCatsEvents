package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class ChangeRankClanMemberCommand {

    private static final SuggestionProvider<CommandSourceStack> RANK_SUGGESTIONS =
            (ctx, builder) -> SharedSuggestionProvider.suggest(
                    Arrays.stream(ClanData.ClanPlayerRank.values())
                            .filter(rank -> rank != ClanData.ClanPlayerRank.LEADER)
                            .map(Enum::name)
                            .toList(),
                    builder
            );





    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("changerole")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.argument("role", StringArgumentType.word())
                                                        .suggests(RANK_SUGGESTIONS)
                                                        .executes(ctx ->
                                                                method(
                                                                        ctx.getSource(),
                                                                        EntityArgument.getPlayer(ctx, "player"),
                                                                        StringArgumentType.getString(ctx, "role")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }


    private static int method(CommandSourceStack source, ServerPlayer targetPlayer, String role) throws CommandSyntaxException {
        ServerPlayer sPlayer = source.getPlayerOrException();

        ClanData.ClanPlayerRank newRank;

        try {
            newRank = ClanData.ClanPlayerRank.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            source.sendFailure(Component.literal("Invalid role: " + role).withStyle(ChatFormatting.RED));
            return 0;
        }

        if (newRank == ClanData.ClanPlayerRank.LEADER) {
            source.sendFailure(Component.literal("Invalid role: " + role).withStyle(ChatFormatting.RED));
            return 0;
        }

        String hostMorphName = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName).orElse(sPlayer.getName().getString());
        String targetMorphName = targetPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName).orElse(targetPlayer.getName().getString());

        UUID targetClanId = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);
        UUID currentMemberClanId = targetPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

        ClanData data = ClanData.get(targetPlayer.serverLevel().getServer().overworld());
        ClanData.Clan targetClan = data.getClan(targetClanId);
        ClanData.Clan currentMemberClan = data.getClan(currentMemberClanId);

        if (targetClanId.equals(ClanData.EMPTY_UUID)) {
            sPlayer.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY));
            return 0;
        }

        if (targetClan != null) {
            boolean canKick = data.canManage(targetClan, sPlayer.getUUID());
            boolean canKickThis = data.canManagePlayer(targetClanId, sPlayer.getUUID(), targetPlayer.getUUID());
            if (!canKick || !canKickThis) {
                sPlayer.sendSystemMessage(Component.literal("You don't have permission to do that.").withStyle(ChatFormatting.YELLOW));
                return 0;
            }
        }

        if (targetPlayer == sPlayer) {
            sPlayer.sendSystemMessage(Component.literal("You can't change your own role.").withStyle(ChatFormatting.YELLOW));
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

        int deputies = 0;
        int medicine = 0;
        int medicineApp = 0;
        for (Map.Entry<UUID, ClanData.ClanPlayerRank> entry : targetClan.members.entrySet()) {
            if (entry.getValue() == ClanData.ClanPlayerRank.DEPUTY) deputies++;
            if (entry.getValue() == ClanData.ClanPlayerRank.MEDICINE) medicine++;
            if (entry.getValue() == ClanData.ClanPlayerRank.MEDICINEAPP) medicineApp++;
        }

        if (newRank == ClanData.ClanPlayerRank.DEPUTY && deputies >= 1) {
            sPlayer.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("Role limit reached. ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal("(" + deputies + ")").withStyle(ChatFormatting.GOLD))
            );
            return 0;
        }
        if (newRank == ClanData.ClanPlayerRank.MEDICINE && medicine >= 2) {
            sPlayer.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("Role limit reached. ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal("(" + medicine + ")").withStyle(ChatFormatting.GOLD))
            );
            return 0;
        }
        if (newRank == ClanData.ClanPlayerRank.MEDICINEAPP && medicineApp >= 1) {
            sPlayer.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("Role limit reached. ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal("(" + medicineApp + ")").withStyle(ChatFormatting.GOLD))
            );
            return 0;
        }

        if (targetClan == currentMemberClan) {
            data.changeMemberRank(targetPlayer, targetClanId, newRank);
            data.setDirty();

            String morphName = data.playerMorphNames.getOrDefault(targetPlayer.getUUID(), "Unknown");

            sPlayer.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(morphName).withStyle(ChatFormatting.GOLD))
                            .append(" has been promoted to ")
                            .append(Component.literal(role).withStyle(ChatFormatting.AQUA))
            );

            for (UUID memberUUID : targetClan.members.keySet()) {
                ServerPlayer member = sPlayer.server.getPlayerList().getPlayer(memberUUID);
                if (member == null) continue;

                member.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal(targetMorphName)
                                        .withStyle(ChatFormatting.GOLD))
                                .append(" is now ")
                                .append(Component.literal(role).withStyle(ChatFormatting.AQUA)
                                .append("!")
                ));
            }

        }

        return 1;
    }

}
