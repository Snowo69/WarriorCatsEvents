package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class DockBackpackHandler extends ItemStackHandler {

    private final ItemStack backpackStack;

    public DockBackpackHandler(ItemStack backpackStack) {
        super(27);
        this.backpackStack = backpackStack;
        deserializeNBT(backpackStack.getOrCreateTag().getCompound("Inventory"));
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        backpackStack.getOrCreateTag().put("Inventory", serializeNBT());
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof DockBackpackItem) return false;
        if (stack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof ShulkerBoxBlock) {
                return false;
            }
        }

        return super.isItemValid(slot, stack);
    }
}