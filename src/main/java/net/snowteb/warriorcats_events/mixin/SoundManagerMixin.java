package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.snowteb.warriorcats_events.sound.SoundManagerAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundManager.class)
public class SoundManagerMixin implements SoundManagerAccess {

    @Shadow
    @Final
    private SoundEngine soundEngine;

    @Override
    public SoundEngine wce$soundEngine() {
        return soundEngine;
    }
}
