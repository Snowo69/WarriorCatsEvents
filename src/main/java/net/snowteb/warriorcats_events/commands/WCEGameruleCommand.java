package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.skills.ISkillData;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;

public class WCEGameruleCommand {

    public enum ConfigType {
        THIRST,
        SKILL,
        AGGRESSIVE_ANIMALS,
        LEAP,
        CHOOSE_SPAWN_LOC,
        PROTECT_TERRITORY_CONTAINERS,
        PROTECT_TERRITORY_BLOCKS,

    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("gamerule")
                        .then(Commands.literal("wceThirstSystem")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> method(ctx.getSource().getPlayerOrException(), BoolArgumentType.getBool(ctx, "value"), ConfigType.THIRST))
                                )
                        ));

        dispatcher.register(
                Commands.literal("gamerule")
                        .then(Commands.literal("wceSkillSystem")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> method(ctx.getSource().getPlayerOrException(), BoolArgumentType.getBool(ctx, "value"), ConfigType.SKILL))
                                )
                        ));

        dispatcher.register(
                Commands.literal("gamerule")
                        .then(Commands.literal("wceAggressiveAnimals")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> method(ctx.getSource().getPlayerOrException(), BoolArgumentType.getBool(ctx, "value"), ConfigType.AGGRESSIVE_ANIMALS))
                                )
                        ));

        dispatcher.register(
                Commands.literal("gamerule")
                        .then(Commands.literal("wceLeapSystem")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> method(ctx.getSource().getPlayerOrException(), BoolArgumentType.getBool(ctx, "value"), ConfigType.LEAP))
                                )
                        ));

        dispatcher.register(
                Commands.literal("gamerule")
                        .then(Commands.literal("wceChooseSpawnLocation")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> method(ctx.getSource().getPlayerOrException(), BoolArgumentType.getBool(ctx, "value"), ConfigType.CHOOSE_SPAWN_LOC))
                                )
                        ));

        dispatcher.register(
                Commands.literal("gamerule")
                        .then(Commands.literal("wceProtectContainersInTerritory")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> method(ctx.getSource().getPlayerOrException(), BoolArgumentType.getBool(ctx, "value"), ConfigType.PROTECT_TERRITORY_CONTAINERS))
                                )
                        ));
        dispatcher.register(
                Commands.literal("gamerule")
                        .then(Commands.literal("wceProtectBlocksInTerritory")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> method(ctx.getSource().getPlayerOrException(), BoolArgumentType.getBool(ctx, "value"), ConfigType.PROTECT_TERRITORY_BLOCKS))
                                )
                        ));
    }

    public static int method(ServerPlayer player, boolean argument, ConfigType commandType) {

        ServerLevel level = player.serverLevel();

        switch (commandType) {
            case THIRST:
                WCEServerConfig.SERVER.THIRST.set(argument);
                break;

            case LEAP:
                WCEServerConfig.SERVER.LEAP_SERVER.set(argument);
                break;

            case SKILL:
                WCEServerConfig.SERVER.SKILL_TREE_SERVER.set(argument);
                for (ServerPlayer toAffect : level.getServer().getPlayerList().getPlayers()) {
                    toAffect.getCapability(PlayerSkillProvider.SKILL_DATA).ifPresent(skillData -> {
                        if (!argument) PlayerSkill.removeAttributes(toAffect);
                        else PlayerSkill.reviveAttributes(toAffect, skillData);
                    });
                }
                break;

            case AGGRESSIVE_ANIMALS:
                WCEServerConfig.SERVER.ENHANCED_ANIMALS.set(argument);
                break;

            case CHOOSE_SPAWN_LOC:
                WCEServerConfig.SERVER.TELEPORT_WHEN_JOIN.set(argument);
                break;

            case PROTECT_TERRITORY_CONTAINERS:
                WCEServerConfig.SERVER.PROTECT_CONTAINERS.set(argument);
                break;

            case PROTECT_TERRITORY_BLOCKS:
                WCEServerConfig.SERVER.PROTECT_PLACE_AND_BREAK_BLOCKS.set(argument);
                break;
        }

        player.sendSystemMessage(Component.literal(commandType.name() + " set to " + argument));


        return 1;
    }

}
