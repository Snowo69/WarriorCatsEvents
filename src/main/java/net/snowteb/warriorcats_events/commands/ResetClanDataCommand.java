package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.client.ClientClanData;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.S2CSyncClanDataPacket;

public class ResetClanDataCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("reset")
                                        .executes((command)
                                                -> resetData(command.getSource()))
                                )
                        )
        );
    }

    private static int resetData(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            cap.reset();
            ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
        });

        source.sendSuccess(
                () -> Component.literal("Resetting player character data."),
                false
        );

        return 1;
    }
}
