package net.snowteb.warriorcats_events.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.datacomponents.ModDataComponents;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.item.custom.CollarArmorItem;

public class CollarRecipe extends CustomRecipe {

    public CollarRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        boolean hasCollar = false;

        boolean hasBell = false;
        boolean hasSpikes = false;
        boolean hasGlow = false;

        boolean alreadyHasBell = false;
        boolean alreadyHasSpikes = false;
        boolean alreadyHasGlow = false;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);
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
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        ItemStack collar = ItemStack.EMPTY;

        boolean hasBell = false;
        boolean hasSpikes = false;
        boolean hasGlow = false;

        boolean alreadyHasBell = false;
        boolean alreadyHasSpikes = false;
        boolean alreadyHasGlow = false;

        for (int i = 0; i < craftingInput.size(); i++) {
            ItemStack stack = craftingInput.getItem(i);

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

            if (hasBell) {
                collar.set(ModDataComponents.HAS_BELL, true);
            }

            if (hasSpikes) {
                collar.set(ModDataComponents.HAS_SPIKES, true);

            }

            if (hasGlow) {
                collar.set(ModDataComponents.HAS_GLOW, true);
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

