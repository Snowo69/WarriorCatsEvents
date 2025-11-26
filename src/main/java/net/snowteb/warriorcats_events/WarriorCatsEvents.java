package net.snowteb.warriorcats_events;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.entity.ModBlockEntities;
import net.snowteb.warriorcats_events.effect.ModEffects;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.integration.WCatIntegration;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.screen.ModMenuTypes;
import net.snowteb.warriorcats_events.screen.StoneCleftScreen;
import net.snowteb.warriorcats_events.sound.ModSounds;
import software.bernie.geckolib.GeckoLib;
import tocraft.walkers.integrations.Integrations;

@Mod(WarriorCatsEvents.MODID)
public class WarriorCatsEvents {
    public static final String MODID = "warriorcats_events";

    public WarriorCatsEvents() {
        System.out.println("[WarriorCatsEvents] Mod Loaded");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        GeckoLib.initialize();

        Integrations.register(WCatIntegration.MODID, WCatIntegration::new);
        ModSounds.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModEffects.register(modEventBus);




        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);


        MinecraftForge.EVENT_BUS.register(this);





    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            ModPackets.register();

        });


    }



    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {

            event.accept(ModItems.SORREL);
            event.accept(ModItems.BURNET);
            event.accept(ModItems.DAISY);
            event.accept(ModItems.CHAMOMILE);
            event.accept(ModItems.MOUSE_FOOD);
            event.accept(ModItems.SQUIRREL_FOOD);
            event.accept(ModItems.TRAVELING_HERBS);
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.STARCLAN_KNOWLEDGE);
            event.accept(ModItems.RIVERCLAN_MUSIC_DISC);
            event.accept(ModBlocks.STONECLEFT);
            event.accept(ModItems.WHISKERS);
            event.accept(ModItems.CLAWS);
            event.accept(ModItems.WARRIORNAMERANDOMIZER);
            event.accept(ModItems.FRESHKILL_AND_HERBS_BUNDLE);

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
            event.accept(ModBlocks.CATMINTPLANT);

            event.accept(ModBlocks.LEAF_DOOR);
            event.accept(ModBlocks.MOSSBED);

            event.accept(ModBlocks.STRIPPED_DARK_LOG);
            event.accept(ModBlocks.DARK_LOG);
            event.accept(ModBlocks.STARRY_LOG);
            event.accept(ModBlocks.STRIPPED_STARRY_LOG);
            event.accept(ModBlocks.STARRY_LEAVES);

        }


        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.MOUSE_SPAWN_EGG);
            event.accept(ModItems.SQUIRREL_SPAWN_EGG);
            event.accept(ModItems.WILDCAT_SPAWN_EGG);
        }





    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.STONECLEFT_MENU.get(), StoneCleftScreen::new);
        }
    }


}
