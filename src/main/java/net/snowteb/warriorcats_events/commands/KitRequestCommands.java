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
import net.snowteb.warriorcats_events.managers.PlayerKittingRequestManager;
import net.snowteb.warriorcats_events.managers.PlayerMateRequestManager;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;
import tocraft.walkers.api.PlayerShape;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KitRequestCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("mate")
                                .then(Commands.literal("kits")
                                        .then(Commands.literal("accept")
                                                .executes(ctx -> method(ctx.getSource()))
                                        )
                                )
                        )
        );

        dispatcher.register(
                Commands.literal("wce")
                        .then(Commands.literal("mate")
                                .then(Commands.literal("kits")
                                        .then(Commands.literal("decline")
                                                .executes(ctx -> method2(ctx.getSource()))
                                        )
                                )
                        )
        );

    }

    public static int method(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        PlayerKittingRequestManager.KitRequest request = PlayerKittingRequestManager.getRequest(player);
        if (request == null) {
            player.sendSystemMessage(
                    Component.literal("No requests pending.")
                            .withStyle(ChatFormatting.GRAY)
            );
            return 0;
        }

        ServerLevel level = player.serverLevel();

        Entity ent = level.getEntity(request.requester);
        if (ent == null) {
            source.sendFailure(Component.literal("Player not found."));
            return 0;
        } else {
            if (ent instanceof ServerPlayer requester && PlayerShape.getCurrentShape(requester) instanceof WCatEntity) {

                int kittingCD = (int) ((WCEServerConfig.SERVER.KIT_GROWTH_MINUTES.get() * 20 * 60) * 0.75f);

                List<Player> toPerform = new ArrayList<>();
                toPerform.add(requester);
                toPerform.add(player);

                for (Player p : toPerform) {
                    ItemStack kitStack = new ItemStack(ModItems.KIT_ITEM.get(), 1 + p.getRandom().nextInt(2));

                    level.playSound(null, p.blockPosition(), SoundEvents.CAT_PURREOW,
                            SoundSource.AMBIENT, 0.7F, 1.0F);
                    level.sendParticles(ParticleTypes.HEART, p.getX(), p.getY(), p.getZ(),
                            3, 0.2f,0.2f,0.2f,0.2f);

                    String myMorphName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getMorphName).orElse("Unnamed");
                    String myMate = (player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                            .map(WCEPlayerData::getMateName).orElse(Component.literal("Unnamed"))).getString();

                    if (!p.addItem(kitStack)) {
                        p.drop(kitStack, false);
                    }

                    p.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA).ifPresent(cap ->{
                        cap.setPlayerKitsCooldown(kittingCD);
                    });

                    p.sendSystemMessage(
                            Component.empty()
                                    .append(Component.literal(myMorphName).withStyle(ChatFormatting.AQUA))
                                    .append(Component.literal(" and ").withStyle(ChatFormatting.WHITE))
                                    .append(Component.literal(myMate).withStyle(ChatFormatting.AQUA))
                                    .append(Component.literal(" have brought kits to the world!").withStyle(ChatFormatting.WHITE))
                    );

                }

                PlayerKittingRequestManager.clear(player);
            }
        }

        return 1;
    }

    public static int method2(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        PlayerKittingRequestManager.KitRequest request = PlayerKittingRequestManager.getRequest(player);
        if (request == null) {
            player.sendSystemMessage(
                    Component.literal("No requests pending.").withStyle(ChatFormatting.GRAY)
            );
            return 0;
        }

        PlayerKittingRequestManager.clear(player);


        String deniedPlayerName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                .map(WCEPlayerData::getMorphName).orElse(player.getName().getString());


        player.sendSystemMessage(
                Component.empty()
                        .append(Component.literal("You declined the request.").withStyle(ChatFormatting.GRAY))
        );


        ServerPlayer requester = player.server.getPlayerList().getPlayer(request.requester);
        if (requester != null) {
            requester.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(deniedPlayerName)
                                    .withStyle(ChatFormatting.GOLD))
                            .append(" declined your request.")
            );
        }

        return 1;
    }


}
