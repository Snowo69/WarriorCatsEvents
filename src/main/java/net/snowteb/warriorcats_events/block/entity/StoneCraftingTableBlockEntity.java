package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.snowteb.warriorcats_events.recipes.WCERecipes;
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", inventory.serializeNBT(registries));

    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
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

                    if (!ItemStack.isSameItemSameComponents(stackInSlot,  stack)) {
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

    private ItemStack recipeResult = ItemStack.EMPTY;
    private long lastCheck = 0;

    public ItemStack recipeResult() {
        if (level == null) return ItemStack.EMPTY;

        ItemStackHandler handler = new ItemStackHandler(5);

        List<ItemStack> items = getRenderStacks();

        for (int i = 0; i < items.size(); i++) {
            handler.setStackInSlot(i, items.get(i));
        }

        RecipeWrapper wrapper = new RecipeWrapper(handler);

        return level.getRecipeManager()
                .getAllRecipesFor(WCERecipes.HERBS.get())
                .stream()
                .filter(recipe -> recipe.value().matches(wrapper, level))
                .findFirst()
                .map(recipe -> recipe.value().assemble(wrapper, level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    public ItemStack getCurrentResult() {
        if (level == null) return ItemStack.EMPTY;

        if (level.getGameTime() - lastCheck > 3) {
            recipeResult = recipeResult();
            lastCheck = level.getGameTime();
        }

        return recipeResult;
    }
}
