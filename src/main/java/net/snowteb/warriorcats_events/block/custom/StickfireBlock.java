package net.snowteb.warriorcats_events.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.snowteb.warriorcats_events.block.entity.ModBlockEntities;
import net.snowteb.warriorcats_events.block.entity.StickfireBlockEntity;
import org.jetbrains.annotations.Nullable;

public class StickfireBlock extends FurnaceBlock {
    public StickfireBlock(Properties pProperties) {
        super(pProperties);
    }

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pState.getValue(LIT) && pEntity instanceof Player
                && !EnchantmentHelper.hasFrostWalker((LivingEntity)pEntity)) {

            if (pEntity.hurt(pLevel.damageSources().inFire(), 2)) {
                pLevel.playSound(null, pEntity.blockPosition(),
                        SoundEvents.PLAYER_HURT_ON_FIRE, SoundSource.BLOCKS, 0.6f, 1f);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new StickfireBlockEntity(pPos, pState);
    }

    @Override
    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof StickfireBlockEntity) {
            pPlayer.openMenu((MenuProvider)blockentity);
            pPlayer.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            double d0 = (double)pPos.getX() + 0.5D;
            double d1 = pPos.getY();
            double d2 = (double)pPos.getZ() + 0.5D;
            if (pRandom.nextDouble() < 0.1D) {
                pLevel.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            double d4 = pRandom.nextDouble() * 0.5D - 0.25D;
            double d6 = pRandom.nextDouble() * 6.0D / 16.0D;
            pLevel.addParticle(ParticleTypes.SMOKE, d0 + d4, d1 + d6, d2 + d4,
                    0.0D, 0.0D, 0.0D);
            pLevel.addParticle(ParticleTypes.FLAME, d0 + d4, d1 + d6, d2 + d4,
                    0.0D, 0.0D, 0.0D);

            pLevel.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, d0 + d4, d1 + 0.8, d2 + d4,
                    0.0D, 0.05D, 0.0D);
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.STICKFIRE.get(), StickfireBlockEntity::serverTick);
    }
}
