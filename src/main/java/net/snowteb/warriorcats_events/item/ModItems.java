package net.snowteb.warriorcats_events.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.item.custom.ModBookItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WarriorCatsEvents.MODID);




    public static final RegistryObject<Item> DOCK_LEAVES = ITEMS.register("dock_leaves",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SORREL = ITEMS.register("sorrel",
            () -> new Item(new Item.Properties().food(ModFoodHerbs.SORREL)));
    public static final RegistryObject<Item> STARCLAN_KNOWLEDGE = ITEMS.register("starclan_knowledge",
            () -> new ModBookItem(new Item.Properties().stacksTo(1)));


    /* public static final RegistryObject<Item> STARCLAN_BOOK = ITEMS.register("starclan_book",
            () -> new WrittenBookItem(new Item.Properties().stacksTo(1)) {
                @Override
                public ItemStack getDefaultInstance() {
                    ItemStack book = new ItemStack(this);
                    CompoundTag tag = new CompoundTag();

                    tag.putString("title", "The lives of a leader");
                    tag.putString("author", "StarClan");
                    tag.putBoolean("resolved", true);
                    tag.putInt("generation", 0);

                    // Contenido del libro
                    ListTag pages = new ListTag();
                    pages.add(StringTag.valueOf(
                            "{\"text\":\"*A strange cat approaches to you, followed by more and more, until you are surrounded by the warriors of StarClan. You find yourself blinded by their starry pelts, you realize your old life has come to an end.*\\n\\n\",\"color\":\"gray\",\"italic\":true,\"extra\":[{\"text\":\"Accept the lives of a leader\",\"color\":\"gold\",\"bold\":true,\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/function warriorcats:leaderslives_1\"}}]}"
                    ));

                    tag.put("pages", pages);

                    book.setTag(tag);
                    return book;
                }
            }); */



    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}