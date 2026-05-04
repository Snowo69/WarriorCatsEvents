package net.snowteb.warriorcats_events.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class HoneyMossBallRecipeSerializer implements RecipeSerializer<HoneyMossBallRecipe> {

    @Override
    public HoneyMossBallRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
        return new HoneyMossBallRecipe(resourceLocation, CraftingBookCategory.MISC);
    }

    @Override
    public HoneyMossBallRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        return new HoneyMossBallRecipe(resourceLocation, CraftingBookCategory.MISC);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, HoneyMossBallRecipe recipe) {
    }

}
