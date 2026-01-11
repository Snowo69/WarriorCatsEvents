package net.snowteb.warriorcats_events.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.snowteb.warriorcats_events.block.ModBlocks;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> DOCK_PLACED_KEY = registerKey("dock_placed");
    public static final ResourceKey<PlacedFeature> SORREL_PLACED_KEY = registerKey("sorrel_placed");
    public static final ResourceKey<PlacedFeature> BURNET_PLACED_KEY = registerKey("burnet_placed");
    public static final ResourceKey<PlacedFeature> CHAMOMILE_PLACED_KEY = registerKey("chamomile_placed");
    public static final ResourceKey<PlacedFeature> DAISY_PLACED_KEY = registerKey("daisy_placed");
    public static final ResourceKey<PlacedFeature> DEATHBERRIES_PLACED_KEY = registerKey("deathberries_placed");
    public static final ResourceKey<PlacedFeature> CATMINT_PLACED_KEY = registerKey("catmint_placed");
    public static final ResourceKey<PlacedFeature> GLOWSHROOM_PLACED_KEY = registerKey("glowshroom_placed");
    public static final ResourceKey<PlacedFeature> YARROW_PLACED_KEY = registerKey("yarrow_placed");

    public static final ResourceKey<PlacedFeature> STARRYTREE_PLACED_KEY = registerKey("starrytree_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, DOCK_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DOCK_KEY),
                List.of(RarityFilter.onAverageOnceEvery(65), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, SORREL_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SORREL_KEY),
                List.of(RarityFilter.onAverageOnceEvery(80), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, BURNET_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BURNET_KEY),
                List.of(RarityFilter.onAverageOnceEvery(80), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, CHAMOMILE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CHAMOMILE_KEY),
                List.of(RarityFilter.onAverageOnceEvery(80), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, DAISY_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DAISY_KEY),
                List.of(RarityFilter.onAverageOnceEvery(80), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, YARROW_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.YARROW_KEY),
                List.of(RarityFilter.onAverageOnceEvery(80), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, DEATHBERRIES_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DEATHBERRIES_KEY),
                List.of(RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, CATMINT_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CATMINT_KEY),
                List.of(RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, GLOWSHROOM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GLOWSHROOM_KEY),
                List.of(RarityFilter.onAverageOnceEvery(90), InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));




//                register(context, STARRYTREE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.STARRYTREE_KEY),
//                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2),
//                        ModBlocks.STARRYTREE_SAPLING.get()));


    }


    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(WarriorCatsEvents.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
