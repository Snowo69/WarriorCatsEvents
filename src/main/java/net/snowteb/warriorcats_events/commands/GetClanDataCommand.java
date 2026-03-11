package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;

public class GetClanDataCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("info")
                                .then(Commands.literal("get").requires(source -> {
                                            if (source.getEntity() instanceof ServerPlayer player) {
                                                return WarriorCatsEvents.Devs.isDev(player.getUUID()) || source.hasPermission(2);
                                            }
                                            return source.hasPermission(2);
                                })
                                        .executes(ctx -> getData(ctx.getSource(), ctx.getSource().getPlayerOrException()))
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx ->
                                                        getData(ctx.getSource(), EntityArgument.getPlayer(ctx, "player"))))
                                )
        ));
    }

    private static int getData(CommandSourceStack source, ServerPlayer targetToShow) throws CommandSyntaxException {

        targetToShow.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {

            String genderText = switch (cap.getGenderData()) {
                case 0 -> "Male";
                case 1 -> "Female";
                case 2 -> "Non binary";
                default -> "Unspecified";
            };

            source.sendSuccess(
                    () -> Component.literal("Showing clan data from ").append(targetToShow.getName().copy()).append(Component.literal(
                                    "\n==================" +
                                    "\nClan: " + cap.getClanName() +
                                    "\nClanUUID: " + cap.getCurrentClanUUID() +
                                    "\nCharacter Name: " + cap.getMorphName() +
                                    "\nMate: " + cap.getMateName().copy() +
                                    "\nMateUUID: " + cap.getMateUUID() +
                                    "\nPrefix: " + cap.getPrefix() +
                                    "\nSuffix: " + cap.getSufix() +
                                    "\nPreferred Variant: " + cap.getVariantData() +
                                    "\nGender: " + genderText +
                                    "\nAge: " + cap.getMorphAge() +
                                    "\nUses suffixes: " + cap.isUseSufixes() +
                                    "\nRegistered: " + cap.isFirstLoginHandled() +
                                            "\n=================="

                    ).withStyle(ChatFormatting.GRAY)),
                    false
            );

        });
        return 1;

    }
}