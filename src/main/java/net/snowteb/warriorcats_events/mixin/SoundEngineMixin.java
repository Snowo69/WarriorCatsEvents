package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.snowteb.warriorcats_events.sound.SoundEngineAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(SoundEngine.class)
public class SoundEngineMixin implements SoundEngineAccess {

    @Shadow
    @Final
    private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

    @Override
    public Map<SoundInstance, ChannelAccess.ChannelHandle> wce$instanceToChannel() {
        return instanceToChannel;
    }
}
