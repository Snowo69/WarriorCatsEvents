package net.snowteb.warriorcats_events.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.saveddata.SavedData;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.S2CSyncClanDataPacket;

import javax.annotation.Nullable;
import java.util.*;

public class ClanData extends SavedData {
    public Map<UUID, Clan> clans = new HashMap<>();
    public Map<UUID, String> playerMorphNames = new HashMap<>();
    public Map<UUID, Integer> playerMorphData = new HashMap<>();


    public static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    private static final int DATA_VERSION = 1;

    public enum ClanPlayerRank {
        LEADER,
        DEPUTY,
        MEDICINE,
        MEDICINEAPP,
        WARRIOR,
        ELDER,
        QUEEN,
        APPRENTICE,
        KIT
    }

    public enum ClanPermissions {
        OWNER,
        ADMIN,
        MEMBER,
        GUEST
    }


    public static class ClanLogEntry {
        public long gameTimeID;
        public Component message;

        public ClanLogEntry(long gameTimeID, Component message) {
            this.gameTimeID = gameTimeID;
            this.message = message;
        }

        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putLong("Time", gameTimeID);
            tag.putString("Message", Component.Serializer.toJson(message));
            return tag;
        }

        public static ClanLogEntry load(CompoundTag tag) {
            return new ClanLogEntry(
                    tag.getLong("Time"),
                    Component.Serializer.fromJson(tag.getString("Message"))
            );
        }
    }


    public static class ClanCat {
        public UUID catUUID;
        public String catGender;
        public Component catName;
        public int catVariant;
        public Component catAge;
        public String catRank;
        public Component catParents;


        public static ClanCat buildCat(WCatEntity cat) {
            ClanCat clanCat = new ClanCat();

            clanCat.catUUID = cat.getUUID();
            clanCat.catGender = cat.getGender() == 0 ? "Tom-cat" : "She-cat";
            clanCat.catName = cat.hasCustomName() ? cat.getCustomName() : Component.literal("Unnamed");
            clanCat.catVariant = cat.getVariant();
            clanCat.catAge = cat.getAge() < 0 ?
                    Component.literal(String.format("%.2f moons", cat.getAgeInMoons()))
                    : Component.literal("Fully grown");
            clanCat.catRank = cat.getRank().name();

            Component parentsText;
            Component catMother = cat.getMother();
            Component catFather = cat.getFather();

            if (catMother.getString().equals("None") && catFather.getString().equals("None")) {
                parentsText = Component.literal("Parents: Parents unknown");
            } else {
                if (catMother.getString().equals("None") && !catFather.getString().equals("None")) {
                    parentsText = Component.empty()
                            .append(Component.literal("Parents: ").withStyle(ChatFormatting.WHITE))
                            .append(catFather.copy().withStyle(ChatFormatting.AQUA));
                } else if (!catMother.getString().equals("None") && catFather.getString().equals("None")) {
                    parentsText = Component.empty()
                            .append(Component.literal("Parents: ").withStyle(ChatFormatting.WHITE))
                            .append(catMother.copy().withStyle(ChatFormatting.AQUA));
                } else {
                    parentsText = Component.empty()
                            .append(Component.literal("Parents: ").withStyle(ChatFormatting.WHITE))
                            .append(catMother.copy().withStyle(ChatFormatting.AQUA))
                            .append(Component.literal(" & ").withStyle(ChatFormatting.WHITE))
                            .append(catFather.copy().withStyle(ChatFormatting.AQUA));
                }
            }

            clanCat.catParents = parentsText;

            return clanCat;
        }

        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();

            tag.putUUID("UUID", this.catUUID);
            tag.putString("Gender", this.catGender);
            tag.putString("Name", Component.Serializer.toJson(this.catName));
            tag.putInt("Variant", this.catVariant);
            tag.putString("Age", Component.Serializer.toJson(this.catAge));
            tag.putString("Rank", this.catRank);
            tag.putString("Parents", Component.Serializer.toJson(this.catParents));

            return tag;
        }

        public static ClanCat load(CompoundTag tag) {
            ClanCat cat = new ClanCat();

            cat.catUUID = tag.getUUID("UUID");
            cat.catGender = tag.getString("Gender");
            cat.catName = Component.Serializer.fromJson(tag.getString("Name"));
            cat.catVariant = tag.getInt("Variant");
            cat.catAge = Component.Serializer.fromJson(tag.getString("Age"));
            cat.catRank = tag.getString("Rank");
            cat.catParents = Component.Serializer.fromJson(tag.getString("Parents"));

            return cat;
        }

    }

    public static class Clan {
        public UUID clanUUID;
        public String name;
        public String clanBioSentence;
        public int clanSymbolIndex;
        public int color;
        UUID leader;
        public String leaderName;
        String normalizedName;
        public Map<UUID, ClanPlayerRank> members = new HashMap<>();
        public Map<UUID, ClanPermissions> memberPerms = new HashMap<>();
        public Map<UUID, ClanCat> clanCats = new HashMap<>();
        public List<ClanLogEntry> logs = new ArrayList<>();
    }


    public Clan createClan(String name, int color, UUID leader, String leaderName, String clanBioSentence, int clanSymbolIndex) {
        Clan clan = new Clan();
        clan.clanUUID = UUID.randomUUID();
        clan.name = name;
        clan.clanBioSentence = clanBioSentence;
        clan.clanSymbolIndex = clanSymbolIndex;
        clan.color = color;
        clan.leader = leader;
        clan.leaderName = leaderName;

        String baseName = normalizeName(name);
        String currentName = baseName;
        int i = 1;

        boolean exists;
        do {
            exists = false;
            for (Clan presentClan : clans.values()) {
                if (presentClan.normalizedName.equals(currentName)) {
                    exists = true;
                    currentName = baseName + i;
                    i++;
                    break;
                }
            }
        } while (exists);

        clan.normalizedName = currentName;

        clan.members.put(leader, ClanPlayerRank.LEADER);
        clan.memberPerms.put(leader, ClanPermissions.OWNER);

        clans.put(clan.clanUUID, clan);

        setDirty();
        return clan;
    }

    public boolean addClanCat(UUID clanUUID, WCatEntity cat) {
        Clan clan = getClan(clanUUID);
        if (clan == null) return false;
        if (cat == null) return false;

//        Component catJoinedClanLog = Component.empty()
//                .append(cat.hasCustomName() ? cat.getCustomName().copy() : Component.literal("A Cat"))
//                .append(" has joined ")
//                .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)))
//                .append("!");
//
//        if (cat.level() instanceof ServerLevel serverLevel) {
//            this.registerLog(serverLevel, clan.clanUUID, catJoinedClanLog);
//        }

        ClanCat clanCat = ClanCat.buildCat(cat);

        clan.clanCats.put(cat.getUUID(), clanCat);
        setDirty();
        return true;
    }

    public boolean addClanCat(Clan clan, WCatEntity cat) {
        if (clan == null) return false;
        if (cat == null) return false;

        ClanCat clanCat = ClanCat.buildCat(cat);

        clan.clanCats.put(cat.getUUID(), clanCat);
        setDirty();
        return true;
    }

    public boolean removeClanCatFromAnyClan(WCatEntity cat) {
        if (cat == null) return false;
        Clan clan = getClan(cat.getClanUUID());
        if (clan == null) return false;

//        Component catLeftClanLog = Component.empty()
//                .append(cat.hasCustomName() ? cat.getCustomName().copy() : Component.literal("A Cat"))
//                .append(" has left ")
//                .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)));
//
//        if (cat.level() instanceof ServerLevel serverLevel) {
//            this.registerLog(serverLevel, clan.clanUUID, catLeftClanLog);
//        }

        clan.clanCats.remove(cat.getUUID());
        setDirty();
        return true;
    }

    public boolean removeClanCatFromClan(UUID clanUUID, WCatEntity cat) {
        if (clanUUID == null) return false;
        Clan clan = getClan(clanUUID);
        if (clan == null) return false;

        Component catLeftClanLog = Component.empty()
                .append(cat.hasCustomName() ? cat.getCustomName().copy() : Component.literal("A Cat"))
                .append(" has left ")
                .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)));

        if (cat.level() instanceof ServerLevel serverLevel) {
            this.registerLog(serverLevel, clan.clanUUID, catLeftClanLog);
        }

        clan.clanCats.remove(cat.getUUID());
        setDirty();
        return true;
    }

    public boolean changeMemberPermissions(ServerPlayer player, UUID clanUUID, ClanPermissions permissions) {
        Clan clan = getClan(clanUUID);
        if (clan == null) return false;
        if (player == null) return false;

        UUID playerUUID = player.getUUID();
        if (!clan.memberPerms.containsKey(playerUUID)) return false;

        clan.memberPerms.put(playerUUID, permissions);

        Component playerJoinedLog = Component.empty()
                .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.YELLOW))
                .append(" has been made ")
                .append(Component.literal(permissions.name()).withStyle(ChatFormatting.RED));

        this.registerLog(player.serverLevel(), clan.clanUUID, playerJoinedLog);

        player.sendSystemMessage(
                Component.empty()
                        .append(Component.literal("You have been made ").withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(permissions.name()).withStyle(ChatFormatting.RED))
                        .append(" of ")
                        .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color))));

        setDirty();

        return true;
    }

    public boolean changeMemberPermissions(UUID playerUUID, UUID clanUUID, ClanPermissions permissions) {
        Clan clan = getClan(clanUUID);
        if (clan == null) return false;
        if (!clan.memberPerms.containsKey(playerUUID)) return false;

        clan.memberPerms.put(playerUUID, permissions);

        setDirty();

        return true;
    }

    public boolean addMember(ServerPlayer player, UUID clanUUID, ClanPlayerRank rank) {
        Clan clan = getClan(clanUUID);
        if (clan == null) return false;
        if (player == null) return false;

        UUID playerUUID = player.getUUID();
        if (clan.members.containsKey(playerUUID)) return false;

        clan.members.put(playerUUID, rank);
        clan.memberPerms.put(playerUUID, ClanPermissions.GUEST);


        player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            cap.setClanName(clan.name);
            cap.setCurrentClanUUID(clan.clanUUID);

            for (Entity entity : player.serverLevel().getEntities().getAll()) {
                if (entity instanceof WCatEntity cat) {
                    if (cat.isOwnedBy(player)) {
                        cat.setClanUUID(clan.clanUUID);
                    }
                }
            }

            ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);

            Component playerJoinedLog = Component.empty()
                    .append(Component.literal(cap.getMorphName()).withStyle(ChatFormatting.AQUA))
                    .append(Component.literal("(").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                    .append(" has joined ")
                    .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)))
                    .append("!");

            this.registerLog(player.serverLevel(), clan.clanUUID, playerJoinedLog);
        });
        player.sendSystemMessage(Component.empty()
                .append("You now belong to ")
                .append(clan.name).withStyle(Style.EMPTY.withColor(clan.color)));




        setDirty();


        return true;
    }

    public boolean removeMember(ServerPlayer player, UUID clanUUID) {
        Clan clan = getClan(clanUUID);
        if (clan == null) return false;
        if (player == null) return false;

        UUID playerUUID = player.getUUID();
        if (!clan.members.containsKey(playerUUID)) return false;

        clan.members.remove(playerUUID);
        clan.memberPerms.remove(playerUUID);

        player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(cap -> {
            cap.setClanName("None");
            cap.setCurrentClanUUID(EMPTY_UUID);

            for (Entity entity : player.serverLevel().getEntities().getAll()) {
                if (entity instanceof WCatEntity cat) {
                    if (cat.isOwnedBy(player)) {
                        cat.setClanUUID(EMPTY_UUID);
                        clan.clanCats.remove(cat.getUUID());
                    }
                }
            }

            ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);

            Component playerLeftLog = Component.empty()
                    .append(Component.literal(cap.getMorphName()).withStyle(ChatFormatting.AQUA))
                    .append(Component.literal("(").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                    .append(" has left ")
                    .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)));

            this.registerLog(player.serverLevel(), clan.clanUUID, playerLeftLog);
        });
        player.sendSystemMessage(Component.literal("You have been removed from your clan.").withStyle(ChatFormatting.YELLOW));

        if (clan.members.isEmpty()) {
            deleteClan(player.serverLevel(), clanUUID);
        }

        setDirty();



        return true;
    }

    public boolean removeMember(UUID targetUUID, UUID clanUUID) {
        Clan clan = getClan(clanUUID);
        if (clan == null) return false;

        if (!clan.members.containsKey(targetUUID)) return false;

        clan.members.remove(targetUUID);
        clan.memberPerms.remove(targetUUID);

        setDirty();
        return true;
    }

    public boolean changeMemberRank(ServerPlayer player, UUID clanUUID, ClanPlayerRank newRank) {
        Clan clan = getClan(clanUUID);
        if (clan == null) return false;
        if (player == null) return false;

        UUID playerUUID = player.getUUID();
        if (!clan.members.containsKey(playerUUID)) return false;

        String morphName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                .map(PlayerClanData::getMorphName).orElse(player.getName().getString());

        Component playerRankChangeLog = Component.empty()
                .append(Component.literal(morphName).withStyle(ChatFormatting.AQUA))
                .append(Component.literal("(").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.GRAY))
                .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                .append(" has been made a ")
                .append(Component.literal(newRank.name()).withStyle(ChatFormatting.GOLD));

        this.registerLog(player.serverLevel(), clan.clanUUID, playerRankChangeLog);

        clan.members.put(playerUUID, newRank);
        setDirty();
        return true;
    }

    public boolean changeMemberRank(UUID targetUUID, UUID clanUUID, ClanPlayerRank newRank) {
        Clan clan = getClan(clanUUID);
        if (clan == null) return false;

        if (!clan.members.containsKey(targetUUID)) return false;

        clan.members.put(targetUUID, newRank);
        setDirty();
        return true;
    }

    public boolean deleteClan(ServerLevel level, UUID clanUUID) {

        Clan clan = getClan(clanUUID);
        if (clan == null) return false;

        clan.members.clear();
        clan.memberPerms.clear();

        clans.remove(clanUUID);

        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA).ifPresent(
                    cap -> {
                        if (clanUUID.equals(cap.getCurrentClanUUID())) {
                            cap.setCurrentClanUUID(EMPTY_UUID);
                            cap.setClanName("None");
                            player.sendSystemMessage(Component.literal("You have been removed from your clan.").withStyle(ChatFormatting.YELLOW));
                            ModPackets.sendToPlayer(new S2CSyncClanDataPacket(cap), player);
                        }
                    }
            );
        }

        for (Entity entity : level.getEntities().getAll()) {
            if (entity instanceof WCatEntity cat) {
                if (clanUUID.equals(cat.getClanUUID())) {
                    cat.setClanUUID(EMPTY_UUID);
                }
            }
        }

        setDirty();
        return true;
    }


    public Clan getClan(UUID clanUUID) {
        return clans.get(clanUUID);
    }

    public void registerLog(ServerLevel level, UUID clanUUID, Component message) {
        Clan clan = getClan(clanUUID);
        if (clan == null) return;

        long time = level.getGameTime();

        clan.logs.add(new ClanLogEntry(time, message));

        if (clan.logs.size() > 300) {
            clan.logs.remove(0);
        }

        setDirty();
    }


    public static String normalizeName(String name) {
        return name
                .toLowerCase(Locale.ROOT)
                .trim()
                .replace(" ", "_");
    }

    @Nullable
    public Clan getClanByName(String input) {
        for (Clan clan : clans.values()) {
            if (clan.normalizedName.equals(normalizeName(input))) {
                return clan;
            }
        }
        return null;
    }

    public boolean canManage(Clan clan, UUID player) {
        ClanPlayerRank rank = clan.members.get(player);
        ClanPermissions perms = clan.memberPerms.get(player);
        if (rank == null) return false;
        if (perms == null) return false;

        boolean can = rank == ClanPlayerRank.LEADER
                || perms == ClanPermissions.OWNER
                || perms == ClanPermissions.ADMIN;

        return can;
    }

    public boolean canManagePlayer(UUID clanUUID, UUID managerUUID, UUID targetUUID) {
        ClanData.Clan clan = getClan(clanUUID);
        if (clan == null) return false;
        ClanPermissions managerPerms = clan.memberPerms.get(managerUUID);
        ClanPermissions targetPerms = clan.memberPerms.get(targetUUID);
        if (!clan.memberPerms.containsKey(managerUUID)) return false;
        if (!clan.memberPerms.containsKey(targetUUID)) return false;


        if (targetPerms == ClanPermissions.OWNER) return false;
        if (managerPerms == ClanPermissions.OWNER) return true;
        if (managerPerms == ClanPermissions.ADMIN && targetPerms == ClanPermissions.ADMIN) return false;

        return managerPerms == ClanPermissions.ADMIN;
    }

    public boolean canInvite(Clan clan, UUID player) {
        ClanPlayerRank rank = clan.members.get(player);
        ClanPermissions perms = clan.memberPerms.get(player);
        if (rank == null) return false;
        if (perms == null) return false;

        boolean can = rank == ClanPlayerRank.LEADER
                || perms == ClanPermissions.OWNER
                || perms == ClanPermissions.ADMIN;

        return can;
    }

    public boolean canCommandWarriors(Clan clan, UUID player) {
        ClanPermissions perms = clan.memberPerms.get(player);
        if (perms == null) return false;

        boolean can = (perms != ClanPermissions.GUEST);

        return can;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {

        tag.putInt("Version", DATA_VERSION);
        ListTag clanList = new ListTag();

        for (Clan clan : clans.values()) {

            CompoundTag clanTag = new CompoundTag();
            clanTag.putUUID("ClanUUID", clan.clanUUID);
            clanTag.putString("Name", clan.name);
            clanTag.putInt("Color", clan.color);

            clanTag.putUUID("Leader", clan.leader);
            clanTag.putString("NameKey", clan.normalizedName);
            clanTag.putString("LeaderName", clan.leaderName);
            clanTag.putString("ClanBio", clan.clanBioSentence);
            clanTag.putInt("SymbolIndex", clan.clanSymbolIndex);


            ListTag members = new ListTag();
            for (var entry : clan.members.entrySet()) {
                CompoundTag m = new CompoundTag();
                m.putUUID("UUID", entry.getKey());
                m.putString("Rank", entry.getValue().name());
                members.add(m);
            }

            clanTag.put("Members", members);

            ListTag catList = new ListTag();
            for (ClanCat cat : clan.clanCats.values()) {
                catList.add(cat.save());
            }
            clanTag.put("ClanCats", catList);


            ListTag logList = new ListTag();
            for (ClanLogEntry log : clan.logs) {
                logList.add(log.save());
            }
            clanTag.put("Logs", logList);


            ListTag permsList = new ListTag();
            for (var entry : clan.memberPerms.entrySet()) {
                CompoundTag p = new CompoundTag();
                p.putUUID("UUID", entry.getKey());
                p.putString("Perm", entry.getValue().name());
                permsList.add(p);
            }
            clanTag.put("Permissions", permsList);

            clanList.add(clanTag);
        }
        tag.put("Clans", clanList);




        ListTag morphList = new ListTag();
        for (var entry : playerMorphNames.entrySet()) {
            CompoundTag morphTag = new CompoundTag();
            morphTag.putUUID("UUID", entry.getKey());
            morphTag.putString("MorphName", entry.getValue());
            morphList.add(morphTag);
        }
        tag.put("PlayerMorphNames", morphList);

        ListTag morphDataList = new ListTag();
        for (var entry : playerMorphData.entrySet()) {
            CompoundTag morphTag = new CompoundTag();
            morphTag.putUUID("UUID", entry.getKey());
            morphTag.putInt("MorphData", entry.getValue());
            morphDataList.add(morphTag);
        }
        tag.put("PlayerMorphData", morphDataList);

        return tag;
    }

    public static ClanData load(CompoundTag tag) {
        ClanData data = new ClanData();

        int version = tag.contains("Version") ? tag.getInt("Version") : 0;

        if (!tag.contains("Clans", Tag.TAG_LIST)) {
            return data;
        }

        ListTag clanList = tag.getList("Clans", Tag.TAG_COMPOUND);
        for (Tag t : clanList) {
            CompoundTag clanTag = (CompoundTag) t;

            Clan clan = new Clan();
            clan.clanUUID = clanTag.getUUID("ClanUUID");
            clan.name = clanTag.getString("Name");
            clan.color = clanTag.getInt("Color");
            clan.leader = clanTag.getUUID("Leader");
            clan.leaderName = clanTag.getString("LeaderName");
            clan.clanBioSentence = clanTag.getString("ClanBio");
            clan.clanSymbolIndex = clanTag.getInt("SymbolIndex");

            clan.normalizedName = clanTag.getString("NameKey");

            clan.members = new HashMap<>();
            ListTag members = clanTag.getList("Members", Tag.TAG_COMPOUND);
            for (Tag m : members) {
                CompoundTag mt = (CompoundTag) m;
                clan.members.put(
                        mt.getUUID("UUID"),
                        ClanPlayerRank.valueOf(mt.getString("Rank"))
                );
            }

            ListTag catList = clanTag.getList("ClanCats", Tag.TAG_COMPOUND);
            for (int i = 0; i < catList.size(); i++) {
                CompoundTag catTag = catList.getCompound(i);
                ClanCat cat = ClanCat.load(catTag);

                clan.clanCats.put(cat.catUUID, cat);
            }


            if (clanTag.contains("Logs", Tag.TAG_LIST)) {
                ListTag logList = clanTag.getList("Logs", Tag.TAG_COMPOUND);
                for (int i = 0; i < logList.size(); i++) {
                    CompoundTag logTag = logList.getCompound(i);
                    clan.logs.add(ClanLogEntry.load(logTag));
                }
            }

            clan.memberPerms = new HashMap<>();

            if (clanTag.contains("Permissions", Tag.TAG_LIST)) {
                ListTag permsList = clanTag.getList("Permissions", Tag.TAG_COMPOUND);
                for (Tag p : permsList) {
                    CompoundTag pt = (CompoundTag) p;
                    clan.memberPerms.put(
                            pt.getUUID("UUID"),
                            ClanPermissions.valueOf(pt.getString("Perm"))
                    );
                }
            }


            data.clans.put(clan.clanUUID, clan);
        }

        if (tag.contains("PlayerMorphNames", Tag.TAG_LIST)) {
            ListTag morphList = tag.getList("PlayerMorphNames", Tag.TAG_COMPOUND);
            for (Tag t : morphList) {
                CompoundTag morphTag = (CompoundTag) t;
                data.playerMorphNames.put(
                        morphTag.getUUID("UUID"),
                        morphTag.getString("MorphName")
                );
            }
        }

        if (tag.contains("PlayerMorphData", Tag.TAG_LIST)) {
            ListTag morphList = tag.getList("PlayerMorphData", Tag.TAG_COMPOUND);
            for (Tag t : morphList) {
                CompoundTag morphTag = (CompoundTag) t;
                data.playerMorphData.put(
                        morphTag.getUUID("UUID"),
                        morphTag.getInt("MorphData")
                );
            }
        }

        return data;
    }

    public static ClanData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                ClanData::load,
                ClanData::new,
                "wce_clans"
        );
    }

    public List<Clan> getAllClans() {
        return new ArrayList<>(clans.values());
    }

    @Nullable
    public Clan getClanByCat(UUID catUUID) {
        for (Clan clan : clans.values()) {
            if (clan.clanCats.containsKey(catUUID)) {
                return clan;
            }
        }
        return null;
    }

}

