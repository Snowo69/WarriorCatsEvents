package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.snowteb.warriorcats_events.screen.menus.StickfireMenu;

public class StickfireBlockEntity extends AbstractFurnaceBlockEntity {
    public StickfireBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.STICKFIRE.get(), pPos, pBlockState, RecipeType.SMELTING);
    }

    protected Component getDefaultName() {
        return Component.translatable("block.warriorcats_events.stickfire");
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new StickfireMenu(pId, pPlayer, this, this.dataAccess);
    }
}