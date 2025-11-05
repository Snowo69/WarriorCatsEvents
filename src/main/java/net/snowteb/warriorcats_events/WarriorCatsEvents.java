package net.snowteb.warriorcats_events;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;
import net.snowteb.warriorcats_events.init.MossbBlocks;
import net.snowteb.warriorcats_events.init.MossbItems;
import net.snowteb.warriorcats_events.item.ModItems;

@Mod(WarriorCatsEvents.MODID)
public class WarriorCatsEvents {
    public static final String MODID = "warriorcats_events";

    public WarriorCatsEvents() {
        System.out.println("[WarriorCatsEvents] Mod cargado correctamente.");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        MossbBlocks.REGISTRY.register(modEventBus);
        MossbItems.REGISTRY.register(modEventBus);

        // 👇 ESTA LÍNEA ERA LA QUE FALTABA
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.DOCK_LEAVES);
            event.accept(ModItems.SORREL);
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.STARCLAN_KNOWLEDGE);
        }


        }

    }
}
