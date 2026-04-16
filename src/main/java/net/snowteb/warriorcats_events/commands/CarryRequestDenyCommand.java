package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
import net.snowteb.warriorcats_events.managers.CarryPlayerRequestManager;

public class CarryRequestDenyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("carryRequest")

                                .then(Commands.literal("deny")
                                        .executes(ctx ->
                                                method(ctx.getSource())
                                        )
                                )

                        ));
    }

    private static int method(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        CarryPlayerRequestManager.CarryRequest request = CarryPlayerRequestManager.getRequest(player);
        if (request == null) {
            player.sendSystemMessage(
                    Component.literal("No requests pending.").withStyle(ChatFormatting.GRAY)
            );
            return 0;
        }

        CarryPlayerRequestManager.clear(player);


        String deniedPlayerName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphName)
                .orElse(player.getName().getString());


        player.sendSystemMessage(
                Component.empty()
                        .append(Component.literal("You declined the carry request.").withStyle(ChatFormatting.GRAY))
        );


        ServerPlayer requester = player.server.getPlayerList().getPlayer(request.requester);
        if (requester != null) {
            requester.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(deniedPlayerName)
                                    .withStyle(ChatFormatting.GOLD))
                            .append(" declined your carry request.")
            );
        }

        return 1;
    }
}


