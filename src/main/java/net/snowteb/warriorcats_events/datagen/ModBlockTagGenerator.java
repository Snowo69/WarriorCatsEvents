package net.snowteb.warriorcats_events.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, WarriorCatsEvents.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        /* this.tag(ModTags.Blocks.CUSTOMHERBBLOCKS)
                .add(ModBlocks.DEATHBERRIESBUSH.get()).addTag(Tags.Blocks.ORES); */


    }
}
