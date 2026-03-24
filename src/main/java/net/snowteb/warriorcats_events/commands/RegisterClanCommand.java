package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2COpenRegisterClanScreenPacket;

import java.util.UUID;

public class RegisterClanCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("register")
                                        .executes((command)
                                                -> openScreen(command.getSource()))
                                )
                        )
        );
    }

    private static int openScreen(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        String clanName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getClanName).orElse("clan");
        String morphName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName).orElse(player.getName().getString());
        UUID currentClanId = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

        if (!currentClanId.equals(ClanData.EMPTY_UUID)) {
            ClanData data = ClanData.get(player.serverLevel().getServer().overworld());
            if (data.getClan(currentClanId) ==  null) {
                player.sendSystemMessage(Component.literal("You are in a clan that doesn't exist. Resetting your clan info...").withStyle(ChatFormatting.GRAY));
                player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                    cap.setCurrentClanUUID(ClanData.EMPTY_UUID);
                });
            } else {
                player.sendSystemMessage(Component.literal("You must leave your current clan to create one.").withStyle(ChatFormatting.YELLOW));
            }
            return 0;
        }

        ModPackets.sendToPlayer(new S2COpenRegisterClanScreenPacket(clanName, morphName), player);

        source.sendSuccess(
                () -> Component.literal("Opening Clan Register menu..."),
                false
        );

        return 1;
    }
}
