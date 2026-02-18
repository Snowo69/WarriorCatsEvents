package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClanInfo;
import net.snowteb.warriorcats_events.client.ClientClanCache;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;
import net.snowteb.warriorcats_events.screen.clandata.ClanListScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class S2CClanListPacket {

    public List<ClanInfo> clans;

    public S2CClanListPacket(List<ClanInfo> clans) {
        this.clans = clans;
    }

    public static void encode(S2CClanListPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.clans.size());
        for (ClanInfo info : msg.clans) {
            buf.writeUUID(info.uuid);
            buf.writeUtf(info.name);
            buf.writeInt(info.color);
            buf.writeUtf(info.leaderName);
            buf.writeUtf(info.clanSentence);
            buf.writeBoolean(info.canManage);
            buf.writeInt(info.memberCount);

            buf.writeInt(info.memberMorphNames.size());
            for (String morph : info.memberMorphNames) {
                buf.writeUtf(morph);
            }

            buf.writeInt(info.clanCats.size());
            for (ClanInfo.ClientClanCat cat : info.clanCats) {
                buf.writeUUID(cat.uuid);
                buf.writeUtf(cat.name);
                buf.writeUtf(cat.gender);
                buf.writeUtf(cat.rank);
                buf.writeUtf(cat.age);
                buf.writeInt(cat.variant);
                buf.writeUtf(cat.parents);
            }

            buf.writeInt(info.clanLogs.size());
            for (ClanInfo.ClientLogEntry log : info.clanLogs) {
                buf.writeLong(log.gameTimeID);
                buf.writeComponent(log.message);
            }

            buf.writeInt(info.symbolIndex);

        }
    }


    public static S2CClanListPacket decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<ClanInfo> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            UUID uuid = buf.readUUID();
            String name = buf.readUtf();
            int color = buf.readInt();
            String leaderName = buf.readUtf();
            String clanSentence = buf.readUtf();
            boolean canManage = buf.readBoolean();
            int memberCount = buf.readInt();

            int morphSize = buf.readInt();
            List<String> morphNames = new ArrayList<>();
            for (int j = 0; j < morphSize; j++) {
                morphNames.add(buf.readUtf());
            }

            int catSize = buf.readInt();
            List<ClanInfo.ClientClanCat> cats = new ArrayList<>();

            for (int j = 0; j < catSize; j++) {
                UUID catUUID = buf.readUUID();
                String catName = buf.readUtf();
                String gender = buf.readUtf();
                String rank = buf.readUtf();
                String age = buf.readUtf();
                int variant = buf.readInt();
                String parents = buf.readUtf();

                cats.add(new ClanInfo.ClientClanCat(catUUID, catName, gender, rank, age, variant, parents));
            }

            int logSize = buf.readInt();
            List<ClanInfo.ClientLogEntry> logs = new ArrayList<>();

            for (int j = 0; j < logSize; j++) {
                long time = buf.readLong();
                Component message = buf.readComponent();
                logs.add(new ClanInfo.ClientLogEntry(time, message));
            }

            int symbolIndex = buf.readInt();

            list.add(new ClanInfo(uuid, name, color, leaderName, clanSentence, canManage, memberCount, morphNames, cats, logs, symbolIndex));

        }

        return new S2CClanListPacket(list);
    }


    public static void handle(S2CClanListPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientClanCache.setClans(msg.clans);
            ClientPacketHandles.openClanListScreen();
        });
        ctx.get().setPacketHandled(true);
    }

}

