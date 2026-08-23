package net.snowteb.warriorcats_events.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class BGMusicSoundInstance extends AbstractSoundInstance {

    private final int fadeTime;
    private final float maxVolume;
    private boolean stoping = false;

    public BGMusicSoundInstance(ResourceLocation pLocation,
                                float pVolume, float pPitch,
                                RandomSource pRandom,
                                int fadeTime, float pMaxVolume) {
        super(pLocation, SoundSource.MUSIC, pRandom);
        this.volume = pVolume;
        this.pitch = pPitch;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.looping = false;
        this.delay = 0;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.relative = true;

        this.fadeTime = fadeTime;
        this.maxVolume = pMaxVolume;
    }

    public static BGMusicSoundInstance instance(SoundEvent pSound) {
        return new BGMusicSoundInstance(pSound.getLocation(), 0.01F, 1.0f,
                SoundInstance.createUnseededRandom(),
                160, 0.4f);
    }

    public void tick() {
        float volumeIncrementPerTick = this.maxVolume / this.fadeTime;

        if (stoping) {
            this.volume = volume - volumeIncrementPerTick;
            if (this.volume <= 0.0F) {
                this.delete();
            }
        } else {
            if (this.volume < maxVolume) {
                this.volume = volume + volumeIncrementPerTick;
                this.volume = Mth.clamp(this.volume,0, maxVolume);
            }
        }
    }
    public void stop() {
        this.stoping = true;
    }
    private void delete() {
        Minecraft.getInstance().getMusicManager().stopPlaying();
    }


    public float getModifiedVolume() {
        return calculateVolume(this.volume, this.source);
    }

    private float calculateVolume(float pVolumeMultiplier, SoundSource pSource) {
        return Mth.clamp(pVolumeMultiplier * this.getSourceVolume(pSource), 0.0F, 1.0F);
    }

    private float getSourceVolume(SoundSource pSource) {
        return pSource != null && pSource != SoundSource.MASTER ? Minecraft.getInstance().options.getSoundSourceVolume(pSource) : 1.0F;
    }
}
