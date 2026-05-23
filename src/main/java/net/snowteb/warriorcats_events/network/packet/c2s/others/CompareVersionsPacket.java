package net.snowteb.warriorcats_events.network.packet.c2s.others;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

import java.util.Objects;

public class CompareVersionsPacket implements CustomPacketPayload {
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

    public static void handle(CompareVersionsPacket msg, IPayloadContext ctx) {

        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();

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
    }

    public static final Type<CompareVersionsPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "compare_versions"));

    public static final StreamCodec<FriendlyByteBuf, CompareVersionsPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
