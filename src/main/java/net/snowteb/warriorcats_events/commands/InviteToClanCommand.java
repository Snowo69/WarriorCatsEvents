package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.S2COpenRegisterClanScreenPacket;
import net.snowteb.warriorcats_events.util.ClanInviteManager;

import java.util.UUID;

public class InviteToClanCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("invite")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx ->
                                                        invitePlayer(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"))
                                        )
                                )
                        )
        ));
    }

    private static int invitePlayer(CommandSourceStack source, ServerPlayer invitedPlayer) throws CommandSyntaxException {
        ServerPlayer sPlayer = source.getPlayerOrException();


        String hostMorphName = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName).orElse(sPlayer.getName().getString());
        String invitedMorphName = invitedPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName).orElse(invitedPlayer.getName().getString());

        UUID invitingClanId = sPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);
        UUID currentClanId = invitedPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

        ClanData data = ClanData.get(invitedPlayer.serverLevel());
        ClanData.Clan clan = data.getClan(invitingClanId);

        if (invitedPlayer == sPlayer) {
            sPlayer.sendSystemMessage(Component.literal("You can't invite yourself to a clan.").withStyle(ChatFormatting.GRAY));
            return 0;
        }

        if (invitingClanId.equals(ClanData.EMPTY_UUID)) {
            sPlayer.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY));
            return 0;
        }

        if (clan != null) {
            boolean canInvite = data.canInvite(clan, sPlayer.getUUID());
            if (!canInvite) {
                sPlayer.sendSystemMessage(Component.literal("You can't invite players to your clan.").withStyle(ChatFormatting.YELLOW));
                return 0;
            }
        }

        if (ClanInviteManager.getInvite(invitedPlayer) != null) {
            sPlayer.sendSystemMessage(Component.literal("The target already has an invite pending.").withStyle(ChatFormatting.YELLOW));
            return 0;
        }

        if (!currentClanId.equals(ClanData.EMPTY_UUID)) {
            if (data.getClan(currentClanId) ==  null) {
                sPlayer.sendSystemMessage(Component.literal("The target is in a clan that doesn't exist. Resetting their clan info...").withStyle(ChatFormatting.GRAY));
                invitedPlayer.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                    cap.setCurrentClanUUID(ClanData.EMPTY_UUID);
                });
            } else {
                sPlayer.sendSystemMessage(Component.literal("The target is already in a clan.").withStyle(ChatFormatting.YELLOW));
            }
        }

        if (clan != null) {

            sPlayer.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("You have invited "))
                            .append(Component.literal(invitedMorphName).withStyle(ChatFormatting.GOLD))
                            .append(" to ")
                            .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color))
            ));

            invitedPlayer.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(hostMorphName).withStyle(ChatFormatting.AQUA))
                            .append(" has invited you to ")
                            .append(clan.name).withStyle(Style.EMPTY.withColor(clan.color))
            );

            ClanInviteManager.invite(invitedPlayer, invitingClanId, sPlayer);

            invitedPlayer.sendSystemMessage(
                    Component.empty()
                            .append(
                                    Component.literal("[ACCEPT]")
                                            .withStyle(style -> style
                                                    .withColor(ChatFormatting.GREEN)
                                                    .withItalic(true)
                                                    .withUnderlined(true)
                                                    .withClickEvent(
                                                            new ClickEvent(
                                                                    ClickEvent.Action.RUN_COMMAND,
                                                                    "/wce clan invite accept"
                                                            )
                                                    )
                                                    .withHoverEvent(
                                                            new HoverEvent(
                                                                    HoverEvent.Action.SHOW_TEXT,
                                                                    Component.literal("Accept the clan invite")
                                                                            .withStyle(ChatFormatting.GREEN)
                                                            )
                                                    )
                                            )
                            )

                            .append("       ")

                            .append(
                                    Component.literal("[DENY]")
                                            .withStyle(style -> style
                                                    .withColor(ChatFormatting.RED)
                                                    .withItalic(true)
                                                    .withUnderlined(true)
                                                    .withClickEvent(
                                                            new ClickEvent(
                                                                    ClickEvent.Action.RUN_COMMAND,
                                                                    "/wce clan invite deny"
                                                            )
                                                    )
                                                    .withHoverEvent(
                                                            new HoverEvent(
                                                                    HoverEvent.Action.SHOW_TEXT,
                                                                    Component.literal("Decline the clan invite")
                                                                            .withStyle(ChatFormatting.RED)
                                                            )
                                                    )
                                            )
                            )
            );
        }

        return 1;
    }

}
