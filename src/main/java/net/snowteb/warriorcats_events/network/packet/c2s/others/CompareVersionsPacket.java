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
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

import java.util.Objects;
import java.util.function.Supplier;

public class CompareVersionsPacket {
    private final String clientVersion;

    public CompareVersionsPacket(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public static void encode(CompareVersionsPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.clientVersion);
    }

    public static CompareVersionsPacket decode(FriendlyByteBuf buf) {
        String clientVersion = buf.readUtf();
        return new CompareVersionsPacket(clientVersion);
    }

    public static void handle(CompareVersionsPacket msg, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            String serverVersion = WarriorCatsEvents.MOD_VERSION;

            if (!Objects.equals(msg.clientVersion, serverVersion)) {

                player.sendSystemMessage(
                        Component.empty()
                                .append((Component.literal("[!] ").withStyle(ChatFormatting.RED)))
                                .append(Component.literal("[WCE] ").withStyle(ChatFormatting.GOLD))
                                .append(Component.literal("Server and client versions don't match.").withStyle(ChatFormatting.DARK_GRAY))
                );
                player.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal("Unmatching versions can cause client-side crashes unless explicitly specified by the dev.").withStyle(ChatFormatting.DARK_GRAY))
                );

                Component link = Component.literal("[!]").withStyle(style -> style.withColor(ChatFormatting.RED)
                        .withUnderlined(false).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                                "https://www.curseforge.com/minecraft/mc-mods/warriorcats-events/files/all"
                        )).withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Component.literal("Versions")
                                        .withStyle(ChatFormatting.YELLOW)
                        )));

                player.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal("Your version: " + msg.clientVersion).withStyle(ChatFormatting.GRAY))
                                .append(" ")
                                .append(link)
                );

                player.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal("Server version: " + serverVersion).withStyle(ChatFormatting.GRAY))
                );

            }

        });
        ctx.get().setPacketHandled(true);
    }

}
