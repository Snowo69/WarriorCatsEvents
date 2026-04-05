package net.snowteb.warriorcats_events.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.item.custom.CollarArmorItem;

public class CollarRecipe extends CustomRecipe {

    public CollarRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {

        boolean hasCollar = false;

        boolean hasBell = false;
        boolean hasSpikes = false;
        boolean hasGlow = false;

        boolean alreadyHasBell = false;
        boolean alreadyHasSpikes = false;
        boolean alreadyHasGlow = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof CollarArmorItem collar) {
                if (hasCollar) return false;
                if (collar.hasSpikes(stack)) alreadyHasSpikes = true;
                if (collar.hasBell(stack)) alreadyHasBell = true;
                if (collar.hasGlow(stack)) alreadyHasGlow = true;
                hasCollar = true;
            } else if ((stack.is(ModItems.COLLAR_BELL.get()) || stack.is(Items.BELL)) && !alreadyHasBell) {
                if (hasBell) return false;
                hasBell = true;
            } else if (stack.is(Items.IRON_INGOT) && !alreadyHasSpikes) {
                if (hasSpikes) return false;
                hasSpikes = true;
            }else if ((stack.is(ModItems.GLOW_SHROOM.get()) || stack.is(Items.GLOW_BERRIES) ||
                    stack.is(Items.GLOWSTONE_DUST) || stack.is(Items.GLOW_INK_SAC))
                    && !alreadyHasGlow) {
                if (hasGlow) return false;
                hasGlow = true;
            } else {
                return false;
            }
        }

        return hasCollar && (hasBell || hasSpikes || hasGlow);
    }

    @Override
    public ItemStack assemble(CraftingContainer table, RegistryAccess access) {
        ItemStack collar = ItemStack.EMPTY;

        boolean hasBell = false;
        boolean hasSpikes = false;
        boolean hasGlow = false;

        boolean alreadyHasBell = false;
        boolean alreadyHasSpikes = false;
        boolean alreadyHasGlow = false;

        for (int i = 0; i < table.getContainerSize(); i++) {
            ItemStack stack = table.getItem(i);

            if (stack.getItem() instanceof CollarArmorItem collarItem) {
                collar = stack.copy();

                if (collarItem.hasSpikes(stack)) alreadyHasSpikes = true;
                if (collarItem.hasBell(stack)) alreadyHasBell = true;
                if (collarItem.hasGlow(stack)) alreadyHasGlow = true;
            }

            if ((stack.is(ModItems.COLLAR_BELL.get()) || stack.is(Items.BELL)) && !alreadyHasBell) hasBell = true;
            if (stack.is(Items.IRON_INGOT) && !alreadyHasSpikes) hasSpikes = true;
            if ((stack.is(ModItems.GLOW_SHROOM.get()) || stack.is(Items.GLOW_BERRIES) ||
                    stack.is(Items.GLOWSTONE_DUST) || stack.is(Items.GLOW_INK_SAC))
                    && !alreadyHasGlow) hasGlow = true;
        }

        if (!collar.isEmpty()) {
            CompoundTag tag = collar.getOrCreateTag();

            if (hasBell) {
                tag.putBoolean("HasBell", true);
            }

            if (hasSpikes) {
                tag.putBoolean("HasSpikes", true);
            }

            if (hasGlow) {
                tag.putBoolean("HasGlow", true);
            }
        }

        return collar;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WCERecipes.COLLAR_RECIPE_SERIALIZER.get();
    }

}

