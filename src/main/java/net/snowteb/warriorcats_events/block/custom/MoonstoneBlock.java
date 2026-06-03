package net.snowteb.warriorcats_events.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.snowteb.warriorcats_events.block.entity.MoonstoneBlockEntity;
import net.snowteb.warriorcats_events.screen.menus.MoonstoneMenu;
import org.jetbrains.annotations.Nullable;

public class MoonstoneBlock extends EnchantingTableBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("block.warriorcats_events.moonstone");

    private static final VoxelShape MOONSTONE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 11.0D, 15.0D);

    public MoonstoneBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MoonstoneBlockEntity(pPos, pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return MOONSTONE_SHAPE;
    }

    @Override
    public @Nullable MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((id, inv, player) -> {
            return new MoonstoneMenu(id, inv, ContainerLevelAccess.create(pLevel, pPos));
        }, CONTAINER_TITLE);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextFloat() < 0.1f){
            double d0 = (double) pPos.getX() + 0.5D;
            double d1 = (double) pPos.getY() + 0.1D;
            double d2 = (double) pPos.getZ() + 0.5D;

            float randomX = pRandom.nextFloat() - 0.5f;
            float randomZ = pRandom.nextFloat() - 0.5f;
            float randomY = pRandom.nextFloat();

            pLevel.addParticle(ParticleTypes.END_ROD,
                    d0 + randomX * 0.8f,
                    d1 + randomY * 0.8f,
                    d2 + randomZ * 0.8f,
                    0.0D, 0.0D, 0.0D);

            pLevel.addParticle(ParticleTypes.ENCHANT,
                    d0 + randomX * 0.8f,
                    d1 + randomY * 0.8f,
                    d2 + randomZ * 0.8f,
                    0.2D, 0.2D, 0.2D);
        }
    }
}
