package net.snowteb.warriorcats_events.block.custom;


import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

import static net.minecraft.world.level.block.BedBlock.*;

public class MossbedBlock extends Block {
	public MossbedBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.MOSS).strength(0.4f, 10f)
                .noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
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
		return box(0, 0, 0, 16, 4, 16);
	}

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (pState.getValue(PART) != BedPart.HEAD) {
                pPos = pPos.relative(pState.getValue(FACING));
                pState = pLevel.getBlockState(pPos);
                if (!pState.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            if (!canSetSpawn(pLevel)) {
                pLevel.removeBlock(pPos, false);
                BlockPos blockpos = pPos.relative(pState.getValue(FACING).getOpposite());
                if (pLevel.getBlockState(blockpos).is(this)) {
                    pLevel.removeBlock(blockpos, false);
                }

                Vec3 vec3 = pPos.getCenter();
                pLevel.explode((Entity)null, pLevel.damageSources().badRespawnPointExplosion(vec3), (ExplosionDamageCalculator)null, vec3, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return InteractionResult.SUCCESS;
            } else if (pState.getValue(OCCUPIED)) {

                    pPlayer.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);


                return InteractionResult.SUCCESS;
            } else {
                pPlayer.startSleepInBed(pPos).ifLeft((p_49477_) -> {
                    if (p_49477_.getMessage() != null) {
                        pPlayer.displayClientMessage(p_49477_.getMessage(), true);
                    }

                });
                return InteractionResult.SUCCESS;
            }
        }
    }

    public static boolean canSetSpawn(Level pLevel) {
        return pLevel.dimensionType().bedWorks();
    }


}

