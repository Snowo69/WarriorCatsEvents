package net.snowteb.warriorcats_events.event;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.util.ModKeybinds;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (ModKeybinds.HISSING_KEY.consumeClick()) {
                var player = Minecraft.getInstance().player;
                if (player != null) {
                player.level().playSound(
                        player,
                        player.blockPosition(),
                        SoundEvents.CAT_HISS,
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f
                );
            }
        }

            }

        }

        @Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
        public static class ClientModBusEvents {

            @SubscribeEvent
            public static void onKeyRegister(RegisterKeyMappingsEvent event) {
                event.register(ModKeybinds.HISSING_KEY);
            }
        }

    }

