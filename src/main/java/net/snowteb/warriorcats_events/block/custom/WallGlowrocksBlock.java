package net.snowteb.warriorcats_events.block.custom;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

import static net.snowteb.warriorcats_events.block.custom.GlowrocksBlock.lightLevel;

public class WallGlowrocksBlock extends TorchBlock {

    private static final Map<Direction, VoxelShape> AABBS
            = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.box(1.0D, 1.0D, 14.0D, 15.0D, 15.0D, 16.0D),
            Direction.SOUTH, Block.box(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 2.0D),
            Direction.WEST, Block.box(14.0D, 1.0D, 1.0D, 16.0D, 15.0D, 15.0D),
            Direction.EAST, Block.box(0.0D, 1.0D, 1.0D, 2.0D, 15.0D, 15.0D)));

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final IntegerProperty AMOUNT_WG = IntegerProperty.create("amount", 1, 4);

    public WallGlowrocksBlock(BlockBehaviour.Properties pProperties, ParticleOptions pFlameParticle) {
        super(pProperties, pFlameParticle);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT_WG, 1));
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pContext) {
        return !pContext.isSecondaryUseActive()
                && pContext.getItemInHand().is(this.asItem())
                && pState.getValue(AMOUNT_WG) < 4 || super.canBeReplaced(pState, pContext);
    }

    @Override
    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getShape(pState);
    }

    public static VoxelShape getShape(BlockState pState) {
        return AABBS.get(pState.getValue(FACING));
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction direction = pState.getValue(FACING);
        BlockPos blockpos = pPos.relative(direction.getOpposite());
        BlockState blockstate = pLevel.getBlockState(blockpos);
        return blockstate.isFaceSturdy(pLevel, blockpos, direction);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockstate = this.defaultBlockState();
        LevelReader levelreader = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        Direction[] adirection = pContext.getNearestLookingDirections();

        BlockState target = pContext.getLevel().getBlockState(pContext.getClickedPos());
        if (target.is(this)) {
            return target.setValue(AMOUNT_WG, Math.min(4, target.getValue(AMOUNT_WG) + 1));
        }

        for(Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction1 = direction.getOpposite();
                blockstate = blockstate.setValue(FACING, direction1);
                if (blockstate.canSurvive(levelreader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextFloat() < 0.1) {
            Direction direction = pState.getValue(FACING);
            double d0 = (double) pPos.getX() + 0.5D;
            double d1 = (double) pPos.getY() + 0.5D;
            double d2 = (double) pPos.getZ() + 0.5D;
            Direction direction1 = direction.getOpposite();

            float randomX = direction1.getStepZ() != 0 ? pRandom.nextFloat() - 0.5f : 0;
            float randomZ = direction1.getStepX() != 0 ? pRandom.nextFloat() - 0.5f : 0;

            float randomY = pRandom.nextFloat() - 0.5f;

            pLevel.addParticle(this.flameParticle,
                    d0 + 0.4D * (double) direction1.getStepX() + randomX * 0.8f,
                    d1 + randomY * 0.8f,
                    d2 + 0.4D * (double) direction1.getStepZ() + randomZ * 0.8f,
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING).add(AMOUNT_WG);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return 2;
    }
}
