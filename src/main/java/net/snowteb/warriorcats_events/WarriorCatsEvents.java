package net.snowteb.warriorcats_events;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.init.WarriorCatsEventsBlocks;
import net.snowteb.warriorcats_events.init.WarriorCatsEventsItems;
import net.snowteb.warriorcats_events.item.ModItems;

@Mod(WarriorCatsEvents.MODID)
public class WarriorCatsEvents {
    public static final String MODID = "warriorcats_events";

    public WarriorCatsEvents() {
        System.out.println("[WarriorCatsEvents] Mod cargado correctamente.");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        WarriorCatsEventsBlocks.REGISTRY.register(modEventBus);
        WarriorCatsEventsItems.REGISTRY.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {

            event.accept(ModItems.TRAVELING_HERBS);
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.STARCLAN_KNOWLEDGE);

        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.DOCK_POULTICE);
            event.accept(ModItems.DOCK_LEAVES);
            event.accept(ModItems.SORREL);
            event.accept(ModItems.BURNET);
            event.accept(ModItems.DAISY);
            event.accept(ModItems.CHAMOMILE);

        }

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.DOCK);
            event.accept(ModBlocks.SORRELPLANT);
            event.accept(ModBlocks.BURNETPLANT);
            event.accept(ModBlocks.CHAMOMILEPLANT);
            event.accept(ModBlocks.DAISYPLANT);
            event.accept(ModBlocks.DEATHBERRIESBUSH);

        }

    }
}
