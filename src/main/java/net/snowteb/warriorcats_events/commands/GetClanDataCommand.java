package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;

public class GetClanDataCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("get")
                                        .executes(ctx -> getData(ctx.getSource(), ctx.getSource().getPlayerOrException()))
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx ->
                                                        getData(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"))))
                                )
        ));
    }

    private static int getData(CommandSourceStack source, ServerPlayer targetToShow) throws CommandSyntaxException {

        targetToShow.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {

            source.sendSuccess(
                    () -> Component.literal("Showing clan data from ").append(targetToShow.getName().copy()).append(Component.literal(
                                    "\n==================" +
                                    "\nClan: " + cap.getClanName() +
                                    "\nCharacter Name: " + cap.getMorphName() +
                                    "\nMate: " + cap.getMateName().copy() +
                                    "\nMateUUID: " + cap.getMateUUID() +
                                    "\nPrefix: " + cap.getPrefix() +
                                    "\nSuffix: " + cap.getSufix() +
                                    "\nPrefered Variant: " + cap.getVariantData() +
                                    "\nGender: " + cap.getGenderData() +
                                    "\nAge: " + cap.getMorphAge() +
                                    "\nUses suffixes: " + cap.isUseSufixes() +
                                    "\nFirst login Registered: " + cap.isFirstLoginHandled() +
                                            "\n=================="

                    ).withStyle(ChatFormatting.GRAY)),
                    false
            );

        });
        return 1;

    }
}