package net.snowteb.warriorcats_events.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.sound.ModSounds;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, value = Dist.CLIENT)
public class ModEvents {


    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        SoundInstance sound = event.getSound();

        if (sound != null && sound.getLocation().equals(ModSounds.RIVERCLAN.getId())) {
            Minecraft.getInstance().getSoundManager().stop(null, SoundSource.MUSIC);
        }
    }


}
