package net.snowteb.warriorcats_events.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.integration.CompatibilityTags;
import net.snowteb.warriorcats_events.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, WarriorCatsEvents.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        this.tag(ItemTags.MUSIC_DISCS).add(ModItems.GENERATIONS_MUSIC_DISC.get());
        this.tag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(ModItems.GENERATIONS_MUSIC_DISC.get());

        this.tag(CompatibilityTags.SERENE_SEASONS_SPRING_CROPS).add(ModItems.DOCK_LEAVES.get(), ModItems.SORREL.get(), ModItems.BURNET.get(), ModItems.DAISY.get());
        this.tag(CompatibilityTags.SERENE_SEASONS_SUMMER_CROPS).add(ModItems.CHAMOMILE.get(), ModItems.CATMINT.get(), ModItems.YARROW.get());
        this.tag(CompatibilityTags.SERENE_SEASONS_AUTUMN_CROPS).add(ModItems.DEATHBERRIES.get());

    }
}
