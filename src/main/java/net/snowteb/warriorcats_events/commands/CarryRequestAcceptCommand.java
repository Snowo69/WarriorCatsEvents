package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.managers.CarryPlayerRequestManager;
import tocraft.walkers.api.PlayerShape;

public class CarryRequestAcceptCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("carryRequest")

                                .then(Commands.literal("accept")
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
                    Component.literal("No requests pending.")
                            .withStyle(ChatFormatting.GRAY)
            );
            return 0;
        }

        Entity ent = player.serverLevel().getEntity(request.requester);
        if (ent == null) {
            source.sendFailure(Component.literal("Player not found."));
            return 0;
        } else {
            if (ent instanceof ServerPlayer requester && PlayerShape.getCurrentShape(requester) instanceof WCatEntity) {
                player.startRiding(requester);
                CarryPlayerRequestManager.clear(player);
            }
        }

        return 1;

    }

}
