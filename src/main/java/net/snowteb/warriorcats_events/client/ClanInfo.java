package net.snowteb.warriorcats_events.client;

import net.minecraft.network.chat.Component;

import java.util.*;

public class ClanInfo {

    public UUID uuid;
    public String name;
    public int color;
    public String leaderName;
    public String clanSentence;
    public int symbolIndex;
    public int memberCount;
    public List<String> memberMorphNames;

    public Map<UUID, Member> playersInClan = new HashMap<>();
    public List<ClientClanCat> clanCats = new ArrayList<>();

    public List<ClientLogEntry> clanLogs = new ArrayList<>();
    public boolean canManage = false;



    public ClanInfo(UUID uuid, String name, int color, String leader, String clanSentence, boolean canManage,
                    int memberCount, List<String> memberMorphNames, List<ClientClanCat> clanCatsNPCSres,
                    List<ClientLogEntry> clanLogs, int symbolIndex) {
        this.uuid = uuid;
        this.name = name;
        this.color = color;
        this.leaderName = leader;
        this.memberCount = memberCount;
        this.memberMorphNames = memberMorphNames;
        this.clanCats = clanCatsNPCSres;
        this.clanLogs = clanLogs;
        this.canManage = canManage;
        this.clanSentence = clanSentence;
        this.symbolIndex = symbolIndex;
    }

    public ClanInfo(UUID uuid, String name, int color, String leader, String clanSentence, int memberCount, Map<UUID, Member> playersInClan, int symbolIndex) {
        this.uuid = uuid;
        this.name = name;
        this.color = color;
        this.leaderName = leader;
        this.memberCount = memberCount;
        this.memberMorphNames = new ArrayList<>();
        this.playersInClan = playersInClan;
        this.clanSentence = clanSentence;
        this.symbolIndex = symbolIndex;
    }

    public Member getMember(UUID memberUUID) {
        return playersInClan.get(memberUUID);
    }

    public ClientClanCat getClanCat(UUID clanCatUUID) {
        for (ClientClanCat cat : clanCats) {
            if (cat.uuid.equals(clanCatUUID)) {
                return cat;
            }
        }
        return null;
    }


    public static class Member {

        private final UUID playerUUID;
        private final String morphName;
        private final String rank;
        private final String permissions;
        private final String age;
        private final boolean isOnline;
        private final int variantData;

        public Member(UUID playerUUID, String morphName, String rank, String permissions, String age, boolean isOnline, int variantData) {
            this.playerUUID = playerUUID;
            this.morphName = morphName;
            this.rank = rank;
            this.permissions = permissions;
            this.age = age;
            this.isOnline = isOnline;
            this.variantData = variantData;
        }

        public UUID getPlayerUUID() { return playerUUID; }
        public String getPlayerMorphName() { return morphName; }
        public String getRank() { return rank; }
        public String getPerms() { return permissions; }
        public String getPlayerMorphAge() { return age; }
        public boolean isPlayerOnline() { return isOnline; }
        public int getVariantData() { return variantData; }
    }


    public static class ClientClanCat {

        public UUID uuid;
        public String name;
        public String gender;
        public String rank;
        public String age;
        public String parents;
        public int variant;

        public ClientClanCat(UUID uuid, String name, String gender, String rank, String age, int variant, String parents) {
            this.uuid = uuid;
            this.name = name;
            this.gender = gender;
            this.variant = variant;
            this.parents = parents;
            this.rank = rank;
            this.age = age;
        }
    }


    public static class ClientLogEntry {
        public long gameTimeID;
        public Component message;

        public ClientLogEntry(long gameTimeID, Component message) {
            this.gameTimeID = gameTimeID;
            this.message = message;
        }
    }

}
