package net.snowteb.warriorcats_events.client;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.others.SendDialoguePacket;
import net.snowteb.warriorcats_events.sound.ModSounds;

import java.util.LinkedList;
import java.util.Queue;

public class DialogueMessageDistributor {

    public static class Dialogue {
        private final Component sender;
        private int time;
        private final Queue<Character> charactersQueue = new LinkedList<>();
        private String message;
        private int tickCount;
        public final WCatEntity cat;

        public Dialogue(Component sender, String dialogue, WCatEntity cat) {
            this(sender, dialogue, calculateTime(dialogue), cat);
        }

        private static int calculateTime(String dialogue) {
            if (!dialogue.isEmpty()) {
                return 50 + 60*(dialogue.length()/150);
            }
            return 20;
        }

        public Dialogue(Component sender, String dialogue, int time1, WCatEntity cat) {
            this.sender = sender;
            this.time = time1;
            this.message = "";
            this.cat = cat;
            for (int i = 0; i < dialogue.length(); i++) {
                this.charactersQueue.add(dialogue.charAt(i));
            }
        }

        public void tick() {
            tickCount++;

            if (!charactersQueue.isEmpty()) {
                boolean playSound = true;

                char next1 = charactersQueue.poll();
                this.message = message + next1;
                if (String.valueOf(next1).isBlank()) playSound = false;

                if (!charactersQueue.isEmpty()) {
                    char next2 = charactersQueue.poll();
                    this.message = message + next2;
                }

                if (this.tickCount % 2 == 0) {
                    if (playSound) {
                        WCEClient.playLocalSound(ModSounds.DIALOGUE_BOOP_1.get(), SoundSource.MASTER, 0.5f, 1f);
                    }
                }
            }

            if (!isComplete()) return;
            if (time > 0) time--;
        }

        public String getMessage() {
            return message;
        }

        public Component getComponent() {
            return Component.empty()
                    .append(sender.copy())
                    .append("\n")
                    .append("\n")
                    .append(getMessage());
        }

        private boolean isComplete() {
            return charactersQueue.isEmpty();
        }

        public boolean shouldRemove() {
            return time <= 0;
        }

        public float getAlpha() {
            return time > 20 ? 1f : (float) time / 20;
        }

    }

    public static void send(Player player, WCatEntity cat, String dialogue) {
        Component sender = cat.getName();

        if (player.level().isClientSide()) {
            DialogueMessage.send(sender, dialogue, cat);
        } else {
            ModPackets.sendToPlayer(new SendDialoguePacket(sender, dialogue, cat), ((ServerPlayer) player));
        }
    }

}
