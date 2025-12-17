package net.snowteb.warriorcats_events.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.SoundType;

/**
 * This is just a set of sounds.
 */
public class ModBlockSetTypes {
    public static final BlockSetType LEAF = registerLeafType();

    private static BlockSetType registerLeafType() {
        BlockSetType leaf = new BlockSetType(
                "leaf",
                true,
                SoundType.CHERRY_LEAVES,
                SoundEvents.CHERRY_LEAVES_PLACE,
                SoundEvents.CHERRY_LEAVES_PLACE,
                SoundEvents.CHERRY_LEAVES_PLACE,
                SoundEvents.CHERRY_LEAVES_PLACE,
                SoundEvents.CHERRY_LEAVES_PLACE,
                SoundEvents.CHERRY_LEAVES_PLACE,
                SoundEvents.CHERRY_LEAVES_PLACE,
                SoundEvents.CHERRY_LEAVES_PLACE
        );

        BlockSetType.register(leaf);
        return leaf;
    }
}
