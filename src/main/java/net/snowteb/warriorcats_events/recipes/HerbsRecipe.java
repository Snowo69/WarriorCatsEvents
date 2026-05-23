package net.snowteb.warriorcats_events.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HerbsRecipe implements Recipe<RecipeWrapper> {

    public final NonNullList<Ingredient> ingredients;
    public final ItemStack result;

    public HerbsRecipe(NonNullList<Ingredient> ingredients, ItemStack result) {
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        List<ItemStack> inputs = new ArrayList<>();


        for (int i = 0; i < pContainer.size(); i++) {
            ItemStack stack = pContainer.getItem(i);
            if (!stack.isEmpty()) {
                inputs.add(stack.copy());
            }
        }

        if (inputs.size() != ingredients.size()) {
            return false;
        }

        List<Ingredient> requiredIngredients = new ArrayList<>(ingredients);

        requiredIngredients.sort((a, b) -> {
            boolean aSpecific = a.getItems().length == 1;
            boolean bSpecific = b.getItems().length == 1;
            return Boolean.compare(!aSpecific, !bSpecific);
        });

        for (ItemStack input : inputs) {
            boolean matched = false;

            Iterator<Ingredient> iterator = requiredIngredients.iterator();
            while (iterator.hasNext()) {
                Ingredient ingredient = iterator.next();

                if (ingredient.test(input)) {
                    iterator.remove();
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                return false;
            }
        }

        return requiredIngredients.isEmpty();
    }

    @Override
    public ItemStack assemble(RecipeWrapper recipeWrapper, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WCERecipes.HERBS_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return WCERecipes.HERBS.get();
    }
}
