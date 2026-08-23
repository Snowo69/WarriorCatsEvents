package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.snowteb.warriorcats_events.datacomponents.ModDataComponents;
import org.jetbrains.annotations.NotNull;

public class DockBackpackContainer extends SimpleContainer {

    private final ItemStack backpackStack;

    public DockBackpackContainer(ItemStack backpackStack) {
        super(DockBackpackContent.SLOTS);
        this.backpackStack = backpackStack;

        DockBackpackContent contents = backpackStack.getOrDefault(
                ModDataComponents.BACKPACK_CONTENTS, DockBackpackContent.empty());
        for (int i = 0; i < DockBackpackContent.SLOTS; i++) {
            this.setItem(i, contents.items().get(i));
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        NonNullList<ItemStack> copy = NonNullList.withSize(DockBackpackContent.SLOTS, ItemStack.EMPTY);
        for (int i = 0; i < DockBackpackContent.SLOTS; i++) {
            copy.set(i, this.getItem(i).copy());
        }
        backpackStack.set(ModDataComponents.BACKPACK_CONTENTS, new DockBackpackContent(copy));
    }

    @Override
    public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        if (!isItemValid(stack)) return false;

        return super.canPlaceItem(slot, stack);
    }

    public boolean isItemValid(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof DockBackpackItem) return false;
        if (stack.getItem() instanceof BlockItem blockItem) {
            return !(blockItem.getBlock() instanceof ShulkerBoxBlock);
        }

        return true;
    }
}