package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.clan.ClanData;

public class RenameClanPacket implements CustomPacketPayload {

    private final String  name;

    public RenameClanPacket(String name) {
        this.name = name;
    }

    public static RenameClanPacket decode(FriendlyByteBuf buf) {

        String name = buf.readUtf();

        return new RenameClanPacket(name);
    }

    public static void encode(RenameClanPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.name);

    }

    public static void handle(RenameClanPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = (ServerPlayer) ctx.player();

            ServerLevel level = player.serverLevel().getServer().overworld();
            ClanData data = ClanData.get(level);



            ClanData.Clan clan = data.getClan(player.getData(ModAttachments.PLAYER_WCE_DATA).getCurrentClanUUID());

            if (clan != null) {
                if (!data.canManage(clan, player.getUUID())) return;


                if (packet.name.isEmpty()) return;
                if (data.getClanByName(packet.name) != null) {
                    player.sendSystemMessage(Component.literal("A clan with this name already exists.").withStyle(ChatFormatting.YELLOW));
                    return;
                }

                String oldName = clan.name;

                boolean success = data.renameClan(clan, packet.name, level);
                if (!success) return;

                player.sendSystemMessage(Component.literal("Clan successfully renamed!").withStyle(ChatFormatting.GRAY));

                String morphName = player.getData(ModAttachments.PLAYER_WCE_DATA).getMorphName();

                Component clanCreatedLog = Component.empty()
                        .append(Component.literal(morphName).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal("(").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                        .append(" has renamed ")
                        .append(Component.literal(oldName).withStyle(Style.EMPTY.withColor(clan.color)))
                        .append(" to ")
                        .append(Component.literal(packet.name).withStyle(Style.EMPTY.withColor(clan.color)))
                        .append(".");

                data.registerLog(level, clan.clanUUID, clanCreatedLog);

                data.setDirty();

            } else {
                player.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY));
            }


        });
    }


    public static final Type<RenameClanPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "rename_clan"));

    public static final StreamCodec<FriendlyByteBuf, RenameClanPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

