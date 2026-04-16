package net.snowteb.warriorcats_events.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.snowteb.warriorcats_events.item.ModItems;

public class GlowshroomBlock extends BushBlock implements BonemealableBlock {
	public GlowshroomBlock() {
		super(Properties.of().sound(SoundType.MOSS).strength(0.2f, 10f)
                .noOcclusion().isRedstoneConductor((bs, br, bp) -> false)
                .noCollission().lightLevel(state -> 13));
	}


	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

    @Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();}

    @Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return box(2, 0, 2, 14, 5, 14);
	}

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.GRASS_BLOCK)
                || state.is(BlockTags.DIRT)
                || state.is(Blocks.STONE)
                || state.is(Blocks.MYCELIUM)
                || state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(BlockTags.DEAD_BUSH_MAY_PLACE_ON)
                || state.is(BlockTags.LOGS)
                || state.is(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        BlockState state = pLevel.getBlockState(pPos.below());

        return mayPlaceOn(state, pLevel, pPos);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {

        if (pRandom.nextFloat() < 0.1F) {
            ItemStack stack = new ItemStack(ModItems.GLOW_SHROOM.get());

            ItemEntity item = new ItemEntity(
                    pLevel,
                    pPos.getX() + 0.5,
                    pPos.getY() + 0.7,
                    pPos.getZ() + 0.5,
                    stack
            );

            pLevel.addFreshEntity(item);
        }

    }
}

