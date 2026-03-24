package net.snowteb.warriorcats_events.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.Nullable;

public class HerbsRecipeSerializer implements RecipeSerializer<HerbsRecipe> {

    @Override
    public HerbsRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        JsonArray ingredientsArray = pSerializedRecipe.getAsJsonArray("ingredients");
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientsArray.size(), Ingredient.EMPTY);

        for (int i = 0; i < ingredientsArray.size(); i++) {
            ingredients.set(i, Ingredient.fromJson(ingredientsArray.get(i)));
        }

        ItemStack result = ShapedRecipe.itemStackFromJson(pSerializedRecipe.getAsJsonObject("result"));

        return new HerbsRecipe(pRecipeId, ingredients, result);
    }

    @Override
    public @Nullable HerbsRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        int size = pBuffer.readInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);

        for (int i = 0; i < size; i++) {
            ingredients.set(i, Ingredient.fromNetwork(pBuffer));
        }

        ItemStack result = pBuffer.readItem();

        return new HerbsRecipe(pRecipeId, ingredients, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, HerbsRecipe pRecipe) {
        pBuffer.writeInt(pRecipe.getIngredients().size());

        for (Ingredient ing : pRecipe.getIngredients()) {
            ing.toNetwork(pBuffer);
        }

        pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
    }
}