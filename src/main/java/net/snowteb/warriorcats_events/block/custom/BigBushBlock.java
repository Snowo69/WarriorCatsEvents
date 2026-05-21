package net.snowteb.warriorcats_events.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.Supplier;

public class BigBushBlock extends GenericBushBlock{
    public BigBushBlock(Properties props, Supplier<Item> dropItem, int minDrop, int maxDrop, SoundEvent harvestSound) {
        super(props, List.of(new DropItem(dropItem, 1f)), minDrop, maxDrop, harvestSound);
    }

    public static final VoxelShape SAPLING_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    public static final VoxelShape MID_GROWTH_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 22.0D, 15.0D);
    public static final VoxelShape FULL_GROWTH_SHAPE = Block.box(-2.0D, 0.0D, -2.0D, 20.0D, 24.0D, 18.0D);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(AGE) == 0) {
            return SAPLING_SHAPE;
        } else {
            return pState.getValue(AGE) < 3 ? MID_GROWTH_SHAPE : FULL_GROWTH_SHAPE;
        }
    }

}
