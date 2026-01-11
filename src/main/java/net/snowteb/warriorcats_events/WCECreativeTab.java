package net.snowteb.warriorcats_events;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.item.ModItems;

public class WCECreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WarriorCatsEvents.MODID);

    public static final RegistryObject<CreativeModeTab> WARRIOR_CATS_TAB =
            CREATIVE_TABS.register("warriorcats_events_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("tab.warriorcats_events"))
                    .icon(() -> new ItemStack(ModItems.CLAWS.get()))
                    .displayItems((params, output) -> {

                        ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                        ModBlocks.BLOCKS.getEntries().forEach(item -> output.accept(item.get()));

                    })
                    .build()
            );

}
