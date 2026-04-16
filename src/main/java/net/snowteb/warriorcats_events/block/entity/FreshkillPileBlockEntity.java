package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.snowteb.warriorcats_events.screen.menus.FreshKillPileMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * On content changes, calls setChanged(), this to avoid items from disappearing after leaving the world.
 */

public class FreshkillPileBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(16) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };


    private static final int INPUT1 = 0;
    private static final int INPUT2 = 1;
    private static final int INPUT3 = 2;
    private static final int INPUT4 = 3;
    private static final int INPUT5 = 4;
    private static final int INPUT6 = 5;
    private static final int INPUT7 = 6;
    private static final int INPUT8 = 7;
    private static final int INPUT9 = 8;
    private static final int INPUT10 = 9;
    private static final int INPUT11 = 10;
    private static final int INPUT12 = 11;
    private static final int INPUT13 = 12;
    private static final int INPUT14 = 13;
    private static final int INPUT15 = 14;
    private static final int INPUT16 = 15;

    private LazyOptional<IItemHandler> LazyItemHandler = LazyOptional.empty();


    public FreshkillPileBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FRESH_KILL_PILE.get(),pPos, pBlockState);

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return LazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        LazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        LazyItemHandler.invalidate();
    }

    /**
     * For every slot in the container, add that item to an inventory, and then after its done, drop the inventory.
     */

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.warriorcats_events.fresh_kill_pile");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FreshKillPileMenu(pContainerId, pPlayerInventory, this, new SimpleContainerData(0));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public List<ItemStack> getRenderStacks() {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            ItemStack stack = this.itemHandler.getStackInSlot(i);

            if (!stack.isEmpty() && !(stack.getItem() instanceof BlockItem)) {
                list.add(stack);

                if (list.size() >= 8) {
                    break;
                }
            }
        }

        return list;
    }

    public boolean putItem(ItemStack itemStack) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(i);

            if (stackInSlot.isEmpty()) {
                itemHandler.setStackInSlot(i, itemStack);
                return true;

            } else if (ItemStack.isSameItemSameTags(stackInSlot, itemStack) && stackInSlot.getCount() + itemStack.getCount() <= stackInSlot.getMaxStackSize()) {
                stackInSlot.grow(itemStack.getCount());
                itemHandler.setStackInSlot(i, stackInSlot);
                return true;

            } else if (ItemStack.isSameItemSameTags(stackInSlot, itemStack) && stackInSlot.getCount() < stackInSlot.getMaxStackSize()) {
                int spaceAvailable = stackInSlot.getMaxStackSize() - stackInSlot.getCount();
                stackInSlot.grow(spaceAvailable);
                itemStack.shrink(spaceAvailable);
                itemHandler.setStackInSlot(i, stackInSlot);
                return true;
            }
        }

        return itemStack.isEmpty();
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
