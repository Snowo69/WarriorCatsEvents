package net.snowteb.warriorcats_events.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HerbsRecipe implements Recipe<RecipeWrapper> {

    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    public HerbsRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
    }


    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        List<ItemStack> inputs = new ArrayList<>();


        for (int i = 0; i < pContainer.getContainerSize(); i++) {
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
    public ItemStack assemble(RecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
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
