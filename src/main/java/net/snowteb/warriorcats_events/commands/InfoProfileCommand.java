package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.attachments.CapabilityManager;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.attachments.WCEPlayerData;
import net.snowteb.warriorcats_events.diseases.Diseaseable;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.others.OpenPlayerCatDataScreenPacket;

import java.util.UUID;

public class InfoProfileCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("info")
                                .then(Commands.literal("profile")
                                        .executes((command)
                                                -> openScreen(command.getSource()))
                                )
                        )
        );
    }

    private static int openScreen(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        CapabilityManager.attachmentProvider(player, ModAttachments.PLAYER_WCE_DATA, cap -> {
            ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
        });

        String name = player.getData(ModAttachments.PLAYER_WCE_DATA).getMorphName();
        String clanName = player.getData(ModAttachments.PLAYER_WCE_DATA).getClanName();
        int gender = player.getData(ModAttachments.PLAYER_WCE_DATA).getGenderData();

        String genderText = switch (gender) {
            case 0 -> "Tom-cat";
            case 1 -> "She-cat";
            default -> player.getData(ModAttachments.PLAYER_WCE_DATA).getGenderText();
        };

        String mateName = player.getData(ModAttachments.PLAYER_WCE_DATA).getMateName().getString();

        WCEPlayerData.Age age = player.getData(ModAttachments.PLAYER_WCE_DATA).getMorphAge();

        int targetKitCooldown = player.getData(ModAttachments.PLAYER_WCE_DATA).getPlayerKitsCooldown();

        int myKitCooldown = player.getData(ModAttachments.PLAYER_WCE_DATA).getPlayerKitsCooldown();

        String bio = player.getData(ModAttachments.PLAYER_WCE_DATA).getCharacterBio();


        WCEPlayerData.PackedData targetData =
                new WCEPlayerData.PackedData(name, clanName, genderText, mateName, age, targetKitCooldown, bio);


        UUID targetUUID = player.getUUID();

        if (player instanceof Diseaseable<?> diseaseable) {
            diseaseable.onChange();
        }

        ModPackets.sendToPlayer(new OpenPlayerCatDataScreenPacket(targetData, targetUUID, myKitCooldown, true), player);


        return 1;
    }
}
