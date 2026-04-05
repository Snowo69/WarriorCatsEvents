package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.client.ClientTerritoryData;

import java.util.*;
import java.util.function.Supplier;

public class SyncTerritoryToClients {

    private final Map<UUID, ClanData.ClanTerritories> data;

    public SyncTerritoryToClients(Map<UUID, ClanData.ClanTerritories> data) {
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(data.size());

        for (var entry : data.entrySet()) {
            buf.writeUUID(entry.getKey());

            ClanData.ClanTerritories clan = entry.getValue();

            buf.writeUtf(clan.clanName());
            buf.writeInt(clan.color());

            List<ClanData.TerritoryChunk> list = clan.claimedTerritory();
            buf.writeInt(list.size());

            for (ClanData.TerritoryChunk chunk : list) {
                ChunkPos pos = chunk.chunkPos;

                buf.writeInt(pos.x);
                buf.writeInt(pos.z);
                buf.writeUtf(chunk.name);
                buf.writeInt(chunk.time);
                buf.writeInt(chunk.timeToReclaim);
            }

            ChunkPos core = clan.core();

            buf.writeBoolean(core != null);

            if (core != null) {
                buf.writeInt(core.x);
                buf.writeInt(core.z);
            }

        }
    }

    public static SyncTerritoryToClients decode(FriendlyByteBuf buf) {
        Map<UUID, ClanData.ClanTerritories> map = new HashMap<>();

        int clansSize = buf.readInt();

        for (int i = 0; i < clansSize; i++) {
            UUID clanUUID = buf.readUUID();

            String name = buf.readUtf();
            int color = buf.readInt();

            int chunkCount = buf.readInt();
            List<ClanData.TerritoryChunk> chunks = new ArrayList<>();

            for (int j = 0; j < chunkCount; j++) {
                int x = buf.readInt();
                int z = buf.readInt();
                String chunkName = buf.readUtf();
                int time = buf.readInt();
                int timeToReclaim = buf.readInt();

                ChunkPos pos = new ChunkPos(x, z);
                chunks.add(new ClanData.TerritoryChunk(pos, chunkName, time, timeToReclaim));
            }

            ChunkPos core = null;
            boolean hasCore = buf.readBoolean();

            if (hasCore) {
                int coreX = buf.readInt();
                int coreZ = buf.readInt();
                core = new ChunkPos(coreX, coreZ);
            }

            map.put(clanUUID, new ClanData.ClanTerritories(chunks, name, color, core));
        }

        return new SyncTerritoryToClients(map);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ClientTerritoryData.clear();

            for (var entry : data.entrySet()) {
                UUID clanUUID = entry.getKey();

                List<ClientTerritoryData.ClientChunk> territoryList = new ArrayList<>();

                data.get(clanUUID).claimedTerritory().forEach(chunk -> {
                    ClientTerritoryData.ClientChunk territory = new ClientTerritoryData.ClientChunk(chunk.chunkPos, chunk.name, chunk.time);
                    territoryList.add(territory);
                });

                ClanData.ClanTerritories current = data.get(clanUUID);

                ClientTerritoryData.ClientClanTerritories rebuiltClanTerritory = new ClientTerritoryData.ClientClanTerritories(territoryList, current.clanName(), current.color(), current.core());


                ClientTerritoryData.CLIENT_TERRITORIES.put(clanUUID, rebuiltClanTerritory);
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
