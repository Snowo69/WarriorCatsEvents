package net.snowteb.warriorcats_events.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.custom.GenericBushBlock;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?,?>> DOCK_KEY = registerKey("dock");
    public static final ResourceKey<ConfiguredFeature<?,?>> SORREL_KEY = registerKey("sorrel");
    public static final ResourceKey<ConfiguredFeature<?,?>> BURNET_KEY = registerKey("burnet");
    public static final ResourceKey<ConfiguredFeature<?,?>> CHAMOMILE_KEY = registerKey("chamomile");
    public static final ResourceKey<ConfiguredFeature<?,?>> DAISY_KEY = registerKey("daisy");
    public static final ResourceKey<ConfiguredFeature<?,?>> DEATHBERRIES_KEY = registerKey("deathberries");
    public static final ResourceKey<ConfiguredFeature<?,?>> CATMINT_KEY = registerKey("catmint");
    public static final ResourceKey<ConfiguredFeature<?,?>> GLOWSHROOM_KEY = registerKey("glowshroom");
    public static final ResourceKey<ConfiguredFeature<?,?>> YARROW_KEY = registerKey("yarrow");

    public static final ResourceKey<ConfiguredFeature<?,?>> DARKTREE_KEY = registerKey("darktree_key");
    public static final ResourceKey<ConfiguredFeature<?,?>> STARRYTREE_KEY = registerKey("starrytree_key");


    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

        /*
        register(context, DOCK_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.DOCK.get()
                                                .defaultBlockState()
                                                .setValue(SweetBerryBushBlock.AGE, 3)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK)));

        register(context, SORREL_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.SORRELPLANT.get()
                                                .defaultBlockState()
                                                .setValue(SweetBerryBushBlock.AGE, 3)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK)));

        register(context, BURNET_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.BURNETPLANT.get()
                                                .defaultBlockState()
                                                .setValue(SweetBerryBushBlock.AGE, 3)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK)));

        register(context, CHAMOMILE_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.CHAMOMILEPLANT.get()
                                                .defaultBlockState()
                                                .setValue(SweetBerryBushBlock.AGE, 3)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK)));

        register(context, DAISY_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.DAISYPLANT.get()
                                                .defaultBlockState()
                                                .setValue(SweetBerryBushBlock.AGE, 3)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK)));

        register(context, DEATHBERRIES_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.DEATHBERRIESBUSH.get()
                                                .defaultBlockState()
                                                .setValue(SweetBerryBushBlock.AGE, 3)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK)));

        register(context, CATMINT_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.CATMINTPLANT.get()
                                                .defaultBlockState()
                                                .setValue(SweetBerryBushBlock.AGE, 3)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK)));

        register(context, YARROW_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(
                                BlockStateProvider.simple(
                                        ModBlocks.YARROWPLANT.get()
                                                .defaultBlockState()
                                                .setValue(SweetBerryBushBlock.AGE, 3)
                                )
                        ),
                        List.of(Blocks.GRASS_BLOCK)));

         */


        register(context, DOCK_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        7,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.DOCK.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));
        register(context, SORREL_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        7,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.SORRELPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));
        register(context, BURNET_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        7,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.BURNETPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));
        register(context, CHAMOMILE_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        7,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.DAISYPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));
        register(context, DAISY_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        7,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.DAISYPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));
        register(context, DEATHBERRIES_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        5,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.DEATHBERRIESBUSH.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));
        register(context, CATMINT_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        5,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.CATMINTPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));
        register(context, YARROW_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        5,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.YARROWPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));



        register(context, GLOWSHROOM_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        5,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(ModBlocks.GLOWSHROOM.get())))));








        register(context, DARKTREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.DARK_LOG.get()),
                new ForkingTrunkPlacer(
                        3,
                        3,
                        6
                ),
                BlockStateProvider.simple(Blocks.AIR),
                new BlobFoliagePlacer(ConstantInt.of(3), ConstantInt.of(2), 3),
                new TwoLayersFeatureSize(1,0,2)).build());

    register(context, STARRYTREE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(ModBlocks.STARRY_LOG.get()),
            new FancyTrunkPlacer(13,2,4),
            BlockStateProvider.simple(ModBlocks.STARRY_LEAVES.get()),
            new PineFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(4)),
            new TwoLayersFeatureSize(1,0,2)).build());






    }






    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                new ResourceLocation(WarriorCatsEvents.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>>
    void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                  ResourceKey<ConfiguredFeature<?, ?>> key,
                  F feature,
                  FC configuration) {

        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
