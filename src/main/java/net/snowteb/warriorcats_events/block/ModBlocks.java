package net.snowteb.warriorcats_events.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.custom.*;
import net.snowteb.warriorcats_events.util.ModBlockSetTypes;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.util.ModSoundTypes;

import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, WarriorCatsEvents.MODID);

    public static final RegistryObject<Block> MOSS_BED = BLOCKS.register("moss_bed",
                    () -> new MossBedBlock(
                            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.2F).noOcclusion()
                    ));
    public static final RegistryObject<Block> HAY_BED = BLOCKS.register("hay_bed",
            () -> new MossBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.2F).noOcclusion()
            ));
    public static final RegistryObject<Block> KELP_BED = BLOCKS.register("kelp_bed",
            () -> new MossBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.2F).noOcclusion()
            ));

    public static final RegistryObject<Block> STONE_BED = BLOCKS.register("stone_bed",
            () -> new MossBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.STONE).strength(0.4F).noOcclusion()
            ));

    public static final RegistryObject<Block> LAVENDER_BED = BLOCKS.register("lavender_bed",
            () -> new MossBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.2F).noOcclusion()
            ));




    public static final RegistryObject<Block> MAKESHIFT_BED = BLOCKS.register("makeshift_bed",
            () -> new MakeshiftBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.0F).noOcclusion()
            ));

    public static final RegistryObject<Block> PREY_BONES = BLOCKS.register("prey_bones",
            () -> new PreyBonesBlock(
                    BlockBehaviour.Properties.of().sound(ModSoundTypes.PREY_BONES).strength(0.0F).noOcclusion()
            ));

    public static final RegistryObject<Block> PEBBLES = BLOCKS.register("pebbles",
            () -> new PebblesBlock(
                    BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(0.25F).noOcclusion()
            ));


    public static final RegistryObject<Block> STONE_CRAFTING_TABLE = BLOCKS.register("stone_crafting_table",
            ()  -> new StoneCraftingTable(BlockBehaviour.Properties.copy(Blocks.STONE).strength(0.2F).noOcclusion()));

    public static final RegistryObject<Block> DOCK = BLOCKS.register("dock",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.DOCK_LEAVES, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> SORRELPLANT = BLOCKS.register("sorrel",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.SORREL, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> BURNETPLANT = BLOCKS.register("burnet",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.BURNET, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> CHAMOMILEPLANT = BLOCKS.register("chamomile_stems",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.CHAMOMILE, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> DAISYPLANT = BLOCKS.register("daisy",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.DAISY, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> CATMINTPLANT = BLOCKS.register("catmint",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.CATMINT, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> DEATHBERRIESBUSH = BLOCKS.register("deathberries_bush",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.DEATHBERRIES, 1f)),
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> YARROWPLANT = BLOCKS.register("yarrow",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.YARROW, 1f)),
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));

    public static final RegistryObject<Block> FEVERFEWPLANT = BLOCKS.register("feverfew",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.FEVERFEW, 1f)),
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));

    public static final RegistryObject<Block> JUNIPERPLANT = BLOCKS.register("juniper",
            () -> new BigBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.JUNIPER_BERRIES,
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));

    public static final RegistryObject<Block> COMFREYPLANT = BLOCKS.register("comfrey",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.COMFREY_LEAVES, 1f),
                            new GenericBushBlock.DropItem(ModItems.COMFREY_ROOT, 0.25f)),
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));



    public static final RegistryObject<Block> LEAF_DOOR = registerBlock("leaf_door",
            () -> new LeafDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).sound(SoundType.CHERRY_LEAVES).noOcclusion(),
                    ModBlockSetTypes.LEAF));

    public static final RegistryObject<Block> TREE_STUMP = registerBlock("tree_stump",
            () -> new TreeStumpBlock(BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LEAF_TRAPDOOR = registerBlock("leaf_trapdoor",
            () -> new LeafTrapdoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).sound(SoundType.CHERRY_LEAVES).noOcclusion(),
                    ModBlockSetTypes.LEAF));

    public static final RegistryObject<Block> GLOWSHROOM = BLOCKS.register("glowshroom", GlowshroomBlock::new);


    public static final RegistryObject<Block> STONECLEFT = registerBlock("stonecleft",
            () -> new StoneCleftBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> FRESHKILL_PILE = registerBlock("fresh_kill_pile",
            () -> new FreshkillPileBlock());

    public static  final RegistryObject<DoublePlantBlock> LAVENDER = registerBlock("lavender",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.copy(Blocks.ROSE_BUSH)));

    public static  final RegistryObject<LavenderPetalsBlock> LAVENDER_PETALS = registerBlock("lavender_petals",
            () -> new LavenderPetalsBlock(BlockBehaviour.Properties.copy(Blocks.PINK_PETALS)));




    // KITTYPET BOWLS

    public static final RegistryObject<KittyPetBowl> WHITE_KITTYPET_BOWL = registerBlock("white_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final RegistryObject<KittyPetBowl> ORANGE_KITTYPET_BOWL = registerBlock("orange_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final RegistryObject<KittyPetBowl> MAGENTA_KITTYPET_BOWL = registerBlock("magenta_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final RegistryObject<KittyPetBowl> BLUE_KITTYPET_BOWL = registerBlock("blue_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final RegistryObject<KittyPetBowl> YELLOW_KITTYPET_BOWL = registerBlock("yellow_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final RegistryObject<KittyPetBowl> LIME_KITTYPET_BOWL = registerBlock("lime_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final RegistryObject<KittyPetBowl> PINK_KITTYPET_BOWL = registerBlock("pink_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final RegistryObject<KittyPetBowl> BLACK_KITTYPET_BOWL = registerBlock("black_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final RegistryObject<KittyPetBowl> RED_KITTYPET_BOWL = registerBlock("red_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    //


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
