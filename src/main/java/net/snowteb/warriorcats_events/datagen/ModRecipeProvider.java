package net.snowteb.warriorcats_events.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.util.ModTags;
import org.spongepowered.asm.util.IConsumer;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }



    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STONECLEFT.get())
                .pattern("ABA")
                .pattern("B B")
                .pattern("ABA")
                .define('A', ItemTags.SAPLINGS)
                .define('B', Items.COBBLESTONE)
                .unlockedBy("has_item", has(Items.AIR))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TRAVELING_HERBS.get(), 1)
                .requires(ModItems.SORREL.get())
                .requires(ModItems.BURNET.get())
                .requires(ModItems.DAISY.get())
                .requires(ModItems.CHAMOMILE.get())
                .requires(ModItems.DOCK_LEAVES.get())
                .unlockedBy(getHasName(ModItems.DOCK_LEAVES.get()), has(ModItems.DOCK_LEAVES.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.WARRIORNAMERANDOMIZER.get(), 1)
                .requires(ModTags.Items.HERBS)
                .requires(ModTags.Items.HERBS)
                .requires(ModTags.Items.HERBS)
                .requires(Items.SUGAR_CANE)
                .requires(ItemTags.SAPLINGS)
                .unlockedBy("has_item", has(Items.AIR))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.WHISKERS.get(), 1)
                .requires(ItemTags.SAPLINGS)
                .requires(Items.WHEAT_SEEDS)
                .requires(ModTags.Items.HERBS)
                .requires(Items.LEATHER)
                .unlockedBy("has_item", has(Items.AIR))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CLAWS.get(), 1)
                .requires(ItemTags.LOGS)
                .requires(Items.LEATHER)
                .unlockedBy("has_item", has(Items.AIR))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STARCLAN_KNOWLEDGE.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', ModItems.TRAVELING_HERBS.get())
                .define('B', Items.SUGAR_CANE)
                .define('C', ModItems.WHISKERS.get())
                .unlockedBy(getHasName(Items.SUGAR_CANE), has(Items.SUGAR_CANE))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FRESHKILL_AND_HERBS_BUNDLE.get(), 2)
                .requires(ModTags.Items.HERBS)
                .requires(ModTags.Items.PREY)
                .requires(ModTags.Items.HERBS)
                .requires(ModTags.Items.PREY)
                .requires(ModItems.DOCK_LEAVES.get())
                .unlockedBy("has_item", has(Items.AIR))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DOCK_POULTICE.get(), 1)
                .requires(ModItems.DOCK_LEAVES.get())
                .unlockedBy("has_item", has(Items.AIR))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.LEAF_DOOR.get(), 2)
                .pattern("AA")
                .pattern("AA")
                .pattern("AA")
                .define('A', ItemTags.LEAVES)
                .unlockedBy("has_item", has(Items.AIR))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MOSSBED.get(), 2)
                .pattern("   ")
                .pattern("BDB")
                .pattern("CBC")
                .define('B', ItemTags.LEAVES)
                .define('C', Items.FEATHER)
                .define('D', Items.GRASS)
                .unlockedBy("has_item", has(Items.AIR))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WARRIORS_GUIDE.get(), 1)
                .pattern(" A ")
                .pattern("CBC")
                .pattern(" A ")
                .define('A', ModTags.Items.HERBS)
                .define('B', Items.LEATHER)
                .define('C', ItemTags.LEAVES)
                .unlockedBy("has_item", has(Items.AIR))
                .save(pWriter);


    }
}
