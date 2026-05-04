package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.OpenClanSetupScreenPacket;
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

        player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(clanData -> {
            ModPackets.sendToPlayer(new S2CSyncClanDataPacket(clanData), player);
        });

        String name = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphName).orElse("Unnamed");
        String clanName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(cap -> cap.getClanName(player.serverLevel())).orElse("No clan");
        int gender = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getGenderData).orElse(2);
        String genderText = switch (gender) {
            case 0 -> "Tom-cat";
            case 1 -> "She-cat";
            default -> player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                    .map(WCEPlayerData::getGenderText).orElse("Non-binary");
        };

        String mateName = (player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMateName).orElse(Component.literal("No mate"))).getString();

        WCEPlayerData.Age age = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphAge).orElse(WCEPlayerData.Age.ADULT);

        int targetKitCooldown = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getPlayerKitsCooldown).orElse(1);

        int myKitCooldown = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getPlayerKitsCooldown).orElse(1);

        String bio = (player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getCharacterBio).orElse(""));


        WCEPlayerData.PackedData targetData =
                new WCEPlayerData.PackedData(name, clanName, genderText, mateName, age, targetKitCooldown, bio);


        UUID targetUUID = player.getUUID();

        ModPackets.sendToPlayer(new OpenPlayerCatDataScreenPacket(targetData, targetUUID, myKitCooldown, true), player);


        return 1;
    }
}
