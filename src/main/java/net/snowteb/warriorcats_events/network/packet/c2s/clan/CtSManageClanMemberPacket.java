package net.snowteb.warriorcats_events.network.packet.c2s.clan;

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

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class CtSManageClanMemberPacket {

    private final String key;
    private final String rank;
    private final UUID playerUUID;
    private final String morphName;

    public CtSManageClanMemberPacket(String key, String rank, UUID playerUUID, String morphName) {
        this.key = key;
        this.rank = rank;
        this.playerUUID = playerUUID;
        this.morphName = morphName;
    }

    public static CtSManageClanMemberPacket decode(FriendlyByteBuf buf) {
        String key = buf.readUtf();
        String rank = buf.readUtf();
        UUID playerUUID = buf.readUUID();
        String morphName = buf.readUtf();
        return new CtSManageClanMemberPacket(key, rank, playerUUID, morphName);
    }

    public static void encode(CtSManageClanMemberPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.key);
        buf.writeUtf(packet.rank);
        buf.writeUUID(packet.playerUUID);
        buf.writeUtf(packet.morphName);
    }

    public static void handle(CtSManageClanMemberPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            ClanData data = ClanData.get(level);

            ClanData.Clan clan = data.getClan(player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID));

            ServerPlayer target = level.getServer()
                    .getPlayerList()
                    .getPlayer(packet.playerUUID);


            if (clan != null) {
                if (!data.canManage(clan, player.getUUID())) return;
                if (!data.canManagePlayer(clan.clanUUID, player.getUUID(), packet.playerUUID)) {
                    player.sendSystemMessage(Component.literal("You don't have permission to do that.").withStyle(ChatFormatting.RED));
                    return;
                }
                if (!clan.members.containsKey(packet.playerUUID)) return;

                if (packet.key.equals("kick")) {
                    kickMember(packet.playerUUID, target, player, packet.morphName, clan, data);
                }
                if (packet.key.equals("changerank")) {
                    if (packet.rank != null) {

                        ClanData.ClanPlayerRank rankToSet = switch (packet.rank) {
                            case "kit" -> ClanData.ClanPlayerRank.KIT;
                            case "apprentice" -> ClanData.ClanPlayerRank.APPRENTICE;
                            case "warrior" -> ClanData.ClanPlayerRank.WARRIOR;
                            case "medapp" -> ClanData.ClanPlayerRank.MEDICINEAPP;
                            case "medicine" -> ClanData.ClanPlayerRank.MEDICINE;
                            case "deputy" -> ClanData.ClanPlayerRank.DEPUTY;
                            case "elder" -> ClanData.ClanPlayerRank.ELDER;
                            case "queen" -> ClanData.ClanPlayerRank.QUEEN;
                            default -> null;
                        };

                        if (rankToSet != null) {
                            changeRank(packet.playerUUID, target, player, packet.morphName, rankToSet, clan, data);
                        }

                    }
                }
                if (packet.key.equals("changeperms")) {
                    if (!(clan.memberPerms.get(player.getUUID()) == ClanData.ClanPermissions.OWNER || clan.members.get(player.getUUID()) == ClanData.ClanPlayerRank.LEADER)
                            && packet.rank.equals("admin")) {
                        player.sendSystemMessage(Component.literal("You don't have permission to do that.").withStyle(ChatFormatting.RED));
                        return;
                    }

                    ClanData.ClanPermissions permsToSet = switch (packet.rank) {
                        case "admin" -> ClanData.ClanPermissions.ADMIN;
                        case "member" -> ClanData.ClanPermissions.MEMBER;
                        case "guest" -> ClanData.ClanPermissions.GUEST;
                        default -> null;
                    };

                    if (permsToSet != null) {
                        changePerms(packet.playerUUID, target, player, packet.morphName, permsToSet, clan, data);
                    }

                }

            } else {
                player.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.GRAY));
            }

        });

        ctx.get().setPacketHandled(true);
    }

    private static void changeRank(UUID targetUUID,ServerPlayer target, ServerPlayer leader, String morphName, ClanData.ClanPlayerRank rank, ClanData.Clan clan, ClanData data) {

        ClanData.ClanPlayerRank currentRank = clan.members.get(targetUUID);

        if (target == leader) {
            leader.sendSystemMessage(Component.literal("You can't change your own role.").withStyle(ChatFormatting.YELLOW));
            return;
        }

        if (currentRank == rank) {
            leader.sendSystemMessage(Component.literal("The target is already a " + rank).withStyle(ChatFormatting.YELLOW));
            return;
        }

        int deputies = 0;
        int medicine = 0;
        int medicineApp = 0;
        for (Map.Entry<UUID, ClanData.ClanPlayerRank> entry : clan.members.entrySet()) {
            if (entry.getValue() == ClanData.ClanPlayerRank.DEPUTY) deputies++;
            if (entry.getValue() == ClanData.ClanPlayerRank.MEDICINE) medicine++;
            if (entry.getValue() == ClanData.ClanPlayerRank.MEDICINEAPP) medicineApp++;
        }

        if (rank == ClanData.ClanPlayerRank.DEPUTY && deputies >= 1) {
            leader.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("Role limit reached. ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal("(" + deputies + ")").withStyle(ChatFormatting.GOLD))
            );
            return;
        }
        if (rank == ClanData.ClanPlayerRank.MEDICINE && medicine >= 2) {
            leader.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("Role limit reached. ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal("(" + medicine + ")").withStyle(ChatFormatting.GOLD))
            );
            return;
        }
        if (rank == ClanData.ClanPlayerRank.MEDICINEAPP && medicineApp >= 1) {
            leader.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal("Role limit reached. ").withStyle(ChatFormatting.YELLOW))
                            .append(Component.literal("(" + medicineApp + ")").withStyle(ChatFormatting.GOLD))
            );
            return;
        }

        if (target != null) {
            data.changeMemberRank(target, clan.clanUUID, rank);
            data.setDirty();
        } else {
            data.changeMemberRank(targetUUID, clan.clanUUID, rank);
            Component playerRankChangeLog = Component.empty()
                    .append(Component.literal(morphName).withStyle(ChatFormatting.AQUA))
                    .append(Component.literal("(").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal("player")
                    .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                    .append(" has been made a ")
                    .append(Component.literal(rank.name()).withStyle(ChatFormatting.GOLD)));

            data.registerLog(leader.serverLevel(), clan.clanUUID, playerRankChangeLog);

            data.setDirty();
        }

        leader.sendSystemMessage(
                Component.empty()
                        .append(Component.literal(morphName).withStyle(ChatFormatting.GOLD))
                        .append(" has been promoted to ")
                        .append(Component.literal(String.valueOf(rank)).withStyle(ChatFormatting.AQUA))
        );

        if (target != null) {
            for (UUID memberUUID : clan.members.keySet()) {
                ServerPlayer member = leader.server.getPlayerList().getPlayer(memberUUID);
                if (member == null) continue;

                member.sendSystemMessage(
                        Component.empty()
                                .append(Component.literal(morphName).withStyle(ChatFormatting.GOLD))
                                .append(" is now ")
                                .append(Component.literal(String.valueOf(rank)).withStyle(ChatFormatting.AQUA)
                                        .append("!")
                                ));
            }
        }

    }

    private static void kickMember(UUID targetUUID ,ServerPlayer target, ServerPlayer leader, String morphName, ClanData.Clan clan, ClanData data) {

        if (target == leader) {
            leader.sendSystemMessage(Component.literal("You can't kick yourself.").withStyle(ChatFormatting.YELLOW));
            return;
        }

        if (clan != null) {
            if (target != null) {
                data.removeMember(target, clan.clanUUID);
            } else {
                data.removeMember(targetUUID, clan.clanUUID);

                Component playerLeftLog = Component.empty()
                        .append(Component.literal(morphName).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal("(").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal("player").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                        .append(" has been kicked from ")
                        .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)));

                data.registerLog(leader.serverLevel(), clan.clanUUID, playerLeftLog);
            }
            data.setDirty();

            leader.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(morphName).withStyle(ChatFormatting.GOLD))
                            .append(" has been removed from ")
                            .append(Component.literal(clan.name).withStyle(Style.EMPTY.withColor(clan.color)))
            );
        }

    }

    private static void changePerms(UUID targetUUID, ServerPlayer target, ServerPlayer leader, String morphName,ClanData.ClanPermissions perms , ClanData.Clan clan, ClanData data) {

        if (target == leader) {
            leader.sendSystemMessage(Component.literal("You can't change your own permissions.").withStyle(ChatFormatting.YELLOW));
            return;
        }

        if (clan != null) {
            if (target != null) {
                data.changeMemberPermissions(target, clan.clanUUID, perms);
            } else {
                data.changeMemberPermissions(targetUUID, clan.clanUUID, perms);

                Component playerJoinedLog = Component.empty()
                        .append(Component.literal(morphName).withStyle(ChatFormatting.AQUA))
                        .append(Component.literal("(").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal("player").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                        .append(" has been made ")
                        .append(Component.literal(perms.name()).withStyle(ChatFormatting.RED));

                data.registerLog(leader.serverLevel(), clan.clanUUID, playerJoinedLog);
            }
            data.setDirty();

            leader.sendSystemMessage(
                    Component.empty()
                            .append(Component.literal(morphName).withStyle(ChatFormatting.GOLD))
                            .append(" has been made ")
                            .append(Component.literal(perms.name()).withStyle(ChatFormatting.RED))
            );
        }

    }

}

