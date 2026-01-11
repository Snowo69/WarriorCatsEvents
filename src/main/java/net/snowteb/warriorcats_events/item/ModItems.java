package net.snowteb.warriorcats_events.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.item.custom.*;
//import net.snowteb.warriorcats_events.item.custom.LeaderCallItem;
import net.snowteb.warriorcats_events.sound.ModSounds;
import net.snowteb.warriorcats_events.util.ItemWithToolTip;
import net.snowteb.warriorcats_events.util.PoulticeTooltip;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nullable;
import java.util.List;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WarriorCatsEvents.MODID);


    public static final RegistryObject<Item> MOUSE_SPAWN_EGG = ITEMS.register("mouse_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MOUSE,0xAD8A6C, 0xE3AD7D,
                    new Item.Properties()));
    public static final RegistryObject<Item> SQUIRREL_SPAWN_EGG = ITEMS.register("squirrel_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SQUIRREL,0xF59527, 0xFCC68B,
                    new Item.Properties()));
    public static final RegistryObject<Item> WILDCAT_SPAWN_EGG = ITEMS.register("wildcat_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.WCAT,0xfcfbe6, 0xf5dfb8,
                    new Item.Properties()));
    public static final RegistryObject<Item> PIGEON_SPAWN_EGG = ITEMS.register("pigeon_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PIGEON,0xa8acb3, 0xccc9bc,
                    new Item.Properties()));
    public static final RegistryObject<Item> BADGER_SPAWN_EGG = ITEMS.register("badger_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BADGER,0x4a4946, 0xe3e2e1,
                    new Item.Properties()));

    public static final RegistryObject<Item> WARRIORS_GUIDE = ITEMS.register("warriors_guide",
            () -> new Item(new Item.Properties().stacksTo(1)) {
                @Override
                public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                    if (!level.isClientSide) {
                        PatchouliAPI.get().openBookGUI((ServerPlayer) player,
                                new ResourceLocation("warriorcats_events", "warriors_guide"));
                    }
                    return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
                }
                @Override
                public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
                    tooltip.add(Component.translatable("item.warriorcats_events.warriors_guide.tooltip").withStyle(ChatFormatting.GRAY));
                }
            });



public static final RegistryObject<Item> FLOWER_CROWN = ITEMS.register("flower_crown",
        () -> new FlowerCrownItem());
    public static final RegistryObject<Item> LEAF_MANE = ITEMS.register("leaf_mane",
            () -> new LeafManeItem());
    public static final RegistryObject<Item> FLOWER_ARMOR = ITEMS.register("flower_armor",
            () -> new FlowerArmorItem());
    public static final RegistryObject<Item> TEETH_CLAWS = ITEMS.register("teeth_claws",
            () -> new TeethClawsItem());

    public static final RegistryObject<Item> ANCIENT_STICK = ITEMS.register("ancient_stick",
        () -> new AncientStickItem(new Item.Properties().stacksTo(1).durability(550)));

    public static final RegistryObject<Item> KIT_ITEM = ITEMS.register("kit",
            () -> new KitItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MYSTIC_FLOWERS_BOUQUET = ITEMS.register("rare_flowers_bouquet",
            () -> new ItemWithToolTip(new Item.Properties().stacksTo(1),
                    "item.warriorcats_events.rare_flowers_bouquet.tooltip"));
    public static final RegistryObject<Item> STRANGE_SHINY_STONE = ITEMS.register("strange_shiny_stone",
            () -> new ItemWithToolTip(new Item.Properties().stacksTo(16),
                    "item.warriorcats_events.strange_shiny_stone.tooltip"));


    public static final RegistryObject<Item> ANIMAL_TOOTH = ITEMS.register("animal_tooth",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ANIMAL_TEETH = ITEMS.register("animal_teeth",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> WARRIOR_NAMETAG = ITEMS.register("warrior_nametag",
            () -> new ItemWithToolTip(new Item.Properties().stacksTo(8),
                    "item.warriorcats_events.warrior_nametag.tooltip"));


    public static final RegistryObject<Item> DOCK_LEAVES = ITEMS.register("dock_leaves",
            () -> new ItemWithToolTip(new Item.Properties(), "item.warriorcats_events.dock_leaves.tooltip"));
    public static final RegistryObject<Item> SORREL = ITEMS.register("sorrel_leaves",
            () -> new ItemWithToolTip(new Item.Properties().food(ModFoodHerbs.SORREL),
                    "item.warriorcats_events.sorrel_leaves.tooltip"));
    public static final RegistryObject<Item> BURNET = ITEMS.register("burnet_leaves",
            () -> new ItemWithToolTip(new Item.Properties().food(ModFoodHerbs.BURNET),
                    "item.warriorcats_events.burnet_leaves.tooltip"));
    public static final RegistryObject<Item> CHAMOMILE = ITEMS.register("chamomile",
            () -> new ItemWithToolTip(new Item.Properties().food(ModFoodHerbs.CHAMOMILE),
                    "item.warriorcats_events.chamomile.tooltip"));
    public static final RegistryObject<Item> DAISY = ITEMS.register("daisy_leaves",
            () -> new ItemWithToolTip(new Item.Properties().food(ModFoodHerbs.DAISY),
                    "item.warriorcats_events.daisy_leaves.tooltip"));
    public static final RegistryObject<Item> DEATHBERRIES = ITEMS.register("deathberries",
            () -> new ItemWithToolTip(new Item.Properties().food(ModFoodHerbs.DEATHBERRIES),
                    "item.warriorcats_events.deathberries.tooltip"));
    public static final RegistryObject<Item> CATMINT = ITEMS.register("catmint_leaves",
            () -> new ItemWithToolTip(new Item.Properties().food(ModFoodHerbs.CATMINT),
                    "item.warriorcats_events.catmint.tooltip"));
    public static final RegistryObject<Item> YARROW = ITEMS.register("yarrow_leaves",
            () -> new ItemWithToolTip(new Item.Properties().food(ModFoodHerbs.YARROW),
                    "item.warriorcats_events.yarrow.tooltip"));


    public static final RegistryObject<Item> TRAVELING_HERBS = ITEMS.register("traveling_herbs",
            () -> new ItemWithToolTip(new Item.Properties().food(ModFoodHerbs.TRAVELING_HERBS),
            "item.warriorcats_events.traveling_herbs.tooltip"));
    public static final RegistryObject<Item> DOCK_POULTICE = ITEMS.register("dock_poultice",
            () -> new PoulticeTooltip(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> MOUSE_FOOD = ITEMS.register("mouse",
            () -> new Item(new Item.Properties().food(ModFoodHerbs.MOUSE_FOOD)));
    public static final RegistryObject<Item> SQUIRREL_FOOD = ITEMS.register("squirrel",
            () -> new Item(new Item.Properties().food(ModFoodHerbs.SQUIRREL_FOOD)));
    public static final RegistryObject<Item> PIGEON_FOOD = ITEMS.register("pigeon",
            () -> new Item(new Item.Properties().food(ModFoodHerbs.PIGEON_FOOD)));



    public static final RegistryObject<Item> WHISKERS = ITEMS.register("whiskers",
            () -> new ItemWithToolTip(new Item.Properties().stacksTo(1).durability(400),
                    "item.warriorcats_events.whiskers.tooltip"));

    public static final RegistryObject<Item> CLAWS = ITEMS.register("claws",
            () -> new ClawsTooltip(new Item.Properties().stacksTo(1).durability(260),
                    "item.warriorcats_events.claws.tooltip"));

    public static final RegistryObject<Item> WARRIORNAMERANDOMIZER = ITEMS.register("warrior_name_randomizer",
            () -> new ItemWithToolTip(new Item.Properties().stacksTo(1).durability(32),
                    "item.warriorcats_events.warrior_name_randomizer.tooltip"));

    public static final RegistryObject<Item> FRESHKILL_AND_HERBS_BUNDLE = ITEMS.register("freshkill_and_herbs",
            () -> new ItemWithToolTip(new Item.Properties(),
                    "item.warriorcats_events.freshkill_and_herbs.tooltip"));


    // DO NOT REMOVE ANY OF THIS
    public static final RegistryObject<Item> DOCK = ITEMS.register("dock",
            () -> new BlockItem(ModBlocks.DOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> SORRELPLANT = ITEMS.register("sorrel",
            () -> new BlockItem(ModBlocks.SORRELPLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> BURNETPLANT = ITEMS.register("burnet",
            () -> new BlockItem(ModBlocks.BURNETPLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHAMOMILEPLANT = ITEMS.register("chamomile_stems",
            () -> new BlockItem(ModBlocks.CHAMOMILEPLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> DAISYPLANT = ITEMS.register("daisy",
            () -> new BlockItem(ModBlocks.DAISYPLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> DEATHBERRIESBUSH = ITEMS.register("deathberries_bush",
            () -> new BlockItem(ModBlocks.DEATHBERRIESBUSH.get(), new Item.Properties()));
    public static final RegistryObject<Item> CATMINTPLANT = ITEMS.register("catmint",
            () -> new BlockItem(ModBlocks.CATMINTPLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> YARROWPLANT = ITEMS.register("yarrow",
            () -> new BlockItem(ModBlocks.YARROWPLANT.get(), new Item.Properties()));

    public static final RegistryObject<Item> MOSS_BED = ITEMS.register("mossbed",
            () -> new BlockItem(ModBlocks.MOSSBED.get(), new Item.Properties().stacksTo(16)));



    public static final RegistryObject<Item> GENERATIONS_MUSIC_DISC = ITEMS.register("generations_music_disc",
            () -> new RecordItem(6, ModSounds.GENERATIONS, new Item.Properties().stacksTo(1), 2650));
//
//    public static final RegistryObject<Item> STARCLAN_KNOWLEDGE = ITEMS.register("starclan_knowledge",
//            () -> new ModBookItemToolTip(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> GLOW_SHROOM = ITEMS.register("glowshroom",
            () -> new BlockItem(ModBlocks.GLOWSHROOM.get(), new Item.Properties().stacksTo(16)));





    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}