package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.Music;
import net.snowteb.warriorcats_events.sound.BGMusicSoundInstance;
import net.snowteb.warriorcats_events.sound.CustomMusicManager;
import net.snowteb.warriorcats_events.sound.MusicManagerAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MusicManager.class)
public class MusicManagerMixin implements MusicManagerAccess {

    @Shadow
    private SoundInstance currentMusic;

    @Final
    @Shadow
    private Minecraft minecraft;

    @Override
    public SoundInstance wce$getCurrentMusic() {
        return currentMusic;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void cancelTick(CallbackInfo ci) {
        if (wce$isPlaylistPlaying(CustomMusicManager.getPlaylistTracks())) ci.cancel();
    }

    @Override
    public boolean wce$isPlaylistPlaying(List<Music> music) {

        MusicManager manager = (MusicManager) (Object) this;
        for (Music m : music) {
            if (manager.isPlayingMusic(m)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void wce$playBackgroundSong(Music music) {

        this.currentMusic = BGMusicSoundInstance.instance(music.getEvent().get());

        if (this.currentMusic.getSound() != SoundManager.EMPTY_SOUND) {
            this.minecraft.getSoundManager().play(this.currentMusic);
        }

    }
}
