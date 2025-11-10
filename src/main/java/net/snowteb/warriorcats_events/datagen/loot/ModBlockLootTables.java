package net.snowteb.warriorcats_events.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.custom.MossbedBlock;
import net.snowteb.warriorcats_events.init.WarriorCatsEventsBlocks;
import net.snowteb.warriorcats_events.item.ModItems;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.DOCK.get(),
                block -> createSingleItemTableWithSilkTouch(
                        block,
                        ModItems.DOCK_LEAVES.get(),
                        UniformGenerator.between(1.0F, 2.0F)
                )

            );
        this.add(ModBlocks.SORRELPLANT.get(),
                block -> createSingleItemTableWithSilkTouch(
                        block,
                        ModItems.SORREL.get(),
                        UniformGenerator.between(1.0F, 2.0F)
                )

        );
        this.add(ModBlocks.DAISYPLANT.get(),
                block -> createSingleItemTableWithSilkTouch(
                        block,
                        ModItems.DAISY.get(),
                        UniformGenerator.between(1.0F, 2.0F)
                )

        );
        this.add(ModBlocks.BURNETPLANT.get(),
                block -> createSingleItemTableWithSilkTouch(
                        block,
                        ModItems.BURNET.get(),
                        UniformGenerator.between(1.0F, 2.0F)
                )

        );
        this.add(ModBlocks.CHAMOMILEPLANT.get(),
                block -> createSingleItemTableWithSilkTouch(
                        block,
                        ModItems.CHAMOMILE.get(),
                        UniformGenerator.between(1.0F, 2.0F)
                )

        );
        this.add(WarriorCatsEventsBlocks.MOSSBED.get(),
                block -> createSingleItemTableWithSilkTouch(
                        block,
                        Items.FEATHER,
                        UniformGenerator.between(1.0F, 3.0F)
                )
        );


    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
