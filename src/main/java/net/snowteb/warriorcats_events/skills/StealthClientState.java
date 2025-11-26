package net.snowteb.warriorcats_events.skills;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.CtSToggleStealthPacket;

public class StealthClientState {
    private static boolean lastState = false;

    public static void tick(boolean currentState) {
        if (currentState != lastState) {
            ModPackets.sendToServer(new CtSToggleStealthPacket(currentState));
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                if (currentState) {
                    mc.player.playSound(SoundEvents.GRASS_HIT, 0.7f, 0.8f);
                } else {
                    mc.player.playSound(SoundEvents.CAT_PURREOW, 0.7f, 1.2f);
                }
            }

            lastState = currentState;
        }
    }
}
