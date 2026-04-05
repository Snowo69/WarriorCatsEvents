package net.snowteb.warriorcats_events.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.entity.*;
import net.snowteb.warriorcats_events.client.ClientStoredMorphs;
import net.snowteb.warriorcats_events.client.EntityChatBubbleManager;
import net.snowteb.warriorcats_events.client.LeapClientState;
import net.snowteb.warriorcats_events.client.ThirstHUD;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.client.*;
import net.snowteb.warriorcats_events.entity.custom.EagleEntity;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.clan.EmoteMorphPacket;
import net.snowteb.warriorcats_events.network.packet.c2s.others.*;
import net.snowteb.warriorcats_events.network.packet.s2c.cats.OpenCatDataScreenPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.clan.OpenClanSetupScreenPacket;
import net.snowteb.warriorcats_events.network.packet.s2c.others.StCFishingScreenPacket;
import net.snowteb.warriorcats_events.particles.*;
import net.snowteb.warriorcats_events.screen.*;
import net.snowteb.warriorcats_events.screen.clandata.CatDataScreen;
import net.snowteb.warriorcats_events.screen.clandata.ClanSetupScreen;
import net.snowteb.warriorcats_events.skills.StealthClientState;
import net.snowteb.warriorcats_events.sound.ModSounds;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import net.snowteb.warriorcats_events.util.ModKeybinds;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

import static net.snowteb.warriorcats_events.WCEClient.*;

public class ClientEvents {
    private static boolean hissPressed;
    private static boolean waterPressed;
    private static int hissCooldown = 0;
    private static int waterCooldown = 0;
    public int shiftKeyDownCount = 0;

    @Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {

            if ((event.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT || event.getKey() == GLFW.GLFW_KEY_RIGHT_SHIFT) && event.getAction() == GLFW.GLFW_PRESS) {
                if (isBeingLatched) {
                    setFreeCounter+=16;
                }
            }

        }


        @SubscribeEvent
        public static void onPlaySound(PlaySoundEvent event) {
            SoundInstance sound = event.getSound();

            if (sound != null && sound.getLocation().equals(ModSounds.GENERATIONS.getId())) {
                Minecraft.getInstance().getSoundManager().stop(null, SoundSource.MUSIC);
            }
        }

        @SubscribeEvent
        public static void onScroll(InputEvent.MouseScrollingEvent event) {
            Minecraft mc = Minecraft.getInstance();

            if (isRenderingEmoteMenu && mc.screen == null) {

                emoteOffset -= (int) event.getScrollDelta();

                if (WarriorCatsEvents.Devs.isDev(Minecraft.getInstance().player.getUUID())) {
                    emoteOffset = Mth.clamp(emoteOffset, -2, MAX_EMOTES);
                } else {
                    emoteOffset = Mth.clamp(emoteOffset, -1, MAX_EMOTES);
                }

                WCEClient.playLocalSound(ModSounds.MENU_CLICK.get(), SoundSource.NEUTRAL, 0.1f,1.3f);

                event.setCanceled(true);
            } else if (isRenderingSoundMenu && mc.screen == null) {
                soundOffset -= (int) event.getScrollDelta();

                soundOffset = Mth.clamp(soundOffset, 0, MAX_SOUNDS);


                WCEClient.playLocalSound(ModSounds.MENU_CLICK.get(), SoundSource.NEUTRAL, 0.1f,1.3f);

                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (mc.player == null) return;
            if (mc.level == null) return;
            /**
             * If the skill is unlocked and on, and the player is shifting, send the info to the StealthClientState.
             */

            if (newParticleTime > 0) newParticleTime--;

            EntityChatBubbleManager.tick();

            player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {
                if (!cap.isUnlocked() || !cap.isOn()) return;

                boolean shifting = mc.options.keyShift.isDown();
                StealthClientState.tick(shifting);
            });

            {
                if (player.getVehicle() != null) {
                    if (player.getVehicle() instanceof EagleEntity eagle && !eagle.isOwnedBy(player)) {
                        if (!isBeingLatched) {
                            isBeingLatched = true;
                            setFreeCounter = 60;
                        }
                    } else {
                        isBeingLatched = false;
                    }
                } else {
                    if (isBeingLatched) isBeingLatched = false;
                }

                if (!isBeingLatched) {
                    setFreeCounter = 0;
                } else {

                    if (setFreeCounter >= 199) {
                        isBeingLatched = false;
                        setFreeCounter = 0;
                        ModPackets.sendToServer(new CtSDismountEaglePacket());

                    }

                    if (setFreeCounter > 0) {
                        setFreeCounter--;
                    }
                }

            }


            boolean shifting = mc.options.keyShift.isDown();
            LeapClientState.tick(shifting);


            /**
             * Keybinds with cooldowns.
             * This is so holding down the key doesn't send 20 packets per second and breaks the whole game.
             */
            if (hissCooldown > 0) hissCooldown--;
            if (waterCooldown > 0) waterCooldown--;

//            if (ModKeybinds.HISSING_KEY.isDown() && hissCooldown == 0) {
//                if (!hissPressed) {
//                    ModPackets.sendToServer(new CtSPlayCatSoundPacket());
//                    hissPressed = true;
//                    hissCooldown = 30;
//                }
//            } else {
//                hissPressed = false;
//            }

            if (ModKeybinds.WATERDRINK_KEY.isDown() && waterCooldown == 0) {
                if (!waterPressed) {
                    ModPackets.sendToServer(new WaterPacket());
//                    ModPackets.sendToServer(new CtSStartSleep());
                    waterPressed = true;
                    waterCooldown = 7;
                }
            } else {
                waterPressed = false;
            }
//            if (ModKeybinds.SKILLMENU_KEY.isDown()) {
//                Minecraft.getInstance().setScreen(new TerritoryMapScreen(null));
//            }


            if (EMOTES_HUD_MENU_KEY.consumeClick()) {

                if (isRenderingEmoteMenu) {

                    int selected = emoteOffset;

                    if (selected == -1) {
                        ModPackets.sendToServer(new CtSSwitchShape());
                    } else {
                        ModPackets.sendToServer(new EmoteMorphPacket(selected));
                    }

                } else {
                    WCEClient.playLocalSound(ModSounds.MENU_OPEN.get(), SoundSource.NEUTRAL, 0.4f,1f);
                }

                if (isRenderingSoundMenu) isRenderingSoundMenu = false;


                emoteOffset = 0;
                isRenderingEmoteMenu = !isRenderingEmoteMenu;
            }

            if (ModKeybinds.HISSING_KEY.consumeClick()) {
                if (isRenderingSoundMenu) {
                    int selected = soundOffset;
                    if (selected != 0) {
                        ModPackets.sendToServer(new CtSPlayCatSoundPacket(selected));
                    }
                } else {
                    WCEClient.playLocalSound(ModSounds.MENU_OPEN.get(), SoundSource.NEUTRAL, 0.4f,1f);
                }

                if (isRenderingEmoteMenu) isRenderingEmoteMenu = false;

                soundOffset = 0;
                isRenderingSoundMenu = !isRenderingSoundMenu;
            }


            if (isRenderingEmoteMenu && (mc.screen != null || mc.player == null)) {
                isRenderingEmoteMenu = false;
                emoteOffset = 0;
            }

            if (isRenderingSoundMenu && (mc.screen != null || mc.player == null)) {
                isRenderingSoundMenu = false;
                soundOffset = 0;
            }

        }

        /**
         * Part of the Update checker
         */
        @SubscribeEvent
        public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
            if (event.getPlayer().level().isClientSide()) {
                if (UpdateCheck.updateAvailable) {
//                    event.getPlayer().sendSystemMessage(
//                            Component.literal("[Warrior Cats Events] New version available: "
//                                    + UpdateCheck.latestVersion).withStyle(ChatFormatting.YELLOW)
//                    );

                    event.getPlayer().sendSystemMessage(
                            Component.literal("[Warrior Cats Events] New ")
                                    .append(
                                            Component.literal("version")
                                                    .withStyle(style -> style.withColor(ChatFormatting.GREEN)
                                                            .withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                                                                    "https://modrinth.com/mod/warrior-cats-events"
                                                            ))
                                                    )
                                    )
                                    .append(" available: "
                                            + UpdateCheck.latestVersion).withStyle(style -> style.withColor(TextColor.fromRgb(0xfffb00))
                                    ));


                }
            }
        }


    }

    @Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {


        /*
        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ModModelLayers.VANILLAWCAT_LAYER, VanillaWCatModel::createBodyLayer);
        }
        */
        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.FRESH_KILL_PILE.get(), FreshkillPileRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.MOSS_BED.get(), MossBedRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.STONE_TABLE.get(), StoneTableRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.TREE_STUMP.get(), TreeStumpEntityRenderer::new);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(ModKeybinds.HISSING_KEY);
            event.register(ModKeybinds.WATERDRINK_KEY);
            event.register(WCEClient.EMOTES_HUD_MENU_KEY);
//            event.register(ModKeybinds.SKILLMENU_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelowAll("thirst", ThirstHUD.HUD_THIRST);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.MOUSE.get(), MouseRenderer::new);
            event.registerEntityRenderer(ModEntities.SQUIRREL.get(), SquirrelRenderer::new);
            event.registerEntityRenderer(ModEntities.WCAT.get(), WCRenderer::new);
            event.registerEntityRenderer(ModEntities.PIGEON.get(), PigeonRenderer::new);
            event.registerEntityRenderer(ModEntities.BADGER.get(), BadgerRenderer::new);
            event.registerEntityRenderer(ModEntities.EAGLE.get(), EagleRenderer::new);

        }

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(WCEParticles.SLEEP.get(), ParticleSleep.Factory::new);
            event.registerSpriteSet(WCEParticles.LAVENDER.get(), ParticleLavender.Factory::new);
            event.registerSpriteSet(WCEParticles.HERBS.get(), ParticleHerbs.Factory::new);
            event.registerSpriteSet(WCEParticles.HERBS_FALL.get(), ParticleHerbsFall.Factory::new);
            event.registerSpriteSet(WCEParticles.FOOTPRINT.get(), ParticleFootprint.Factory::new);
        }

        /**
         * This allows the leaf door to change colors depending on the biome.
         */
        @SubscribeEvent
        public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
            event.register(
                    (state, world, pos, tintIndex) -> {
                        return world != null && pos != null
                                ? BiomeColors.getAverageFoliageColor(world, pos)
                                : FoliageColor.getDefaultColor();
                    },
                    ModBlocks.LEAF_DOOR.get(), ModBlocks.LEAF_TRAPDOOR.get(), ModBlocks.MAKESHIFT_BED.get()
            );
        }


        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.STONECLEFT_MENU.get(), StoneCleftScreen::new);
            MenuScreens.register(ModMenuTypes.FRESHKILL_PILE_MENU.get(), FreshkillPileScreen::new);
            MenuScreens.register(ModMenuTypes.WCAT_INVENTORY.get(), WCatScreen::new);

            UpdateCheck.checkForUpdates();

            ClientStoredMorphs.load();

            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (mc, parent) -> {
                                WCEClient.playLocalSound(ModSounds.MENU_OPEN.get(), SoundSource.NEUTRAL, 1.0f, 1.3f);
                                return new WCEConfigScreen(parent);
                            }
                    )
            );

            ItemProperties.register(ModItems.ANCIENT_STICK.get(),
                    new ResourceLocation(WarriorCatsEvents.MODID, "dismiss_active"),
                    (stack, level, entity, seed) -> {
                        if (stack.hasTag() && stack.getTag().getBoolean("dismissClanSwitchActive")) {
                            return 1.0F;
                        }
                        return 0.0F;
                    }
            );


            {
                for (RegistryObject<Item> collar : Arrays.asList(
                        ModItems.BLACK_CAT_COLLAR, ModItems.BROWN_CAT_COLLAR, ModItems.WHITE_CAT_COLLAR,
                        ModItems.PINK_CAT_COLLAR, ModItems.ORANGE_CAT_COLLAR, ModItems.RED_CAT_COLLAR,
                        ModItems.BLUE_CAT_COLLAR, ModItems.PURPLE_CAT_COLLAR)) {

                    ItemProperties.register(collar.get(),
                            new ResourceLocation("warriorcats_events", "has_bell"),
                            (stack, level, entity, seed) -> stack.getTag() != null && stack.getTag().getBoolean("HasBell") ? 1f : 0f);

                    ItemProperties.register(collar.get(),
                            new ResourceLocation("warriorcats_events", "has_spikes"),
                            (stack, level, entity, seed) -> stack.getTag() != null && stack.getTag().getBoolean("HasSpikes") ? 1f : 0f);

                    ItemProperties.register(collar.get(),
                            new ResourceLocation("warriorcats_events", "has_glow"),
                            (stack, level, entity, seed) -> stack.getTag() != null && stack.getTag().getBoolean("HasGlow") ? 1f : 0f);
                }
            }



            ModPackets.INSTANCE.registerMessage(
                    13,
                    StCFishingScreenPacket.class,
                    (pkt, buf) -> pkt.toBytes(buf),
                    StCFishingScreenPacket::new,
                    (pkt, ctxSupplier) -> {
                        ctxSupplier.get().enqueueWork(() -> {
                            Minecraft.getInstance().setScreen(new FishingScreen());
                        });
                        ctxSupplier.get().setPacketHandled(true);
                    }
            );

            ModPackets.INSTANCE.registerMessage(
                    20,
                    OpenClanSetupScreenPacket.class,
                    (pkt, buf) -> pkt.toBytes(buf),
                    OpenClanSetupScreenPacket::new,
                    (pkt, ctxSupplier) -> {
                        ctxSupplier.get().enqueueWork(() -> {
                            Minecraft.getInstance().setScreen(new ClanSetupScreen());
                        });
                        ctxSupplier.get().setPacketHandled(true);
                    }
            );

            ModPackets.INSTANCE.registerMessage(
                    32,
                    OpenCatDataScreenPacket.class,
                    (pkt, buf) -> pkt.toBytes(buf),
                    OpenCatDataScreenPacket::new,
                    (pkt, ctxSupplier) -> {
                        ctxSupplier.get().enqueueWork(() -> {
                            Minecraft mc = Minecraft.getInstance();
                            if (mc.level == null) return;

                            var entity = mc.level.getEntity(pkt.catId);
                            if (entity instanceof WCatEntity cat) {
                                mc.setScreen(new CatDataScreen(cat.getDisplayName(), cat));
                            }
                        });

                        ctxSupplier.get().setPacketHandled(true);
                    }
            );


        }

    }

}

