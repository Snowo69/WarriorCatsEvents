package net.snowteb.warriorcats_events.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
/**
 * A goal that makes an entity find and move to certain blocks.
 */
public class MoveToGrassGoal extends MoveToBlockGoal {

    private static final Block[] TARGET_BLOCKS = {
            Blocks.GRASS,
            Blocks.TALL_GRASS,
            Blocks.DANDELION,
            Blocks.POPPY,
            Blocks.FERN,
            Blocks.LARGE_FERN,
            Blocks.OAK_LOG,
            Blocks.SPRUCE_LOG,
            Blocks.BIRCH_LOG,
            Blocks.DARK_OAK_LOG,


    };

    public MoveToGrassGoal(PathfinderMob mob, double speed, int searchRange) {
        super(mob, speed, searchRange);
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        for (Block block : TARGET_BLOCKS) {
            if (state.is(block)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double acceptedDistance() {
        return 1D;
    }
}
