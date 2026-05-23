package net.snowteb.warriorcats_events.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.custom.*;
import net.snowteb.warriorcats_events.util.ModBlockSetTypes;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.util.ModSoundTypes;

import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(WarriorCatsEvents.MODID);

    public static final DeferredHolder<Block, Block> MOSS_BED = BLOCKS.register("moss_bed",
                    () -> new MossBedBlock(
                            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.2F).noOcclusion()
                    ));
    public static final DeferredHolder<Block, Block> HAY_BED = BLOCKS.register("hay_bed",
            () -> new MossBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.2F).noOcclusion()
            ));
    public static final DeferredHolder<Block, Block> KELP_BED = BLOCKS.register("kelp_bed",
            () -> new MossBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.2F).noOcclusion()
            ));

    public static final DeferredHolder<Block, Block> STONE_BED = BLOCKS.register("stone_bed",
            () -> new MossBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.STONE).strength(0.4F).noOcclusion()
            ));

    public static final DeferredHolder<Block, Block> LAVENDER_BED = BLOCKS.register("lavender_bed",
            () -> new MossBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.2F).noOcclusion()
            ));




    public static final DeferredHolder<Block, Block> MAKESHIFT_BED = BLOCKS.register("makeshift_bed",
            () -> new MakeshiftBedBlock(
                    BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.MOSS).strength(0.0F).noOcclusion()
            ));

    public static final DeferredHolder<Block, Block> PREY_BONES = BLOCKS.register("prey_bones",
            () -> new PreyBonesBlock(
                    BlockBehaviour.Properties.of().sound(ModSoundTypes.PREY_BONES).strength(0.0F).noOcclusion()
            ));

    public static final DeferredHolder<Block, Block> PEBBLES = BLOCKS.register("pebbles",
            () -> new PebblesBlock(
                    BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(0.25F).noOcclusion()
            ));


    public static final DeferredHolder<Block, Block> STONE_CRAFTING_TABLE = BLOCKS.register("stone_crafting_table",
            ()  -> new StoneCraftingTable(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(0.2F).noOcclusion()));

    public static final DeferredHolder<Block, Block> DOCK = BLOCKS.register("dock",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.DOCK_LEAVES, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final DeferredHolder<Block, Block> SORRELPLANT = BLOCKS.register("sorrel",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.SORREL, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final DeferredHolder<Block, Block> BURNETPLANT = BLOCKS.register("burnet",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.BURNET, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final DeferredHolder<Block, Block> CHAMOMILEPLANT = BLOCKS.register("chamomile_stems",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.CHAMOMILE, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final DeferredHolder<Block, Block> DAISYPLANT = BLOCKS.register("daisy",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.DAISY, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final DeferredHolder<Block, Block> CATMINTPLANT = BLOCKS.register("catmint",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.CATMINT, 1f)),
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final DeferredHolder<Block, Block> DEATHBERRIESBUSH = BLOCKS.register("deathberries_bush",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.DEATHBERRIES, 1f)),
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final DeferredHolder<Block, Block> YARROWPLANT = BLOCKS.register("yarrow",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.YARROW, 1f)),
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));

    public static final DeferredHolder<Block, Block> FEVERFEWPLANT = BLOCKS.register("feverfew",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.FEVERFEW, 1f)),
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));

    public static final DeferredHolder<Block, Block> JUNIPERPLANT = BLOCKS.register("juniper",
            () -> new BigBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.JUNIPER_BERRIES,
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));

    public static final DeferredHolder<Block, Block> COMFREYPLANT = BLOCKS.register("comfrey",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    List.of(new GenericBushBlock.DropItem(ModItems.COMFREY_LEAVES, 1f),
                            new GenericBushBlock.DropItem(ModItems.COMFREY_ROOT, 0.25f)),
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));



    public static final DeferredHolder<Block, Block> LEAF_DOOR = registerBlock("leaf_door",
            () -> new LeafDoorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).sound(SoundType.CHERRY_LEAVES).noOcclusion(),
                    ModBlockSetTypes.LEAF));

    public static final DeferredHolder<Block, Block> TREE_STUMP = registerBlock("tree_stump",
            () -> new TreeStumpBlock(BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.WOOD)));

    public static final DeferredHolder<Block, Block> LEAF_TRAPDOOR = registerBlock("leaf_trapdoor",
            () -> new LeafTrapdoorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).sound(SoundType.CHERRY_LEAVES).noOcclusion(),
                    ModBlockSetTypes.LEAF));

    public static final DeferredHolder<Block, Block> GLOWSHROOM = BLOCKS.register("glowshroom", () -> new GlowshroomBlock(BlockBehaviour.Properties.of()));


    public static final DeferredHolder<Block, Block> STONECLEFT = registerBlock("stonecleft",
            () -> new StoneCleftBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion().requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, Block> FRESHKILL_PILE = registerBlock("fresh_kill_pile",
            () -> new FreshkillPileBlock(BlockBehaviour.Properties.of()));

    public static  final DeferredBlock<DoublePlantBlock> LAVENDER = registerBlock("lavender",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ROSE_BUSH)));

    public static  final DeferredBlock<LavenderPetalsBlock> LAVENDER_PETALS = registerBlock("lavender_petals",
            () -> new LavenderPetalsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_PETALS)));




    // KITTYPET BOWLS

    public static final DeferredBlock<KittyPetBowl> WHITE_KITTYPET_BOWL = registerBlock("white_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final DeferredBlock<KittyPetBowl> ORANGE_KITTYPET_BOWL = registerBlock("orange_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final DeferredBlock<KittyPetBowl> MAGENTA_KITTYPET_BOWL = registerBlock("magenta_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final DeferredBlock<KittyPetBowl> BLUE_KITTYPET_BOWL = registerBlock("blue_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final DeferredBlock<KittyPetBowl> YELLOW_KITTYPET_BOWL = registerBlock("yellow_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final DeferredBlock<KittyPetBowl> LIME_KITTYPET_BOWL = registerBlock("lime_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final DeferredBlock<KittyPetBowl> PINK_KITTYPET_BOWL = registerBlock("pink_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final DeferredBlock<KittyPetBowl> BLACK_KITTYPET_BOWL = registerBlock("black_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    public static final DeferredBlock<KittyPetBowl> RED_KITTYPET_BOWL = registerBlock("red_kittypet_bowl",
            () -> new KittyPetBowl(BlockBehaviour.Properties.of().strength(0.5f)));

    //


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
