package net.snowteb.warriorcats_events.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.custom.*;
import net.snowteb.warriorcats_events.util.ModBlockSetTypes;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.worldgen.tree.DarkTreeGrower;
import net.snowteb.warriorcats_events.worldgen.tree.StarryTreeGrower;

import java.util.function.Supplier;

public class ModBlocks {
    public static final SoundType CUSTOMHERBS = new SoundType(
            1.0f, 1.0f, SoundEvents.CHERRY_LEAVES_HIT, null,
            SoundEvents.SWEET_BERRY_BUSH_PLACE, SoundEvents.CHERRY_LEAVES_HIT, null);

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, WarriorCatsEvents.MODID);




    public static final RegistryObject<Block> DOCK = BLOCKS.register("dock",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.DOCK_LEAVES,
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> SORRELPLANT = BLOCKS.register("sorrel",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.SORREL,
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> BURNETPLANT = BLOCKS.register("burnet",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.BURNET,
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> CHAMOMILEPLANT = BLOCKS.register("chamomile_stems",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.CHAMOMILE,
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> DAISYPLANT = BLOCKS.register("daisy",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.DAISY,
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> CATMINTPLANT = BLOCKS.register("catmint",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.CATMINT,
                    1,
                    3,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> DEATHBERRIESBUSH = BLOCKS.register("deathberries_bush",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.DEATHBERRIES,
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));
    public static final RegistryObject<Block> YARROWPLANT = BLOCKS.register("yarrow",
            () -> new GenericBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).randomTicks(),
                    ModItems.YARROW,
                    1,
                    2,
                    SoundEvents.AZALEA_LEAVES_FALL
            ));




    public static final RegistryObject<Block> LEAF_DOOR = registerBlock("leaf_door",
            () -> new ModDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).sound(SoundType.CHERRY_LEAVES).noOcclusion(),
                    ModBlockSetTypes.LEAF));

    public static final RegistryObject<Block> MOSSBED = BLOCKS.register("mossbed", MossbedBlock::new);
    public static final RegistryObject<Block> GLOWSHROOM = BLOCKS.register("glowshroom", GlowshroomBlock::new);


    public static final RegistryObject<Block> STONECLEFT = registerBlock("stonecleft",
            () -> new StoneCleftBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion().requiresCorrectToolForDrops()));

//    public static final RegistryObject<Block> DARK_LOG = registerBlock("dark_log",
//            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LOG).strength(3f).noLootTable()));
//    public static final RegistryObject<Block> STRIPPED_DARK_LOG = registerBlock("stripped_dark_log",
//            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_DARK_OAK_WOOD).strength(3f).noLootTable()));
//
//    public static final RegistryObject<Block> STARRY_LOG = registerBlock("starry_log",
//            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LOG).strength(3f).noLootTable()));
//    public static final RegistryObject<Block> STRIPPED_STARRY_LOG = registerBlock("stripped_starry_log",
//            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).strength(3f).noLootTable()));
//    public static final RegistryObject<Block> STARRY_LEAVES = registerBlock("starry_leaves",
//            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noLootTable()){
//                @Override
//                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
//                    return true;
//                }
//
//                @Override
//                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
//                    return 60;
//                }
//
//                @Override
//                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
//                    return 30;
//                }
//            });
//
//
//    public static final RegistryObject<Block> DARKTREE_SAPLING = registerBlock("darktree_sapling",
//            () -> new SaplingBlock(new DarkTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
//    public static final RegistryObject<Block> STARRYTREE_SAPLING = registerBlock("starrytree_sapling",
//            () -> new SaplingBlock(new StarryTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));



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
