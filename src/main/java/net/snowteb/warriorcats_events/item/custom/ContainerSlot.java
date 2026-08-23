package net.snowteb.warriorcats_events.item.custom;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerSlot extends Slot {

    public ContainerSlot(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return this.container.canPlaceItem(this.getContainerSlot(), stack);
    }
}
