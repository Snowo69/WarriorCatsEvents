package net.snowteb.warriorcats_events.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CarryPlayerRequestManager {

    private static final Map<UUID, CarryRequest> requests = new HashMap<>();

    public static void request(ServerPlayer target, ServerPlayer requester) {
        requests.put(
                target.getUUID(),
                new CarryRequest(requester.getUUID())
        );
    }

    public static CarryRequest getRequest(ServerPlayer player) {
        return requests.get(player.getUUID());
    }

    public static void clear(ServerPlayer player) {
        requests.remove(player.getUUID());
    }

    public static void tick(ServerPlayer player) {
        CarryRequest request = requests.get(player.getUUID());
        if (request == null) return;

        if (request.tick()) {
            requests.remove(player.getUUID());
            player.sendSystemMessage(
                    Component.literal("The request has expired").withStyle(ChatFormatting.GRAY)
            );
        }
    }


    public static class CarryRequest {
        public final UUID requester;
        private int ticksLeft;

        public static final int TIME = 30 * 20;

        public CarryRequest(UUID requester) {
            this.requester = requester;
            this.ticksLeft = TIME;
        }

        public boolean tick() {
            ticksLeft--;
            return ticksLeft <= 0;
        }

    }

}

