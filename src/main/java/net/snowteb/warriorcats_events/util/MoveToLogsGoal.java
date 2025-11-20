package net.snowteb.warriorcats_events.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MoveToLogsGoal extends MoveToBlockGoal {

    private static final Block[] TARGET_BLOCKS = {
            Blocks.OAK_LOG,
            Blocks.SPRUCE_LOG,
            Blocks.BIRCH_LOG,
            Blocks.DARK_OAK_LOG,


    };

    public MoveToLogsGoal(PathfinderMob mob, double speed, int searchRange) {
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
        return 2D;
    }
}
