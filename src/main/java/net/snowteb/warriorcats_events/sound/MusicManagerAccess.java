package net.snowteb.warriorcats_events.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.Music;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MusicManagerAccess {

    @Nullable
    SoundInstance wce$getCurrentMusic();

    boolean wce$isPlaylistPlaying(List<Music> music);

    void wce$playBackgroundSong(Music music);
}
