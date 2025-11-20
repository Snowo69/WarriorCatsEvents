package net.snowteb.warriorcats_events.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
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
                .pattern("BCB")
                .pattern("ABA")
                .define('A', ItemTags.SAPLINGS)
                .define('B', Items.COBBLESTONE)
                .define('C', Items.AIR)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TRAVELING_HERBS.get(), 1)
                .requires(ModItems.SORREL.get())
                .requires(ModItems.BURNET.get())
                .requires(ModItems.DAISY.get())
                .requires(ModItems.CHAMOMILE.get())
                .requires(ModItems.DOCK_LEAVES.get())
                .unlockedBy(getHasName(ModItems.DOCK_LEAVES.get()), has(ModItems.DOCK_LEAVES.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.WARRIORNAMERANDOMIZER.get(), 8)
                .requires(ModTags.Items.HERBS)
                .requires(ModTags.Items.HERBS)
                .requires(ModTags.Items.HERBS)
                .requires(Items.SUGAR_CANE)
                .requires(ItemTags.SAPLINGS)
                .unlockedBy(getHasName(ModItems.DOCK_LEAVES.get()), has(ModItems.DOCK_LEAVES.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.WHISKERS.get(), 8)
                .requires(ItemTags.SAPLINGS)
                .requires(Items.WHEAT_SEEDS)
                .requires(ModTags.Items.HERBS)
                .requires(Items.LEATHER)
                .unlockedBy(getHasName(Items.WHEAT_SEEDS), has(Items.WHEAT_SEEDS))
                .save(pWriter);

    }
}
