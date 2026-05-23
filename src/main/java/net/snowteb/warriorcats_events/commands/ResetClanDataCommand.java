package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.attachments.CapabilityManager;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;

public class ResetClanDataCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("info")
                                .then(Commands.literal("reset")
                                        .executes((command)
                                                -> resetData(command.getSource()))
                                )
                        )
        );
    }

    private static int resetData(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        ClanData data = ClanData.get(player.serverLevel().getServer().overworld());

        CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_WCE_DATA, cap -> {
            cap.reset();
            ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
            data.playerMorphNames.put(player.getUUID(), cap.getMorphName());

            WCGenetics.PackedGeneticData morphData =
                    new WCGenetics.PackedGeneticData(cap.getPlayerGenetics(),
                            cap.getPlayerGeneticalVariants(),
                            cap.getPlayerChimeraGenetics(),
                            cap.getPlayerChimeraVariants(),
                            cap.isOnGeneticalSkin(), cap.getVariantData());

            data.playerMorphData.put(player.getUUID(), morphData);
            data.setDirty();
        });

        source.sendSuccess(
                () -> Component.literal("Resetting player character data."),
                false
        );

        return 1;
    }
}
