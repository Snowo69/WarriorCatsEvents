package net.snowteb.warriorcats_events.client;

import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class EntityChatBubbleManager {

    public static Map<UUID, ChatBubble> bubbles = new HashMap<>();

    public static class ChatBubble {
        public UUID sender;
        public Component message;
        public int time;

        public ChatBubble(UUID sender, Component message, int time) {
            this.message = message;
            this.sender = sender;
            this.time = time;
        }

    }

    public static void add(UUID sender, Component message, int time) {
        bubbles.put(sender, new ChatBubble(sender, message, time));
    }

    public static void tick() {

        Iterator<Map.Entry<UUID, ChatBubble>> iterator = bubbles.entrySet().iterator();
        while (iterator.hasNext()) {
            ChatBubble bubble = iterator.next().getValue();
            bubble.time--;

            if (bubble.time <= 0) {
                iterator.remove();
            }
        }

    }
}
