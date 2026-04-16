package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.clan.WCEPlayerDataProvider;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.managers.PlayerKittingRequestManager;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerKitPacket {

    private final UUID targetUUID;

    public PlayerKitPacket(UUID targetUUID) {
        this.targetUUID = targetUUID;
    }

    public static void encode(PlayerKitPacket msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.targetUUID);
    }

    public static PlayerKitPacket decode(FriendlyByteBuf buf) {
        return new PlayerKitPacket(
                buf.readUUID()
        );
    }

    public static void handle(PlayerKitPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            Entity entity = level.getEntity(msg.targetUUID);

            if (!(entity instanceof ServerPlayer targetPlayer)) return;

            String myMorphName = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                    .map(WCEPlayerData::getMorphName).orElse("Unnamed");
            String targetMorphName = targetPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                    .map(WCEPlayerData::getMorphName).orElse("Unnamed");

            WCEPlayerData.Age myAge = player.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                    .map(WCEPlayerData::getMorphAge).orElse(WCEPlayerData.Age.ADULT);
            WCEPlayerData.Age targetAge = targetPlayer.getCapability(WCEPlayerDataProvider.PLAYER_CLAN_DATA)
                    .map(WCEPlayerData::getMorphAge).orElse(WCEPlayerData.Age.ADULT);

            if (myAge != WCEPlayerData.Age.ADULT) {
                player.sendSystemMessage(Component.literal(myMorphName + " is not old enough for this.")
                        .withStyle(ChatFormatting.RED));
                return;
            } else if (targetAge != WCEPlayerData.Age.ADULT) {
                player.sendSystemMessage(Component.literal(targetMorphName + " is not old enough for this.")
                        .withStyle(ChatFormatting.RED));
                return;
            }

            PlayerKittingRequestManager.request(targetPlayer, player);

            player.sendSystemMessage(
                    Component.empty()
                            .append("Request to create life sent to ")
                            .append(Component.literal(targetMorphName).withStyle(ChatFormatting.AQUA)
                                    .append(Component.literal( "(" + targetPlayer.getName().getString() + ")").withStyle(ChatFormatting.GRAY))
                            ));

            targetPlayer.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(myMorphName).withStyle(ChatFormatting.AQUA)
                                    .append(Component.literal( "(" + player.getName().getString() + ")").withStyle(ChatFormatting.GRAY))
                                    .append(" wants to bring kits to life with you.")
                            ));

            targetPlayer.sendSystemMessage(
                    Component.empty()
                            .append(
                                    Component.literal("[ACCEPT]")
                                            .withStyle(style -> style
                                                    .withColor(ChatFormatting.GREEN)
                                                    .withItalic(true)
                                                    .withUnderlined(true)
                                                    .withClickEvent(
                                                            new ClickEvent(
                                                                    ClickEvent.Action.RUN_COMMAND,
                                                                    "/wce mate kits accept"
                                                            )
                                                    )
                                                    .withHoverEvent(
                                                            new HoverEvent(
                                                                    HoverEvent.Action.SHOW_TEXT,
                                                                    Component.literal("Accept")
                                                                            .withStyle(ChatFormatting.GREEN)
                                                            )
                                                    )
                                            )
                            )

                            .append("       ")

                            .append(
                                    Component.literal("[DECLINE]")
                                            .withStyle(style -> style
                                                    .withColor(ChatFormatting.RED)
                                                    .withItalic(true)
                                                    .withUnderlined(true)
                                                    .withClickEvent(
                                                            new ClickEvent(
                                                                    ClickEvent.Action.RUN_COMMAND,
                                                                    "/wce mate kits decline"
                                                            )
                                                    )
                                                    .withHoverEvent(
                                                            new HoverEvent(
                                                                    HoverEvent.Action.SHOW_TEXT,
                                                                    Component.literal("Decline")
                                                                            .withStyle(ChatFormatting.RED)
                                                            )
                                                    )
                                            )
                            )
            );

        });

        ctx.get().setPacketHandled(true);
    }

}

