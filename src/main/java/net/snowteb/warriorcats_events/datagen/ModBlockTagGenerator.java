package net.snowteb.warriorcats_events.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.integration.CompatibilityTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, WarriorCatsEvents.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        this.tag(BlockTags.DOORS).add(ModBlocks.LEAF_DOOR.get());
        this.tag(BlockTags.TRAPDOORS).add(ModBlocks.LEAF_TRAPDOOR.get());

        this.tag(BlockTags.FLOWERS).add(ModBlocks.LAVENDER.get());
        this.tag(BlockTags.TALL_FLOWERS).add(ModBlocks.LAVENDER.get());
        this.tag(BlockTags.SMALL_FLOWERS).add(ModBlocks.LAVENDER_PETALS.get());


        this.tag(BlockTags.BEDS).add(ModBlocks.MOSS_BED.get(), ModBlocks.HAY_BED.get());

        this.tag(CompatibilityTags.SERENE_SEASONS_SPRING_CROPS_BLOCK).add(ModBlocks.DOCK.get(), ModBlocks.SORRELPLANT.get(), ModBlocks.BURNETPLANT.get(), ModBlocks.DAISYPLANT.get());
        this.tag(CompatibilityTags.SERENE_SEASONS_SUMMER_CROPS_BLOCK).add(ModBlocks.CHAMOMILEPLANT.get(), ModBlocks.CATMINTPLANT.get(), ModBlocks.YARROWPLANT.get());
        this.tag(CompatibilityTags.SERENE_SEASONS_AUTUMN_CROPS_BLOCK).add(ModBlocks.DEATHBERRIESBUSH.get());

    }
}
