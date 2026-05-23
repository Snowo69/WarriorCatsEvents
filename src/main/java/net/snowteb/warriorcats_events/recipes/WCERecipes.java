package net.snowteb.warriorcats_events.recipes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class WCERecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, WarriorCatsEvents.MODID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, WarriorCatsEvents.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HerbsRecipe>> HERBS_SERIALIZER =
            SERIALIZERS.register("herbs", HerbsRecipeSerializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<HerbsRecipe>> HERBS =
            TYPES.register("herbs", () -> new RecipeType<>() {
                public String toString() {
                    return ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "herbs").toString();
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> COLLAR_RECIPE_SERIALIZER =
            SERIALIZERS.register("collar_recipe", () -> new SimpleCraftingRecipeSerializer<>(CollarRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>,RecipeSerializer<?>> HONEY_MOSSBALL_SERIALIZER =
            SERIALIZERS.register("honey_mossball_recipe", () -> new SimpleCraftingRecipeSerializer<>(HoneyMossBallRecipe::new));


    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
        TYPES.register(bus);
    }

}
