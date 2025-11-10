package net.snowteb.warriorcats_events.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.item.ModItems;
import org.spongepowered.asm.util.IConsumer;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }



    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        /*ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TRAVELING_HERBS.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.CHAMOMILE.get())
                .unlockedBy(getHasName(ModItems.CHAMOMILE.get()), has(ModItems.CHAMOMILE.get()))
                .save(pWriter); */
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TRAVELING_HERBS.get(), 1)
                .requires(ModItems.SORREL.get())
                .requires(ModItems.BURNET.get())
                .requires(ModItems.DAISY.get())
                .requires(ModItems.CHAMOMILE.get())
                .requires(ModItems.DOCK_LEAVES.get())
                .unlockedBy(getHasName(ModItems.DOCK_LEAVES.get()), has(ModItems.DOCK_LEAVES.get()))
                .save(pWriter);
    }
}
