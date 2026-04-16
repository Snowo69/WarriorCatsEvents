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
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;

import java.util.Map;
import java.util.UUID;

public class SetNewLeaderCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("setnewleader")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                        .executes(ctx ->
                                                                method(
                                                                        ctx.getSource(),
                                                                        EntityArgument.getPlayer(ctx, "player")
                                                                )
                                                        )
                                                )
                                        )
                                )

        );
    }


    private static int method(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer sPlayer = source.getPlayerOrException();

        String hostMorphName = sPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphName).orElse(sPlayer.getName().getString());
        String targetMorphName = targetPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphName).orElse(targetPlayer.getName().getString());

        UUID targetClanId = sPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);
        UUID currentMemberClanId = targetPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

        ClanData data = ClanData.get(targetPlayer.serverLevel().getServer().overworld());
        ClanData.Clan targetClan = data.getClan(targetClanId);
        ClanData.Clan currentMemberClan = data.getClan(currentMemberClanId);


        if (targetClanId.equals(ClanData.EMPTY_UUID)) {
            sPlayer.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY));
            return 0;
        }

        if (targetClan != null) {
            boolean canManage = targetClan.members.get(sPlayer.getUUID()) == ClanData.ClanPlayerRank.LEADER
                    && targetClan.memberPerms.get(sPlayer.getUUID()) == ClanData.ClanPermissions.OWNER;
            if (!canManage) {
                sPlayer.sendSystemMessage(Component.literal("You are not leader of your clan.").withStyle(ChatFormatting.YELLOW));
                return 0;
            }
        }

        if (targetPlayer == sPlayer) {
            sPlayer.sendSystemMessage(Component.literal("You are already a leader.").withStyle(ChatFormatting.YELLOW));
            return 0;
        }

        if (!currentMemberClanId.equals(ClanData.EMPTY_UUID)) {
            if (data.getClan(currentMemberClanId) == null) {
                sPlayer.sendSystemMessage(Component.literal("The target is in a clan that doesn't exist. Resetting their clan data...").withStyle(ChatFormatting.GRAY));
                targetPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                    cap.setCurrentClanUUID(ClanData.EMPTY_UUID);
                });
                return 0;
            }
        }

        if (targetClan == currentMemberClan && targetClan != null) {

            for (Map.Entry<UUID, ClanData.ClanPlayerRank> entry : targetClan.members.entrySet()) {
                if (entry.getValue() == ClanData.ClanPlayerRank.LEADER) {
                    entry.setValue(ClanData.ClanPlayerRank.WARRIOR);
                }
            }

            for (Map.Entry<UUID, ClanData.ClanPermissions> entry : targetClan.memberPerms.entrySet()) {
                if (entry.getValue() == ClanData.ClanPermissions.OWNER) {
                    entry.setValue(ClanData.ClanPermissions.MEMBER);
                }
            }


            data.changeMemberRank(targetPlayer, targetClanId, ClanData.ClanPlayerRank.LEADER);
            data.changeMemberPermissions(targetPlayer, targetClanId, ClanData.ClanPermissions.OWNER);

            targetClan.leaderName = hostMorphName;
            data.setDirty();

            String morphName = data.playerMorphNames.getOrDefault(targetPlayer.getUUID(), "Unknown");

            sPlayer.sendSystemMessage(
                    Component.empty()
                            .append("You have given the leadership to ")
                            .append(Component.literal(morphName).withStyle(ChatFormatting.GOLD))
            );

            for (UUID memberUUID : targetClan.members.keySet()) {
                ServerPlayer member = sPlayer.server.getPlayerList().getPlayer(memberUUID);
                if (member == null) continue;

                member.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal(targetMorphName)
                                        .withStyle(ChatFormatting.GOLD))
                                .append(" is now the new leader of ")
                                .append(Component.literal(targetClan.name).withStyle(Style.EMPTY.withColor(targetClan.color))
                                .append("!")
                ));
            }

        }

        return 1;
    }

}
