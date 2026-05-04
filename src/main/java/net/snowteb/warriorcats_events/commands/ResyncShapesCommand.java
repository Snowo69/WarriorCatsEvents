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
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.others.OpenPlayerCatDataScreenPacket;
import tocraft.walkers.api.PlayerShape;

import java.util.List;
import java.util.UUID;

public class ResyncShapesCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("resync-shapes")
                                .executes((command)
                                        -> updateShapesToClient(command.getSource()
                                        )
                                )
                        )
        );
    }

    private static int updateShapesToClient(CommandSourceStack source) throws CommandSyntaxException {

        ServerPlayer player = source.getPlayerOrException();

        if (player.getServer() == null) return 0;

        List<ServerPlayer> players = player.getServer().getPlayerList().getPlayers();

        for (ServerPlayer p : players) {
            if (p != null) {
                if (PlayerShape.getCurrentShape(p) instanceof WCatEntity) {
                    PlayerShape.sync(p, player);
                }
            }
        }

        player.sendSystemMessage(Component.literal("Shapes re-synced!").withStyle(ChatFormatting.ITALIC));
        return 1;
    }
}
