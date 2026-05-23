package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.OpenCreateMorphPacket;

public class OpenSummonCatCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("summon").requires(
                                source -> source.hasPermission(3) || (source.getEntity() instanceof ServerPlayer player && player.isCreative()))
                                .executes((command)
                                        -> method(command.getSource()))
                        )
        );
    }

    private static int method(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        ModPackets.sendToPlayer(new OpenCreateMorphPacket(true), player);

        source.sendSuccess(
                () -> Component.literal("Used summon"),
                false
        );

        return 1;
    }
}
