package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClanInfo;
import net.snowteb.warriorcats_events.client.ClientClanCache;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.*;
import java.util.function.Supplier;

public class S2CManageClanPacket {

    private final ClanInfo clan;

    public S2CManageClanPacket(ClanInfo clan) {
        this.clan = clan;
    }

    public static void encode(S2CManageClanPacket msg, FriendlyByteBuf buf) {
        ClanInfo info = msg.clan;

        buf.writeUUID(info.uuid);
        buf.writeUtf(info.name);
        buf.writeInt(info.color);
        buf.writeUtf(info.leaderName);
        buf.writeUtf(info.clanSentence);
        buf.writeInt(info.memberCount);

        buf.writeInt(info.playersInClan.size());

        for (ClanInfo.Member member : info.playersInClan.values()) {
            buf.writeUUID(member.getPlayerUUID());
            buf.writeUtf(member.getPlayerMorphName());
            buf.writeUtf(member.getRank());
            buf.writeUtf(member.getPerms());
            buf.writeUtf(member.getPlayerMorphAge());
            buf.writeBoolean(member.isPlayerOnline());
            buf.writeInt(member.getVariantData());
        }

        buf.writeInt(info.symbolIndex);

    }


    public static S2CManageClanPacket decode(FriendlyByteBuf buf) {
        UUID uuid = buf.readUUID();
        String name = buf.readUtf();
        int color = buf.readInt();
        String leaderName = buf.readUtf();
        String clanSentence = buf.readUtf();
        int memberCount = buf.readInt();

        int mapSize = buf.readInt();
        Map<UUID, ClanInfo.Member> playersInClan = new HashMap<>();
        for (int i = 0; i < mapSize; i++) {
            UUID memberUUID = buf.readUUID();
            String morphName = buf.readUtf();
            String rank = buf.readUtf();
            String perms = buf.readUtf();
            String age = buf.readUtf();
            boolean isOnline = buf.readBoolean();
            int variantData = buf.readInt();

            ClanInfo.Member member = new ClanInfo.Member(
                    memberUUID,
                    morphName,
                    rank,
                    perms,
                    age,
                    isOnline,
                    variantData
            );
            playersInClan.put(memberUUID, member);
        }

        int symbolIndex = buf.readInt();

        ClanInfo info = new ClanInfo(
                uuid,
                name,
                color,
                leaderName,
                clanSentence,
                memberCount,
                playersInClan,
                symbolIndex
        );

        return new S2CManageClanPacket(info);
    }


    public static void handle(S2CManageClanPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPacketHandles.openManageClanScreen(msg.clan);
        });
        ctx.get().setPacketHandled(true);
    }

}

