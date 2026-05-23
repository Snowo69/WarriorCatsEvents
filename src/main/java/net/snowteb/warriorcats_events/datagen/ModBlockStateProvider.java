package net.snowteb.warriorcats_events.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;



public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, WarriorCatsEvents.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {


        makeBush(((SweetBerryBushBlock) ModBlocks.DOCK.get()), "dock_stage", "dock_stage");
        makeBush(((SweetBerryBushBlock) ModBlocks.SORRELPLANT.get()), "sorrel_stage", "sorrel_stage");
        makeBush(((SweetBerryBushBlock) ModBlocks.BURNETPLANT.get()), "burnet_stage", "burnet_stage");
        makeBush(((SweetBerryBushBlock) ModBlocks.CHAMOMILEPLANT.get()), "chamomile_stage", "chamomile_stage");
        makeBush(((SweetBerryBushBlock) ModBlocks.DAISYPLANT.get()), "daisy_stage", "daisy_stage");
        makeBush(((SweetBerryBushBlock) ModBlocks.DEATHBERRIESBUSH.get()), "deathberries_stage", "deathberries_stage");
        makeBush(((SweetBerryBushBlock) ModBlocks.CATMINTPLANT.get()), "catmint_stage", "catmint_stage");
        makeBush(((SweetBerryBushBlock) ModBlocks.YARROWPLANT.get()), "yarrow_stage", "yarrow_stage");
        makeBush(((SweetBerryBushBlock) ModBlocks.FEVERFEWPLANT.get()), "feverfew_stage", "feverfew_stage");
        makeBigBush(((SweetBerryBushBlock) ModBlocks.JUNIPERPLANT.get()), "juniper_stage", "juniper_stage");
        makeBush(((SweetBerryBushBlock) ModBlocks.COMFREYPLANT.get()), "comfrey_stage", "comfrey_stage");


//        saplingBlock(ModBlocks.STARRYTREE_SAPLING);
//        saplingBlock(ModBlocks.DARKTREE_SAPLING);

        horizontalBlock(ModBlocks.STONECLEFT.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/stone_cleft")));

//        logBlock(((RotatedPillarBlock) ModBlocks.DARK_LOG.get()));
//        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_DARK_LOG.get()),
//                blockTexture(ModBlocks.STRIPPED_DARK_LOG.get()),
//                ResourceLocation.fromNamespaceAndPath(MODID, "block/stripped_dark_log_top"));
//
//        logBlock(((RotatedPillarBlock) ModBlocks.STARRY_LOG.get()));
//        axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_STARRY_LOG.get()),
//                blockTexture(ModBlocks.STRIPPED_STARRY_LOG.get()),
//                ResourceLocation.fromNamespaceAndPath(MODID, "block/stripped_starry_log_top"));
//
//
//        blockItem(ModBlocks.DARK_LOG);
//        blockItem(ModBlocks.STRIPPED_DARK_LOG);
//        blockItem(ModBlocks.STARRY_LOG);
//        blockItem(ModBlocks.STRIPPED_STARRY_LOG);
//
//        leavesBlock(ModBlocks.STARRY_LEAVES);

    }

    private void blockItem(DeferredBlock<?> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(WarriorCatsEvents.MODID +
                ":block/" + BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath()));
    }
    private void blockWithItem(DeferredBlock<?> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    private void leavesBlock(DeferredBlock<?> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), ResourceLocation.fromNamespaceAndPath("minecraft","block/leaves"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }
    public void makeBush(SweetBerryBushBlock block, String modelBaseName, String textureBaseName) {

        getVariantBuilder(block).forAllStates(state -> {

            int age = state.getValue(SweetBerryBushBlock.AGE);
            String texPath = "block/" + textureBaseName + age;

            return new ConfiguredModel[]{
                    new ConfiguredModel(
                            models().cross(
                                    modelBaseName + age,
                                    ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, texPath)
                            ).renderType("cutout")
                    )
            };
        });
    }

    public void makeBigBush(SweetBerryBushBlock block, String modelBaseName, String textureBaseName) {

        getVariantBuilder(block).forAllStates(state -> {

            int age = state.getValue(SweetBerryBushBlock.AGE);

            String texPath = "block/" + textureBaseName + age;

            return new ConfiguredModel[]{
                    new ConfiguredModel(
                            models().withExistingParent(
                                    modelBaseName + age,
                                    modLoc("custom/big_cross")
                            ).texture("cross", modLoc(texPath)).renderType("cutout")
                    )
            };
        });
    }

    private void saplingBlock(DeferredBlock<?> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().cross(BuiltInRegistries.BLOCK.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));

    }


}
