package net.snowteb.warriorcats_events.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class WCERecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, WarriorCatsEvents.MODID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, WarriorCatsEvents.MODID);

    public static final RegistryObject<RecipeSerializer<HerbsRecipe>> HERBS_SERIALIZER =
            SERIALIZERS.register("herbs", HerbsRecipeSerializer::new);

    public static final RegistryObject<RecipeType<HerbsRecipe>> HERBS =
            TYPES.register("herbs", () -> new RecipeType<>() {
                public String toString() {
                    return new ResourceLocation(WarriorCatsEvents.MODID, "herbs").toString();
                }
            });

    public static final RegistryObject<RecipeSerializer<?>> COLLAR_RECIPE_SERIALIZER =
            SERIALIZERS.register("collar_recipe", CollarRecipeSerializer::new);


    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
        TYPES.register(bus);
    }

}
