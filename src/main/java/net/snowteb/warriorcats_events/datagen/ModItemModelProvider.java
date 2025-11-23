package net.snowteb.warriorcats_events.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, WarriorCatsEvents.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
       // simpleItem(ModItems.BURNET);

        simpleItem(ModItems.RIVERCLAN_MUSIC_DISC);
        simpleItem(ModItems.WHISKERS);
        simpleItem(ModItems.CLAWS);
        simpleItem(ModItems.MOUSE_FOOD);
        simpleItem(ModItems.SQUIRREL_FOOD);
        simpleItem(ModItems.WARRIORNAMERANDOMIZER);
        simpleItem(ModItems.FRESHKILL_AND_HERBS_BUNDLE);
        simpleItem(ModItems.TRAVELING_HERBS);


        simpleBlockItem(ModBlocks.LEAF_DOOR);

        withExistingParent(ModItems.MOUSE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SQUIRREL_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.WILDCAT_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));


        basicItem(ModItems.DOCK_LEAVES.get());
        basicItem(ModItems.SORREL.get());
        basicItem(ModItems.BURNET.get());
        basicItem(ModItems.CHAMOMILE.get());
        basicItem(ModItems.DAISY.get());
        basicItem(ModItems.DEATHBERRIES.get());
        basicItem(ModItems.CATMINT.get());


        {
            withExistingParent(
                    ModBlocks.DOCK.getId().getPath(),
                    new ResourceLocation("item/generated")
            ).texture("layer0",
                    new ResourceLocation(WarriorCatsEvents.MODID, "block/dock_stage3")
            );
            withExistingParent(
                    ModBlocks.SORRELPLANT.getId().getPath(),
                    new ResourceLocation("item/generated")
            ).texture("layer0",
                    new ResourceLocation(WarriorCatsEvents.MODID, "block/sorrel_stage3")
            );
            withExistingParent(
                    ModBlocks.BURNETPLANT.getId().getPath(),
                    new ResourceLocation("item/generated")
            ).texture("layer0",
                    new ResourceLocation(WarriorCatsEvents.MODID, "block/burnet_stage3")
            );
            withExistingParent(
                    ModBlocks.CHAMOMILEPLANT.getId().getPath(),
                    new ResourceLocation("item/generated")
            ).texture("layer0",
                    new ResourceLocation(WarriorCatsEvents.MODID, "block/chamomile_stage3")
            );
            withExistingParent(
                    ModBlocks.DAISYPLANT.getId().getPath(),
                    new ResourceLocation("item/generated")
            ).texture("layer0",
                    new ResourceLocation(WarriorCatsEvents.MODID, "block/daisy_stage3")
            );
            withExistingParent(
                    ModBlocks.DEATHBERRIESBUSH.getId().getPath(),
                    new ResourceLocation("item/generated")
            ).texture("layer0",
                    new ResourceLocation(WarriorCatsEvents.MODID, "block/deathberries_stage3")
            );
            withExistingParent(
                    ModBlocks.CATMINTPLANT.getId().getPath(),
                    new ResourceLocation("item/generated")
            ).texture("layer0",
                    new ResourceLocation(WarriorCatsEvents.MODID, "block/catmint_stage3")
            );

        }


        saplingItem(ModBlocks.STARRYTREE_SAPLING);
        saplingItem(ModBlocks.DARKTREE_SAPLING);




    }



    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(WarriorCatsEvents.MODID, "item/" + item.getId().getPath()));
    }
    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(WarriorCatsEvents.MODID, "item/" + item.getId().getPath()));
    }
    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(WarriorCatsEvents.MODID, "block/" + item.getId().getPath()));
    }


}
