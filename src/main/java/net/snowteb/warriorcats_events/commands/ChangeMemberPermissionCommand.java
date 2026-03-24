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
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class ChangeMemberPermissionCommand {

    private static final SuggestionProvider<CommandSourceStack> PERMS_SUGGESTIONS =
            (ctx, builder) -> SharedSuggestionProvider.suggest(
                    Arrays.stream(ClanData.ClanPermissions.values())
                            .filter(rank -> rank != ClanData.ClanPermissions.OWNER)
                            .map(Enum::name)
                            .toList(),
                    builder
            );





    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("changeperms")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .then(Commands.argument("perms", StringArgumentType.word())
                                                        .suggests(PERMS_SUGGESTIONS)
                                                        .executes(ctx ->
                                                                method(
                                                                        ctx.getSource(),
                                                                        EntityArgument.getPlayer(ctx, "player"),
                                                                        StringArgumentType.getString(ctx, "perms")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }


    private static int method(CommandSourceStack source, ServerPlayer targetPlayer, String perms) throws CommandSyntaxException {
        ServerPlayer sPlayer = source.getPlayerOrException();

        ClanData.ClanPermissions newPerms;

        try {
            newPerms = ClanData.ClanPermissions.valueOf(perms.toUpperCase());
        } catch (IllegalArgumentException e) {
            source.sendFailure(Component.literal("Invalid perms: " + perms).withStyle(ChatFormatting.RED));
            return 0;
        }

        if (newPerms == ClanData.ClanPermissions.OWNER) {
            source.sendFailure(Component.literal("Invalid perms: " + perms).withStyle(ChatFormatting.RED));
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
                sPlayer.sendSystemMessage(Component.literal("You don't have permission to do that.").withStyle(ChatFormatting.RED));
                return 0;
            }
        }

        if (targetPlayer == sPlayer) {
            sPlayer.sendSystemMessage(Component.literal("You can't change your own perms.").withStyle(ChatFormatting.YELLOW));
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
            data.changeMemberPermissions(targetPlayer, targetClanId, newPerms);
            data.setDirty();

            sPlayer.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(targetPlayer.getName().getString()).withStyle(ChatFormatting.GOLD))
                            .append(" has been made ")
                            .append(Component.literal(perms).withStyle(ChatFormatting.RED))
            );

            for (UUID memberUUID : targetClan.members.keySet()) {
                ServerPlayer member = sPlayer.server.getPlayerList().getPlayer(memberUUID);
                if (member == null) continue;

                member.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal(targetPlayer.getName().getString())
                                        .withStyle(ChatFormatting.GOLD))
                                .append(" is now ")
                                .append(Component.literal(perms).withStyle(ChatFormatting.RED)
                ));
            }

        }

        return 1;
    }

}
