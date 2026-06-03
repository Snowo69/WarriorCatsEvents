package net.snowteb.warriorcats_events.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GlowrocksBlock extends TorchBlock {

    public static final IntegerProperty AMOUNT_G = IntegerProperty.create("amount", 1, 6);;

    public GlowrocksBlock(Properties pProperties, SimpleParticleType pFlameParticle) {
        super(pFlameParticle, pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AMOUNT_G, 1));
    }

    public static int lightLevel(int maxRocks, BlockState state, IntegerProperty property, int maxLight) {
        int rocks = state.getValue(property);
        double ratio = (double) maxLight / maxRocks;

        return (int) (rocks * ratio);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
        return blockstate.is(this) ? blockstate.setValue(AMOUNT_G, Integer.valueOf(Math.min(6, blockstate.getValue(AMOUNT_G) + 1))) : this.defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AMOUNT_G);
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pContext) {
        return !pContext.isSecondaryUseActive()
                && pContext.getItemInHand().is(this.asItem())
                && pState.getValue(AMOUNT_G) < 6 || super.canBeReplaced(pState, pContext);
    }

    protected static final VoxelShape ROCK_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return ROCK_AABB;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.below()).isFaceSturdy(pLevel, pPos.below(), Direction.UP);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {

        if (pRandom.nextFloat() < 0.1f){
            double d0 = (double) pPos.getX() + 0.5D;
            double d1 = (double) pPos.getY() + 0.1D;
            double d2 = (double) pPos.getZ() + 0.5D;

            float randomX = pRandom.nextFloat() - 0.5f;
            float randomZ = pRandom.nextFloat() - 0.5f;

            pLevel.addParticle(this.flameParticle,
                    d0 + randomX * 0.8f,
                    d1,
                    d2 + randomZ * 0.8f,
                    0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return 2;
    }
}
