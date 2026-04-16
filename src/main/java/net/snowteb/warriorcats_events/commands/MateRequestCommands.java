package net.snowteb.warriorcats_events.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.managers.PlayerMateRequestManager;
import tocraft.walkers.api.PlayerShape;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MateRequestCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("mate")
                                .then(Commands.literal("accept")
                                        .executes(ctx -> method(ctx.getSource()))
                                )
                        )
        );

        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("mate")
                                .then(Commands.literal("decline")
                                        .executes(ctx -> method2(ctx.getSource()))
                                )
                        )
        );

        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("mate")
                                .then(Commands.literal("divorce")
                                        .executes(ctx -> method3(ctx.getSource()))
                                )
                        )
        );
    }

    public static int method(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        PlayerMateRequestManager.MateRequest request = PlayerMateRequestManager.getRequest(player);
        if (request == null) {
            player.sendSystemMessage(
                    Component.literal("No requests pending.")
                            .withStyle(ChatFormatting.GRAY)
            );
            return 0;
        }

        Entity ent = player.serverLevel().getEntity(request.requester);
        if (ent == null) {
            source.sendFailure(Component.literal("Player not found."));
            return 0;
        } else {
            if (ent instanceof ServerPlayer requester && PlayerShape.getCurrentShape(requester) instanceof WCatEntity) {

                String myMorphName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getMorphName).orElse("Unnamed");
                String targetMorphName = requester.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getMorphName).orElse("Unnamed");

                boolean isPlayerChimera = WCGenetics.Chimerism.isChimera(player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getPlayerGenetics).orElse(new WCGenetics()).chimeraGene);

                boolean isRequesterChimera = WCGenetics.Chimerism.isChimera(requester.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getPlayerGenetics).orElse(new WCGenetics()).chimeraGene);

                WCGenetics playerGenetics;
                if (player.getRandom().nextBoolean() && isPlayerChimera) {
                    playerGenetics = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getPlayerChimeraGenetics).orElse(new WCGenetics());
                } else {
                    playerGenetics = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getPlayerGenetics).orElse(new WCGenetics());
                }

                WCGenetics requesterGenetics;
                if (requester.getRandom().nextBoolean() && isRequesterChimera) {
                    requesterGenetics = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getPlayerChimeraGenetics).orElse(new WCGenetics());
                } else {
                    requesterGenetics = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getPlayerGenetics).orElse(new WCGenetics());
                }

                requester.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(data -> {
                    data.setMateUUID(player.getUUID());
                    data.setMateName(Component.literal(myMorphName));
                    data.setMateGenetics(playerGenetics);
                });
                player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(data -> {
                    data.setMateUUID(requester.getUUID());
                    data.setMateName(Component.literal(targetMorphName));
                    data.setMateGenetics(requesterGenetics);
                });


                player.sendSystemMessage(Component.literal(myMorphName + " and " + targetMorphName + " are now a beautiful couple!")
                        .withStyle(ChatFormatting.GREEN));
                requester.sendSystemMessage(Component.literal(targetMorphName + " and " + myMorphName + " are now a beautiful couple!")
                        .withStyle(ChatFormatting.GREEN));


                List<Player> toPerform = new ArrayList<>();
                toPerform.add(requester);
                toPerform.add(player);
                ServerLevel level = player.serverLevel();

                for (Player p : toPerform) {
                    level.playSound(null, p.blockPosition(), SoundEvents.CAT_PURREOW,
                            SoundSource.AMBIENT, 0.7F, 1.0F);
                    level.sendParticles(ParticleTypes.HEART, p.getX(), p.getY(), p.getZ(),
                            3, 0.2f,0.2f,0.2f,0.2f);
                }


                PlayerMateRequestManager.clear(player);
            }
        }

        return 1;
    }

    public static int method2(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        PlayerMateRequestManager.MateRequest request = PlayerMateRequestManager.getRequest(player);
        if (request == null) {
            player.sendSystemMessage(
                    Component.literal("No requests pending.").withStyle(ChatFormatting.GRAY)
            );
            return 0;
        }

        PlayerMateRequestManager.clear(player);


        String deniedPlayerName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphName)
                .orElse(player.getName().getString());


        player.sendSystemMessage(
                Component.empty()
                        .append(Component.literal("You declined the proposal.").withStyle(ChatFormatting.GRAY))
        );


        ServerPlayer requester = player.server.getPlayerList().getPlayer(request.requester);
        if (requester != null) {
            requester.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(deniedPlayerName)
                                    .withStyle(ChatFormatting.GOLD))
                            .append(" declined your proposal.")
            );

            ItemStack stack = new ItemStack(ModItems.MYSTIC_FLOWERS_BOUQUET.get());
            if (!requester.addItem(stack)) {
                requester.drop(stack, false);
            }
        }

        ServerLevel level = player.serverLevel();

        level.sendParticles(ParticleTypes.SMOKE, player.getX(), player.getY(), player.getZ(),
                10, 0.2f,0.2f,0.2f,0.2f);

        return 1;
    }

    public static int method3(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        UUID currentMateUUID = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMateUUID).orElse(ClanData.EMPTY_UUID);

        if (currentMateUUID.equals(ClanData.EMPTY_UUID)) {
            player.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("You don't have a mate.").withStyle(ChatFormatting.GRAY))
            );
            return 0;
        } else {

            player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(data -> {
                data.setMateUUID(ClanData.EMPTY_UUID);
                data.setMateName(Component.literal("None"));

                player.sendSystemMessage(Component.literal("You no longer have a mate."));

            });

            Entity entity = player.serverLevel().getEntity(currentMateUUID);

            if (entity instanceof Player exMate) {
                String myMorphName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                        .map(WCEPlayerData::getMorphName).orElse("Unnamed");

                exMate.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(data -> {
                   data.setMateUUID(ClanData.EMPTY_UUID);
                   data.setMateName(Component.literal("None"));
                });

                exMate.sendSystemMessage(Component.empty()
                        .append(Component.literal(myMorphName).withStyle(ChatFormatting.AQUA))
                                .append(Component.literal(" has divorced from you...").withStyle(ChatFormatting.WHITE)));

            } else if (entity instanceof WCatEntity cat) {
                cat.setMateUUID(ClanData.EMPTY_UUID);
                cat.setMate(Component.literal("None"));
            }

            return 1;

        }
    }

}
