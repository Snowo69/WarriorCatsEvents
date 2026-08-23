package net.snowteb.warriorcats_events.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BraceletRecipeSerializer implements RecipeSerializer<BraceletRecipe> {

    @Override
    public BraceletRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
        return new BraceletRecipe(resourceLocation, CraftingBookCategory.MISC);
    }

    @Override
    public BraceletRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        return new BraceletRecipe(resourceLocation, CraftingBookCategory.MISC);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, BraceletRecipe recipe) {
    }

}
