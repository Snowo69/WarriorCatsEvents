package net.snowteb.warriorcats_events.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.client.ThirstHUD;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.client.MouseRenderer;
import net.snowteb.warriorcats_events.entity.client.SquirrelRenderer;
import net.snowteb.warriorcats_events.entity.client.WCRenderer;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.CtSHissPacket;
import net.snowteb.warriorcats_events.network.packet.ReqSkillDataPacket;
import net.snowteb.warriorcats_events.network.packet.WaterPacket;
import net.snowteb.warriorcats_events.screen.SkillScreen;
import net.snowteb.warriorcats_events.skills.StealthClientState;
import net.snowteb.warriorcats_events.sound.ModSounds;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import net.snowteb.warriorcats_events.util.ModKeybinds;
import tocraft.walkers.api.model.ClassArmProvider;
import tocraft.walkers.api.model.EntityArms;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (ModKeybinds.HISSING_KEY.consumeClick()) {
                ModPackets.sendToServer(new CtSHissPacket());
              }

            if (ModKeybinds.WATERDRINK_KEY.consumeClick()) {
                ModPackets.sendToServer(new WaterPacket());
            }

            if (ModKeybinds.SKILLMENU_KEY.consumeClick()) {
                Minecraft.getInstance().setScreen(new SkillScreen());
                ModPackets.sendToServer(new ReqSkillDataPacket());
            }

        }


        @SubscribeEvent
        public static void onPlaySound(PlaySoundEvent event) {
            SoundInstance sound = event.getSound();

            if (sound != null && sound.getLocation().equals(ModSounds.RIVERCLAN.getId())) {
                Minecraft.getInstance().getSoundManager().stop(null, SoundSource.MUSIC);
            }
        }



        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (mc.player == null) return;

            player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {
                if (!cap.isUnlocked()) return;

                boolean shifting = mc.options.keyShift.isDown();
                StealthClientState.tick(shifting);
            });




        }



        @SubscribeEvent
        public static void onOverlayRender(RenderGuiOverlayEvent.Pre event) {

            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {

                if (!cap.isUnlocked()) return;

                if (cap.isStealthOn() && cap.isUnlocked()) {
                    GuiGraphics gui = event.getGuiGraphics();
                    int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                    int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();

                    gui.fill(0, 0, w, h, 0x71000000);
                }
            });
        }




    }

    @Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

            @SubscribeEvent
            public static void onKeyRegister(RegisterKeyMappingsEvent event) {
                event.register(ModKeybinds.HISSING_KEY);
                event.register(ModKeybinds.WATERDRINK_KEY);
                event.register(ModKeybinds.SKILLMENU_KEY);
            }

            @SubscribeEvent
            public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
                event.registerAboveAll("thirst", ThirstHUD.HUD_THIRST);
            }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.MOUSE.get(), MouseRenderer::new);
            event.registerEntityRenderer(ModEntities.SQUIRREL.get(), SquirrelRenderer::new);
            event.registerEntityRenderer(ModEntities.WCAT.get(), WCRenderer::new);


        }


        @SubscribeEvent
        public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
            event.register(
                    (state, world, pos, tintIndex) -> {
                        return world != null && pos != null
                                ? BiomeColors.getAverageFoliageColor(world, pos)
                                : FoliageColor.getDefaultColor();
                    },
                    ModBlocks.LEAF_DOOR.get()
            );
        }


        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityArms.register(HumanoidModel.class, new ClassArmProvider<HumanoidModel>() {
                @Override
                public ModelPart getArm(LivingEntity entity, HumanoidModel model) {
                    return model.rightArm;
                }
            });
        }



    }

}

