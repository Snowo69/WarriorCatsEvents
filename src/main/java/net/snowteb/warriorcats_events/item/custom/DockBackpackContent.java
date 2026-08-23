package net.snowteb.warriorcats_events.item.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record DockBackpackContent(NonNullList<ItemStack> items) {

    public static final int SLOTS = 27;

    public static DockBackpackContent empty() {
        return new DockBackpackContent(NonNullList.withSize(SLOTS, ItemStack.EMPTY));
    }

    public static final Codec<DockBackpackContent> CODEC = ItemStack.OPTIONAL_CODEC
            .listOf()
            .xmap(DockBackpackContent::fromList, DockBackpackContent::toList);

    public static final StreamCodec<RegistryFriendlyByteBuf, DockBackpackContent> STREAM_CODEC =
            ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list())
                    .map(DockBackpackContent::fromList, DockBackpackContent::toList);

    private static DockBackpackContent fromList(List<ItemStack> list) {
        NonNullList<ItemStack> lst = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        for (int i = 0; i < Math.min(SLOTS, list.size()); i++) {
            lst.set(i, list.get(i));
        }
        return new DockBackpackContent(lst);
    }

    private List<ItemStack> toList() {
        return items;
    }

    public int getSlots() {
        return SLOTS;
    }

    public ItemStack getStackInSlot(int i) {
        try {
            return items.get(i);
        } catch (IndexOutOfBoundsException e) {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DockBackpackContent(NonNullList<ItemStack> items1))) return false;
        if (items().size() != items1.size()) return false;

        for (int i = 0; i < items().size(); i++) {
            if (!ItemStack.isSameItemSameComponents(items().get(i), items1.get(i))) return false;
            if (items().get(i).getCount() != items1.get(i).getCount()) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (ItemStack stack : items) {
            result = 31 * result + (stack.isEmpty() ? 0 :
                    ItemStack.hashItemAndComponents(stack) * 31 + stack.getCount());
        }
        return result;
    }
}
