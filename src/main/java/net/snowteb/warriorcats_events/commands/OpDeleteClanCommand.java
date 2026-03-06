package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class OpDeleteClanCommand {

    private static final SuggestionProvider<CommandSourceStack> CLAN_SUGGESTIONS =
            (ctx, builder) -> {
                ServerLevel level = ctx.getSource().getLevel();
                ClanData data = ClanData.get(level);
                return SharedSuggestionProvider.suggest(data.clans.values().stream()
                        .map(clan -> clan.normalizedName).toList(), builder);
            };


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("clan")
                                .then(Commands.literal("adminForceDelete").requires(source -> source.hasPermission(3))
                                        .then(Commands.argument("clan", StringArgumentType.word())
                                                .suggests(CLAN_SUGGESTIONS)
                                                .executes(ctx ->
                                                        method(
                                                                ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "clan")
                                                        )
                                                )
                                        )
                                )
                        )

        );
    }


    private static int method(CommandSourceStack source, String clanName) throws CommandSyntaxException {

        ServerLevel level = source.getLevel();
        ClanData data = ClanData.get(level);

        ClanData.Clan targetClan = data.getClanByName(clanName);

        if (targetClan == null) {
            source.sendFailure(Component.literal("The provided clan does not exist.").withStyle(ChatFormatting.RED));
            return 0;
        }

        int color = targetClan.color;
        String clanRealName = targetClan.name;

        data.deleteClan(level, targetClan.clanUUID);

        source.sendSystemMessage(
                Component.empty()
                                .append(Component.literal("Clan successfully deleted: ").withStyle(ChatFormatting.GREEN))
                        .append(Component.literal(clanRealName).withStyle(Style.EMPTY.withColor(color))));


        return 1;
    }

}
