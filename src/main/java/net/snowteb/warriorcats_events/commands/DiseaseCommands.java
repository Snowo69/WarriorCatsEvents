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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.snowteb.warriorcats_events.diseases.*;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiseaseCommands {

    private static final SuggestionProvider<CommandSourceStack> DISEASES =
            (ctx, builder) -> {
                List<String> diseaseIDS = DiseaseRegistry.getList().values().stream().map(DiseaseType::getID).toList();
                return SharedSuggestionProvider.suggest(diseaseIDS, builder);
            };


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("disease")
                                .requires(source -> source.hasPermission(3))
                                .then(Commands.literal("give")
                                        .then(Commands.argument("entity", EntityArgument.entities())
                                                .then(Commands.argument("disease", StringArgumentType.greedyString())
                                                        .suggests(DISEASES)

                                                        .executes(ctx -> {

                                                            Collection<? extends Entity> entities =
                                                                    EntityArgument.getEntities(ctx, "entity");

                                                            String diseaseID =
                                                                    StringArgumentType.getString(ctx, "disease");

                                                            return method(ctx.getSource(), entities, diseaseID);
                                                        })
                                                )
                                        )
                                )

                                .then(Commands.literal("remove")
                                        .then(Commands.argument("entity", EntityArgument.entities())
                                                .then(Commands.argument("disease", StringArgumentType.greedyString())
                                                        .suggests(DISEASES)
                                                        .executes(ctx -> {
                                                            Collection<? extends Entity> entities =
                                                                    EntityArgument.getEntities(ctx, "entity");

                                                            String diseaseID =
                                                                    StringArgumentType.getString(ctx, "disease");

                                                            return method2(ctx.getSource(), entities, diseaseID);
                                                        })
                                                )
                                        )
                                )

                                .then(Commands.literal("clear")
                                        .then(Commands.argument("entity", EntityArgument.entities())
                                                .executes(ctx -> {
                                                    Collection<? extends Entity> entities =
                                                            EntityArgument.getEntities(ctx, "entity");

                                                    return method3(ctx.getSource(), entities);
                                                })

                                        )

                                        .executes(ctx -> {
                                            Collection<Entity> entities = new ArrayList<>();
                                            entities.add(ctx.getSource().getEntity());

                                            return method3(ctx.getSource(), entities);
                                        })
                                )
                        )
        );
    }

    private static int method(CommandSourceStack source, Collection<? extends Entity> entities, String disease) throws CommandSyntaxException {

        ServerPlayer player = source.getPlayerOrException();

        if (!WCEServerConfig.SERVER.DISEASES.get()) {
            player.sendSystemMessage(Component.literal("Diseases are disabled in this world.")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        DiseaseType<?> type = DiseaseRegistry.getByID(disease);
        if (type == null) return 0;
        int entityCount = 0;

        for (Entity entity : entities) {
            if (!(entity instanceof Diseaseable<?> diseaseable)) {continue;}

            if (!diseaseable.addDisease(type, false)) {
                if (diseaseable.hasDisease(type)) {
                    player.sendSystemMessage(Component.literal("The target already has the disease.")
                            .withStyle(ChatFormatting.RED));
                } else {
                    player.sendSystemMessage(Component.literal("The disease couldn't be applied.")
                            .withStyle(ChatFormatting.RED));
                }
            } else {
                entityCount++;
            }

        }

        player.sendSystemMessage(Component.literal("Added " + disease + " to " + entityCount + " entities."));

        return 1;
    }

    private static int method2(CommandSourceStack source, Collection<? extends Entity> entities, String disease) throws CommandSyntaxException {

        ServerLevel level = source.getLevel();
        ServerPlayer player = source.getPlayerOrException();

        if (!WCEServerConfig.SERVER.DISEASES.get()) {
            player.sendSystemMessage(Component.literal("Diseases are disabled in this world.")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        DiseaseType<?> type = DiseaseRegistry.getByID(disease);
        if (type == null) return 0;
        int entityCount = 0;

        for (Entity entity : entities) {
            if (!(entity instanceof Diseaseable<?> diseaseable)) continue;

            if (!diseaseable.removeDisease(type)) {
                player.sendSystemMessage(Component.literal("The target doesn't have the disease.")
                        .withStyle(ChatFormatting.RED));
            } else {
                entityCount++;
            }

            diseaseable.onChange();

        }

        player.sendSystemMessage(Component.literal("Removed " + disease + " from " + entityCount + " entities."));

        return 1;
    }

    private static int method3(CommandSourceStack source, Collection<? extends Entity> entities) throws CommandSyntaxException {

        ServerPlayer player = source.getPlayerOrException();

        if (!WCEServerConfig.SERVER.DISEASES.get()) {
            player.sendSystemMessage(Component.literal("Diseases are disabled in this world.")
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        int entityCount = 0;

        for (Entity entity : entities) {
            if (!(entity instanceof Diseaseable<?> diseaseable)) continue;

            diseaseable.getList().clear();
            diseaseable.onChange();

            entityCount++;
        }

        player.sendSystemMessage(Component.literal("Removed all diseases from " + entityCount + " entities."));

        return 1;
    }


}
