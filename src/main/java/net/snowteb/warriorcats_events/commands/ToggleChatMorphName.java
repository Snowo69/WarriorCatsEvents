package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;

public class ToggleChatMorphName {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("chatMorphName")
                                .then(Commands.literal("toggleChatMorphName")
                                        .executes(ctx -> method(ctx.getSource().getPlayerOrException()))
                                )
                        )
        );

        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("chatMorphName")
                                .then(Commands.literal("toggleFancyFont")
                                        .executes(ctx -> method2(ctx.getSource().getPlayerOrException()))
                                )
                        )
        );
    }

    public static int method(ServerPlayer player) {

        player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            boolean morphNameInChat = cap.isMorphNameInChat();

            Component message = morphNameInChat ?
                    Component.empty()
                            .append("Show morph name in chat: ")
                            .append(Component.literal("OFF").withStyle(ChatFormatting.RED))
                    :
                    Component.empty()
                            .append("Show morph name in chat: ")
                            .append(Component.literal("ON").withStyle(ChatFormatting.GREEN));

            cap.setMorphNameInChat(!morphNameInChat);
            player.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("[WCE] ").withStyle(ChatFormatting.GOLD))
                            .append(message.copy())
            );

        });

        return 1;
    }

    public static int method2(ServerPlayer player) {

        player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            boolean usingFancyFont = cap.isUsingFancyFont();

            Component message = usingFancyFont ?
                    Component.empty()
                            .append("Fancy font: ")
                            .append(Component.literal("OFF").withStyle(ChatFormatting.RED))
                    :
                    Component.empty()
                            .append("Fancy font: ")
                            .append(Component.literal("ON").withStyle(ChatFormatting.GREEN));

            cap.setUsingFancyFont(!usingFancyFont);
            player.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("[WCE] ").withStyle(ChatFormatting.GOLD))
                            .append(message.copy())
            );

        });

        return 1;
    }

}
