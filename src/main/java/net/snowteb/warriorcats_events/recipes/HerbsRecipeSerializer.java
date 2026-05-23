package net.snowteb.warriorcats_events.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;

public class HerbsRecipeSerializer implements RecipeSerializer<HerbsRecipe> {


    public static final MapCodec<HerbsRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC_NONEMPTY
                            .listOf()
                            .fieldOf("ingredients")
                            .xmap(
                                    list -> {
                                        NonNullList<Ingredient> nll = NonNullList.create();
                                        nll.addAll(list);
                                        return nll;
                                    },
                                    list -> (List<Ingredient>) list
                            )
                            .forGetter(HerbsRecipe::getIngredients),
                    ItemStack.STRICT_CODEC
                            .fieldOf("result")
                            .forGetter(r -> r.result)
            ).apply(instance, HerbsRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, HerbsRecipe> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public HerbsRecipe decode(RegistryFriendlyByteBuf buf) {
                    int size = buf.readVarInt();
                    NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
                    for (int i = 0; i < size; i++) {
                        ingredients.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                    }
                    ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                    return new HerbsRecipe(ingredients, result);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, HerbsRecipe recipe) {
                    buf.writeVarInt(recipe.getIngredients().size());
                    for (Ingredient ing : recipe.getIngredients()) {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
                    }
                    ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                }
            };

    @Override
    public MapCodec<HerbsRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, HerbsRecipe> streamCodec() {
        return STREAM_CODEC;
    }

//    @Override
//    public HerbsRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
//        JsonArray ingredientsArray = pSerializedRecipe.getAsJsonArray("ingredients");
//        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientsArray.size(), Ingredient.EMPTY);
//
//        for (int i = 0; i < ingredientsArray.size(); i++) {
//            ingredients.set(i, Ingredient.fromJson(ingredientsArray.get(i)));
//        }
//
//        JsonObject resultJson = pSerializedRecipe.getAsJsonObject("result");
//        ItemStack result = ShapedRecipe.itemStackFromJson(resultJson);
//
//        if (resultJson.has("with")) {
//            JsonObject with = resultJson.getAsJsonObject("with");
//            CompoundTag tag = result.getOrCreateTag();
//
//            for (var entry : with.entrySet()) {
//                String key = entry.getKey();
//                var value = entry.getValue();
//
//                if (value.isJsonPrimitive()) {
//                    var prim = value.getAsJsonPrimitive();
//
//                    if (prim.isNumber()) {
//                        tag.putInt(key, prim.getAsInt());
//                    } else if (prim.isBoolean()) {
//                        tag.putBoolean(key, prim.getAsBoolean());
//                    } else if (prim.isString()) {
//                        tag.putString(key, prim.getAsString());
//                    }
//                }
//            }
//        }
//
//        return new HerbsRecipe(ingredients, result);
//    }
//
//    @Override
//    public @Nullable HerbsRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
//        int size = pBuffer.readInt();
//        NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
//
//        for (int i = 0; i < size; i++) {
//            ingredients.set(i, Ingredient.fromNetwork(pBuffer));
//        }
//
//        ItemStack result = pBuffer.readItem();
//
//        return new HerbsRecipe(ingredients, result);
//    }
//
//    @Override
//    public void toNetwork(FriendlyByteBuf pBuffer, HerbsRecipe pRecipe) {
//        pBuffer.writeInt(pRecipe.getIngredients().size());
//
//        for (Ingredient ing : pRecipe.getIngredients()) {
//            ing.toNetwork(pBuffer);
//        }
//
//        pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
//    }
}