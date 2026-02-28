package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.commands.ClanListCommand;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.OpenSpecificClanScreen;

import java.util.*;
import java.util.function.Supplier;

public class CtSRegisterLogPacket {

    private final String message;
    private final UUID clanuuid;

    public CtSRegisterLogPacket(String message, UUID clanuuid) {
        this.message = message;
        this.clanuuid = clanuuid;
    }

    public static CtSRegisterLogPacket decode(FriendlyByteBuf buf) {
        String message = buf.readUtf(140);
        UUID  clanuuid = buf.readUUID();
        return new CtSRegisterLogPacket(message, clanuuid);
    }

    public static void encode(CtSRegisterLogPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.message);
        buf.writeUUID(packet.clanuuid);
    }

    public static void handle(CtSRegisterLogPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            ClanData data = ClanData.get(level);

            ClanData.Clan clan = data.getClan(packet.clanuuid);

            if (clan != null) {
                if (!data.canManage(clan, player.getUUID())) return;

                Component finalMessage = Component.empty()
                                .append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY))
                                .append(Component.literal(player.getDisplayName().getString()).withStyle(ChatFormatting.DARK_GRAY))
                                .append(Component.literal("] ").withStyle(ChatFormatting.DARK_GRAY))
                        .append(parseColoredText(packet.message, data).copy());

                data.registerLog(player.serverLevel(), clan.clanUUID, finalMessage);

                player.displayClientMessage(Component.literal("Log successfully registered").withStyle(ChatFormatting.GREEN), false);


                ClanListCommand.getList(player);

                ModPackets.sendToPlayer(new OpenSpecificClanScreen(clan.name, clan.clanUUID), player);

            } else {
                player.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY));
            }

        });

        ctx.get().setPacketHandled(true);
    }

    public static Component parseColoredText(String text, ClanData data) {

        MutableComponent result = Component.empty();

        String[] words = text.split("\\s+");

        Collection<String> morphNames = data.playerMorphNames.values();
        Set<String> clanCatNames = new HashSet<>();

        for (ClanData.Clan clan : data.getAllClans()) {
            for (UUID uuid : clan.clanCats.keySet()) {
                clanCatNames.add(clan.clanCats.get(uuid).catName
                                .getString()
                        .replace(" ♂", "")
                        .replace(" ♀", "")
                        .replace(" ", "")
                );

            }
        }

        Set<String> clanNameSet = new HashSet<>();
        for (ClanData.Clan clan : data.clans.values()) {
            clanNameSet.add(clan.name);
        }

        for (int i = 0; i < words.length; i++) {

            String rawWord = words[i];

            String cleanWord = rawWord.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");

            if (cleanWord.endsWith("'s")) {
                cleanWord = cleanWord.substring(0, cleanWord.length() - 2);
            }

            ChatFormatting color = ChatFormatting.WHITE;

            if (clanNameSet.contains(cleanWord)) {
                color = ChatFormatting.GOLD;
            } else if (morphNames.contains(cleanWord)) {
                color = ChatFormatting.AQUA;
            }
            else if (clanCatNames.contains(cleanWord)) {
                color = ChatFormatting.GREEN;
            }

            MutableComponent part = Component.literal(rawWord).withStyle(color);
            result.append(part);

            if (i < words.length - 1) {
                result.append(Component.literal(" "));
            }
        }

        return result;
    }



}

