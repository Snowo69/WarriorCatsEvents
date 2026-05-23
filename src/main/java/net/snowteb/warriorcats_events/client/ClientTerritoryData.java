package net.snowteb.warriorcats_events.client;

import net.minecraft.world.level.ChunkPos;

import java.util.*;

public class ClientTerritoryData {

    public static final Map<UUID, ClientClanTerritories> CLIENT_TERRITORIES = new HashMap<>();

    public static void clear() {
        CLIENT_TERRITORIES.clear();
    }

    public record ClientClanTerritories(List<ClientChunk> claimedTerritory, String clanName, int color, ChunkPos core) {}

    public static class ClientChunk {
        public final ChunkPos chunkPos;
        public final String name;
        public int time;

        public ClientChunk(ChunkPos chunkPos, String name, int time) {
            this.chunkPos = chunkPos;
            this.name = name;
            this.time = time;
        }
    }

    public static void clientTerritoryTick() {
        for (ClientClanTerritories territory : CLIENT_TERRITORIES.values()) {
            for (ClientChunk chunk : territory.claimedTerritory()) {

                boolean corePresent = false;

                if (chunk.chunkPos.equals(territory.core) || isAdjacentToCore(chunk.chunkPos, territory.core)) corePresent = true;

                if (chunk.time > 0 && !corePresent) chunk.time--;
            }
        }
    }

    public static final ChunkPos[] SQUARE_ADJACENT_CHUNKS = new ChunkPos[]{
            new ChunkPos(-1, 1), new ChunkPos(0, 1), new ChunkPos(1, 1),
            new ChunkPos(-1, 0),                              new ChunkPos(1, 0),
            new ChunkPos(-1, -1), new ChunkPos(0, -1), new ChunkPos(1, -1)
    };
    public static boolean isAdjacentToCore(ChunkPos pos, ChunkPos core) {
        if (core == null) return false;
        if (pos == null) return false;

        for (ChunkPos chunk : SQUARE_ADJACENT_CHUNKS) {
            ChunkPos checking = new ChunkPos(pos.x + chunk.x, pos.z + chunk.z);
            if (checking.equals(core)) {
                return true;
            }
        }
        return false;
    }

}
