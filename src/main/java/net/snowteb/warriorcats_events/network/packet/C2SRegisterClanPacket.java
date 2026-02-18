package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SRegisterClanPacket {

    private final int color;
    private final String  name;
    private final String  clanSentence;
    private final int clanSymbolIndex;

    public C2SRegisterClanPacket(int color, String name, String clanSentence, int clanSymbolIndex) {
        this.color = color;
        this.name = name;
        this.clanSentence = clanSentence;
        this.clanSymbolIndex = clanSymbolIndex;
    }

    public static C2SRegisterClanPacket decode(FriendlyByteBuf buf) {
        int color = buf.readInt();
        String name = buf.readUtf();
        String clanSentence = buf.readUtf();
        int clanSymbolIndex = buf.readInt();
        return new C2SRegisterClanPacket(color, name,  clanSentence,  clanSymbolIndex);
    }

    public static void encode(C2SRegisterClanPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.color);
        buf.writeUtf(packet.name);
        buf.writeUtf(packet.clanSentence);
        buf.writeInt(packet.clanSymbolIndex);
    }

    public static void handle(C2SRegisterClanPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            ClanData data = ClanData.get(level);

            if (packet.name.isEmpty()) return;
            if (data.getClanByName(packet.name) != null) {
                player.sendSystemMessage(Component.literal("A clan with this name already exists.").withStyle(ChatFormatting.YELLOW));
                return;
            }

            UUID leaderUUID = player.getUUID();
            String leaderName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getMorphName).orElse(player.getName().getString());

            String finalClanSentence = "";
            if (!packet.clanSentence.isEmpty()) {
                finalClanSentence = "\"" + packet.clanSentence + "\"";
            }

            ClanData.Clan clan = data.createClan(packet.name, packet.color, leaderUUID, leaderName, finalClanSentence, packet.clanSymbolIndex);

            player.sendSystemMessage(Component.literal("Clan successfully created!").withStyle(ChatFormatting.GRAY));

            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
                cap.setCurrentClanUUID(clan.clanUUID);
                cap.setClanName(clan.name);

                ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
            });

            Component clanCreatedLog = Component.empty()
                            .append(Component.literal(leaderName).withStyle(ChatFormatting.AQUA))
                    .append(Component.literal("(").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                    .append(" has founded ")
                            .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)))
                            .append("!");

            data.registerLog(level, clan.clanUUID, clanCreatedLog);

            level.getAllEntities().forEach(entity -> {
                if (entity instanceof WCatEntity cat) {
                    if (cat.isOwnedBy(player)) {
                        cat.setClanUUID(clan.clanUUID);
                        data.addClanCat(clan, cat);

                        Component catJoinedClanLog = Component.empty()
                                .append(cat.hasCustomName() ? cat.getCustomName().copy() : Component.literal("A Cat"))
                                .append(" has joined ")
                                .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)))
                                .append("!");

                        if (cat.level() instanceof ServerLevel serverLevel) {
                            data.registerLog(serverLevel, clan.clanUUID, catJoinedClanLog);
                        }
                    }
                }
            });

            data.setDirty();
        });

        ctx.get().setPacketHandled(true);
    }
}

