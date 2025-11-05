package net.snowteb.warriorcats_events.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class MossbItems {
    public static final DeferredRegister<Item> REGISTRY =
            DeferredRegister.create(ForgeRegistries.ITEMS, "warriorcats_events");

    public static final RegistryObject<Item> MOSSBED = REGISTRY.register("mossbed",
            () -> new BlockItem(MossbBlocks.MOSSBED.get(), new Item.Properties().stacksTo(16)));
}
