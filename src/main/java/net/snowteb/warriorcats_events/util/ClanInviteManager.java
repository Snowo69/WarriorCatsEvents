package net.snowteb.warriorcats_events.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClanInviteManager {

    private static final Map<UUID, ClanInvite> invites = new HashMap<>();

    public static void invite(ServerPlayer invited, UUID clanUUID, ServerPlayer inviter) {
        invites.put(
                invited.getUUID(),
                new ClanInvite(clanUUID, inviter.getUUID())
        );
    }

    public static ClanInvite getInvite(ServerPlayer player) {
        return invites.get(player.getUUID());
    }

    public static void clear(ServerPlayer player) {
        invites.remove(player.getUUID());
    }

    public static void tick(ServerPlayer player) {
        ClanInvite invite = invites.get(player.getUUID());
        if (invite == null) return;

        if (invite.tick()) {
            invites.remove(player.getUUID());
            player.sendSystemMessage(
                    Component.literal("The invite has expired").withStyle(ChatFormatting.GRAY)
            );
        }
    }


    public static class ClanInvite {
        public final UUID clanUUID;
        public final UUID inviter;
        private int ticksLeft;

        public static final int TIME = 30 * 20;

        public ClanInvite(UUID clanUUID, UUID inviter) {
            this.clanUUID = clanUUID;
            this.inviter = inviter;
            this.ticksLeft = TIME;
        }

        public boolean tick() {
            ticksLeft--;
            return ticksLeft <= 0;
        }

    }

}

