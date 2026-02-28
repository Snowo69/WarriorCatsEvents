package net.snowteb.warriorcats_events.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LeafTrapdoorBlock extends TrapDoorBlock {

    public static final IntegerProperty CLOSED_VARIANT =
            IntegerProperty.create("closed_variant", 1, 2);


    protected static final VoxelShape BOTTOM_CLOSED =
            Block.box(0, 0, 0, 16, 3, 16);
    protected static final VoxelShape TOP_CLOSED =
            Block.box(0, 13, 0, 16, 16, 16);


    protected static final VoxelShape NORTH_OPEN =
            Block.box(0, 0, 13, 16, 3, 16);
    protected static final VoxelShape SOUTH_OPEN =
            Block.box(0, 0, 0, 16, 3, 3);
    protected static final VoxelShape WEST_OPEN =
            Block.box(13, 0, 0, 16, 3, 16);
    protected static final VoxelShape EAST_OPEN =
            Block.box(0, 0, 0, 3, 3, 16);

    protected static final VoxelShape NORTH_OPEN2 =
            Block.box(0, 13, 13, 16, 16, 16);
    protected static final VoxelShape SOUTH_OPEN2 =
            Block.box(0, 13, 0, 16, 16, 3);
    protected static final VoxelShape WEST_OPEN2 =
            Block.box(13, 13, 0, 16, 16, 16);
    protected static final VoxelShape EAST_OPEN2 =
            Block.box(0, 13, 0, 3, 16, 16);

    protected static final VoxelShape NORTH_OPEN_VAR2 =
            Block.box(0, 0, 13, 16, 16, 16);
    protected static final VoxelShape SOUTH_OPEN_VAR2 =
            Block.box(0, 0, 0, 16, 16, 3);
    protected static final VoxelShape WEST_OPEN_VAR2 =
            Block.box(13, 0, 0, 16, 16, 16);
    protected static final VoxelShape EAST_OPEN_VAR2 =
            Block.box(0, 0, 0, 3, 16, 16);




    public LeafTrapdoorBlock(Properties properties, BlockSetType type) {
        super(properties, type);
        this.registerDefaultState(this.defaultBlockState().setValue(CLOSED_VARIANT, 1));

    }

    /**
     * The blockstate collision boxes change depending on the direction
     * the block is facing.
     */

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean open = state.getValue(OPEN);
        Half half = state.getValue(HALF);
        int variant = state.getValue(CLOSED_VARIANT);

        if (!open) {
            if (variant == 1) {
                return half == Half.TOP ? TOP_CLOSED : BOTTOM_CLOSED;
            } else {
                return switch (direction) {
                    case NORTH -> NORTH_OPEN_VAR2;
                    case SOUTH -> SOUTH_OPEN_VAR2;
                    case WEST  -> WEST_OPEN_VAR2;
                    case EAST  -> EAST_OPEN_VAR2;
                    default -> NORTH_OPEN_VAR2;
                };
            }
        }

        if (half == Half.BOTTOM) {
            return switch (direction) {
                case NORTH -> NORTH_OPEN;
                case SOUTH -> SOUTH_OPEN;
                case WEST  -> WEST_OPEN;
                case EAST  -> EAST_OPEN;
                default -> NORTH_OPEN;
            };
        } else {
            return switch (direction) {
                case NORTH -> NORTH_OPEN2;
                case SOUTH -> SOUTH_OPEN2;
                case WEST  -> WEST_OPEN2;
                case EAST  -> EAST_OPEN2;
                default -> NORTH_OPEN;
            };
        }

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CLOSED_VARIANT);
    }


    @Override
    public InteractionResult use(BlockState state, Level level,
                                 BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {

        boolean isOpen = state.getValue(OPEN);
        int variant = state.getValue(CLOSED_VARIANT);


        if (!level.isClientSide) {

            if (!isOpen) {
                level.setBlock(pos, state.setValue(OPEN, true), 2);
            } else {
                int nextVariant = variant == 1 ? 2 : 1;
                level.setBlock(pos,
                        state.setValue(OPEN, false)
                                .setValue(CLOSED_VARIANT, nextVariant),
                        2);
            }
        }
        this.playSound(player, level, pos, state.getValue(OPEN));

        return InteractionResult.sidedSuccess(level.isClientSide);
    }


}
