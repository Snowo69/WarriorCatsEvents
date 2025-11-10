package net.snowteb.warriorcats_events.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final SoundType CUSTOMHERBS = new SoundType(
            1.0f, 1.0f, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, null,
            SoundEvents.SWEET_BERRY_BUSH_PLACE, SoundEvents.CHERRY_LEAVES_HIT, null);

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, WarriorCatsEvents.MODID);


    public static final RegistryObject<BushBlock> DOCK = registerBlock("dock",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).sound(ModBlocks.CUSTOMHERBS)
                    .strength(0.1f)));
    public static final RegistryObject<BushBlock> SORRELPLANT = registerBlock("sorrel",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).sound(ModBlocks.CUSTOMHERBS)
                    .strength(0.1f)));
    public static final RegistryObject<BushBlock> BURNETPLANT = registerBlock("burnet",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).sound(ModBlocks.CUSTOMHERBS)
                    .strength(0.1f)));
    public static final RegistryObject<BushBlock> CHAMOMILEPLANT = registerBlock("chamomile_stems",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).sound(ModBlocks.CUSTOMHERBS)
                    .strength(0.1f)));
    public static final RegistryObject<BushBlock> DAISYPLANT = registerBlock("daisy",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).sound(ModBlocks.CUSTOMHERBS)
                    .strength(0.1f)));
    public static final RegistryObject<BushBlock> DEATHBERRIESBUSH = registerBlock("deathberries_bush",
            () -> new BushBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noLootTable().sound(ModBlocks.CUSTOMHERBS)
                    .strength(0.1f)));



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
