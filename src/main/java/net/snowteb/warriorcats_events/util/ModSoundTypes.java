package net.snowteb.warriorcats_events.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public class ModSoundTypes {

    public static final SoundType PREY_BONES =
            new SoundType(0.7f, 1.4f,
                    SoundEvents.TURTLE_EGG_BREAK,
                    SoundEvents.BONE_BLOCK_STEP,
                    SoundEvents.TURTLE_EGG_CRACK,
                    SoundEvents.BONE_BLOCK_HIT,
                    SoundEvents.BONE_BLOCK_FALL
            );

}
