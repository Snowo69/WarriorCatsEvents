package net.snowteb.warriorcats_events.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.item.custom.BraceletArmorItem;
import org.jetbrains.annotations.NotNull;

public class BraceletRecipe extends CustomRecipe {

    public BraceletRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {

        boolean hasBracelet = false;

        boolean hasSpikes = false;
        boolean hasGlow = false;

        boolean alreadyHasSpikes = false;
        boolean alreadyHasGlow = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof BraceletArmorItem braceletArmorItem) {
                if (hasBracelet) return false;
                if (braceletArmorItem.hasSpikes(stack)) alreadyHasSpikes = true;
                if (braceletArmorItem.hasGlow(stack)) alreadyHasGlow = true;
                hasBracelet = true;
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

        return hasBracelet && (hasSpikes || hasGlow);
    }

    @Override
    public ItemStack assemble(CraftingContainer table, RegistryAccess access) {
        ItemStack bracelet = ItemStack.EMPTY;

        boolean hasSpikes = false;
        boolean hasGlow = false;

        boolean alreadyHasSpikes = false;
        boolean alreadyHasGlow = false;

        for (int i = 0; i < table.getContainerSize(); i++) {
            ItemStack stack = table.getItem(i);

            if (stack.getItem() instanceof BraceletArmorItem braceletArmorItem) {
                bracelet = stack.copy();

                if (braceletArmorItem.hasSpikes(stack)) alreadyHasSpikes = true;
                if (braceletArmorItem.hasGlow(stack)) alreadyHasGlow = true;
            }

            if (stack.is(Items.IRON_INGOT) && !alreadyHasSpikes) hasSpikes = true;
            if ((stack.is(ModItems.GLOW_SHROOM.get()) || stack.is(Items.GLOW_BERRIES) ||
                    stack.is(Items.GLOWSTONE_DUST) || stack.is(Items.GLOW_INK_SAC))
                    && !alreadyHasGlow) hasGlow = true;
        }

        if (!bracelet.isEmpty()) {
            CompoundTag tag = bracelet.getOrCreateTag();

            if (hasSpikes) {
                tag.putBoolean("HasSpikes", true);
            }

            if (hasGlow) {
                tag.putBoolean("HasGlow", true);
            }
        }

        return bracelet;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WCERecipes.BRACELET_RECIPE_SERIALIZER.get();
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return new ItemStack(ModItems.CAT_BRACELET.get());
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ModItems.CAT_BRACELET.get()));
        ingredients.add(Ingredient.of(
                Items.IRON_INGOT,
                ModItems.GLOW_SHROOM.get(), Items.GLOW_BERRIES,
                Items.GLOWSTONE_DUST, Items.GLOW_INK_SAC
        ));
        return ingredients;
    }

}

