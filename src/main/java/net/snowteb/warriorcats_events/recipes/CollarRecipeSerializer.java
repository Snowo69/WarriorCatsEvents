//package net.snowteb.warriorcats_events.recipes;
//
//import com.mojang.serialization.MapCodec;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//
//public class CollarRecipeSerializer implements RecipeSerializer<CollarRecipe> {
//
//    public static final StreamCodec<RegistryFriendlyByteBuf, CollarRecipe> STREAM_CODEC =
//            StreamCodec.unit(CollarRecipe::new);
//
//    public static final MapCodec<CollarRecipe> CODEC =
//            MapCodec.unit(new CollarRecipe());
//
//    @Override
//    public MapCodec<CollarRecipe> codec() {
//        return CODEC;
//    }
//
//    @Override
//    public StreamCodec<RegistryFriendlyByteBuf, CollarRecipe> streamCodec() {
//        return STREAM_CODEC;
//    }
//}
