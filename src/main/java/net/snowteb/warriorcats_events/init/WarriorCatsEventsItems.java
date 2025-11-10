package net.snowteb.warriorcats_events.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.snowteb.warriorcats_events.WarriorCatsEvents;


public class WarriorCatsEventsItems {
    public static final DeferredRegister<Item> REGISTRY =
            DeferredRegister.create(ForgeRegistries.ITEMS, WarriorCatsEvents.MODID);

    public static final RegistryObject<Item> MOSS_BED = REGISTRY.register("mossbed",
            () -> new BlockItem(WarriorCatsEventsBlocks.MOSSBED.get(), new Item.Properties().stacksTo(16)));
}
