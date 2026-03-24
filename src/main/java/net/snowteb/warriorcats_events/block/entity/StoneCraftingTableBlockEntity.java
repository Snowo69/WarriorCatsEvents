package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StoneCraftingTableBlockEntity extends BlockEntity {

    private final ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    };

    public ItemStackHandler getItemStackHandler() {
        return inventory;
    }

    public StoneCraftingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.STONE_TABLE.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        inventory.deserializeNBT(pTag.getCompound("inventory"));
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

        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);

            list.add(stack);
            if (list.size() >= 5) {
                break;
            }
        }

        return list;
    }

    public boolean handleInteractValidItem(Player player, ItemStack stack) {
        boolean handled = false;

        for (int i = 0; i < this.inventory.getSlots(); i++) {
            if (this.inventory.getStackInSlot(i).isEmpty()) {
                this.inventory.setStackInSlot(i, stack.copyWithCount(1));
                if (!player.getAbilities().instabuild) player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                handled = true;
                break;
            }
        }

        if (!handled) {
            for (int i = 0; i < this.inventory.getSlots(); i++) {
                if (!this.inventory.getStackInSlot(i).isEmpty()) {
                    ItemStack stackInSlot = this.inventory.getStackInSlot(i);

                    if (!ItemStack.isSameItemSameTags(stackInSlot,  stack)) {
                        this.inventory.setStackInSlot(i, stack.copyWithCount(1));
                        if (!player.getAbilities().instabuild) player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);

                        if (!player.addItem(stackInSlot)) {
                            player.drop(stackInSlot, false);
                        }
                        handled = true;
                        break;
                    }

                }
            }
        }
        return handled;
    }

    public boolean handleTakeItem(Player player) {
        boolean handled = false;
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) {
                ItemStack stackInSlot = this.inventory.getStackInSlot(i).copyWithCount(1);

                this.inventory.setStackInSlot(i, ItemStack.EMPTY);

                if (!player.addItem(stackInSlot)) {
                    player.drop(stackInSlot, false);
                }
                handled = true;
                break;
            }
        }
        return handled;
    }

    public void dropInventory() {
        SimpleContainer inventory = new SimpleContainer(this.inventory.getSlots());
        for(int i = 0; i < this.inventory.getSlots(); i++) {
            inventory.setItem(i, this.inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public boolean isNotEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
