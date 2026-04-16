package net.snowteb.warriorcats_events.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WarriorCatsEvents.MODID);


    public static final RegistryObject<BlockEntityType<StoneCleftBlockEntity>> STONECLEFT_BLOCK =
            BLOCK_ENTITIES.register("stone_cleft_block", () ->
                    BlockEntityType.Builder.of(StoneCleftBlockEntity::new,
                            ModBlocks.STONECLEFT.get()).build(null));

    public static final RegistryObject<BlockEntityType<FreshkillPileBlockEntity>> FRESH_KILL_PILE =
            BLOCK_ENTITIES.register("fresh_kill_pile_block", () ->
                    BlockEntityType.Builder.of(FreshkillPileBlockEntity::new,
                            ModBlocks.FRESHKILL_PILE.get()).build(null));

    public static final RegistryObject<BlockEntityType<MossBedBlockEntity>> MOSS_BED =
            BLOCK_ENTITIES.register("moss_bed", () ->
                    BlockEntityType.Builder.of(MossBedBlockEntity::new,
                            ModBlocks.MOSS_BED.get(),
                            ModBlocks.HAY_BED.get(),
                            ModBlocks.KELP_BED.get(),
                            ModBlocks.STONE_BED.get(),
                            ModBlocks.LAVENDER_BED.get()
                            )
                            .build(null)
            );

    public static final RegistryObject<BlockEntityType<MakeshiftBedBlockEntity>> MAKESHIFT_BED =
            BLOCK_ENTITIES.register("makeshift_bed", () ->
                    BlockEntityType.Builder.of(MakeshiftBedBlockEntity::new,
                                    ModBlocks.MAKESHIFT_BED.get())
                            .build(null)
            );

    public static final RegistryObject<BlockEntityType<StoneCraftingTableBlockEntity>> STONE_TABLE =
            BLOCK_ENTITIES.register("stone_table", () ->
                    BlockEntityType.Builder.of(StoneCraftingTableBlockEntity::new,
                            ModBlocks.STONE_CRAFTING_TABLE.get()).build(null)
            );

    public static final RegistryObject<BlockEntityType<TreeStumpBlockEntity>> TREE_STUMP =
            BLOCK_ENTITIES.register("tree_stump", () ->
                    BlockEntityType.Builder.of(TreeStumpBlockEntity::new,
                            ModBlocks.TREE_STUMP.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
