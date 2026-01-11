package net.snowteb.warriorcats_events.skills;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.CtSToggleStealthPacket;
import net.snowteb.warriorcats_events.sound.ModSounds;

public class StealthClientState {
    private static boolean lastState = false;

    /**
     * If the state changes, then toggle the stealth mode and play 2 cute sounds.
     * Then update the current state.
     */
    public static void tick(boolean currentState) {
        if (currentState != lastState) {
            ModPackets.sendToServer(new CtSToggleStealthPacket(currentState));
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                if (currentState) {
                    mc.player.playSound(SoundEvents.GRASS_HIT, 0.7f, 0.8f);
                    mc.player.playSound(ModSounds.STEALTH_WOOSH.get(), 0.9f, 1f);
                } else {
                    mc.player.playSound(SoundEvents.CAT_PURREOW, 0.7f, 1.2f);
                    mc.player.playSound(ModSounds.STEALTH_WOOSH.get(), 0.9f, 1.2f);

                }
            }

            lastState = currentState;
        }
    }
}
