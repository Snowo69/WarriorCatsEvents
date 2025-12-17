package net.snowteb.warriorcats_events.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModDoorBlock extends DoorBlock {


    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_AABB  = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB  = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);


    protected static final VoxelShape SOUTH_OPEN_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_OPEN_AABB = Block.box(0.0D, 0.0D, 13.0D, 3.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_OPEN_AABB  = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape EAST_OPEN_AABB  = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 3.0D);
    protected static final VoxelShape SOUTH_OPEN_AABB_2 = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_OPEN_AABB_2 = Block.box(13.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_OPEN_AABB_2  = Block.box(13.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_OPEN_AABB_2  = Block.box(0.0D, 0.0D, 13.0D, 3.0D, 16.0D, 16.0D);

    public ModDoorBlock(BlockBehaviour.Properties properties, BlockSetType type) {
        super(properties, type);
    }

    /**
     * The blockstate collision boxes change depending on the direction
     * the block is facing.
     */

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean isClosed = !state.getValue(OPEN);
        boolean hingeRight = state.getValue(HINGE) == DoorHingeSide.RIGHT;

        switch (direction) {
            case EAST:
            default:
                return isClosed ? EAST_AABB : (hingeRight ? NORTH_OPEN_AABB : SOUTH_OPEN_AABB);
            case SOUTH:
                return isClosed ? SOUTH_AABB : (hingeRight ? EAST_OPEN_AABB : WEST_OPEN_AABB);
            case WEST:
                return isClosed ? WEST_AABB : (hingeRight ? SOUTH_OPEN_AABB_2 : NORTH_OPEN_AABB_2);
            case NORTH:
                return isClosed ? NORTH_AABB : (hingeRight ? WEST_OPEN_AABB_2 : EAST_OPEN_AABB_2);
        }
    }
}
