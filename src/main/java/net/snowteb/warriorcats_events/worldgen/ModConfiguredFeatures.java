package net.snowteb.warriorcats_events.worldgen;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.custom.LavenderPetalsBlock;
import net.snowteb.warriorcats_events.block.custom.PebblesBlock;

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
    public static final ResourceKey<ConfiguredFeature<?,?>> FEVERFEW_KEY = registerKey("feverfew");
    public static final ResourceKey<ConfiguredFeature<?,?>> JUNIPER_KEY = registerKey("juniper");
    public static final ResourceKey<ConfiguredFeature<?,?>> COMFREY_KEY = registerKey("comfrey");

    public static final ResourceKey<ConfiguredFeature<?,?>> LAVENDER_KEY = registerKey("lavender");

    public static final ResourceKey<ConfiguredFeature<?,?>> PEBBLES_KEY = registerKey("pebbles");

    public static final ResourceKey<ConfiguredFeature<?, ?>>
            WALL_GLOWROCK =
            ResourceKey.create(
                    Registries.CONFIGURED_FEATURE,
                    ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "glowrocks_config_feature")
            );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        context.register(
                WALL_GLOWROCK,
                new ConfiguredFeature<>(
                        ModFeatures.GLOWROCKS_FEATURE.get(),
                        NoneFeatureConfiguration.INSTANCE
                )
        );

        register(context, DOCK_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        10,
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
                        10,
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
                        10,
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
                        10,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.CHAMOMILEPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));
        register(context, DAISY_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        10,
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
                        7,
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
                        7,
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
                        7,
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

        register(context, FEVERFEW_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        7,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.FEVERFEWPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));

        register(context, JUNIPER_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        7,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.JUNIPERPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));

        register(context, COMFREY_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        7,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                ModBlocks.COMFREYPLANT.get()
                                                        .defaultBlockState()
                                                        .setValue(SweetBerryBushBlock.AGE, 3)
                                        )))));



        register(context, GLOWSHROOM_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        7,
                        7,
                        0,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(ModBlocks.GLOWSHROOM.get())))));


        register(context, LAVENDER_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        1800,
                        16,
                        2,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        new WeightedStateProvider(
                                                SimpleWeightedRandomList.<BlockState>builder()
                                                        .add(ModBlocks.LAVENDER.get().defaultBlockState(), 7)
                                                        .add(ModBlocks.LAVENDER_PETALS.get().defaultBlockState().setValue(LavenderPetalsBlock.AMOUNT, 1).setValue(LavenderPetalsBlock.FACING, Direction.EAST), 1)
                                                        .add(ModBlocks.LAVENDER_PETALS.get().defaultBlockState().setValue(LavenderPetalsBlock.AMOUNT, 2).setValue(LavenderPetalsBlock.FACING, Direction.WEST), 1)
                                                        .add(ModBlocks.LAVENDER_PETALS.get().defaultBlockState().setValue(LavenderPetalsBlock.AMOUNT, 3).setValue(LavenderPetalsBlock.FACING, Direction.SOUTH), 1)
                                                        .add(ModBlocks.LAVENDER_PETALS.get().defaultBlockState().setValue(LavenderPetalsBlock.AMOUNT, 4).setValue(LavenderPetalsBlock.FACING, Direction.NORTH), 1)
                                                        .add(Blocks.ALLIUM.defaultBlockState(), 1)
                                                        .build()
                                        )))));



        register(context, PEBBLES_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        15,
                        10,
                        2,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        new WeightedStateProvider(
                                                SimpleWeightedRandomList.<BlockState>builder()
                                                        .add(ModBlocks.PEBBLES.get().defaultBlockState().setValue(PebblesBlock.FACING, Direction.EAST), 1)
                                                        .add(ModBlocks.PEBBLES.get().defaultBlockState().setValue(PebblesBlock.FACING, Direction.WEST), 1)
                                                        .add(ModBlocks.PEBBLES.get().defaultBlockState().setValue(PebblesBlock.FACING, Direction.SOUTH), 1)
                                                        .add(ModBlocks.PEBBLES.get().defaultBlockState().setValue(PebblesBlock.FACING, Direction.NORTH), 1)
                                                        .build()
                                        )))));





    }






    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>>
    void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                  ResourceKey<ConfiguredFeature<?, ?>> key,
                  F feature,
                  FC configuration) {

        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
