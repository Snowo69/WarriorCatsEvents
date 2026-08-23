package net.snowteb.warriorcats_events.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;

import java.util.Map;

public interface SoundEngineAccess {
    Map<SoundInstance, ChannelAccess.ChannelHandle> wce$instanceToChannel();
}
