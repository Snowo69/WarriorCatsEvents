package net.snowteb.warriorcats_events.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CollarRecipeSerializer implements RecipeSerializer<CollarRecipe> {

    @Override
    public CollarRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
        return new CollarRecipe(resourceLocation, CraftingBookCategory.MISC);
    }

    @Override
    public CollarRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        return new CollarRecipe(resourceLocation, CraftingBookCategory.MISC);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, CollarRecipe recipe) {
    }

}
