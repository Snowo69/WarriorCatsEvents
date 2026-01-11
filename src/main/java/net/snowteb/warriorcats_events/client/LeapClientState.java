package net.snowteb.warriorcats_events.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Animal;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.CtSPerformLeapPacket;
import net.snowteb.warriorcats_events.sound.ModSounds;
import tocraft.walkers.api.PlayerShape;

public class LeapClientState {
    private static int shiftKeyDownCounter = 0;
    private static int leapPowerCounter = 0;
    private static boolean leapCanceledInShifting = false;
    private static boolean attackWasDown = false;

    private static final Component STICK_GREEN =
            Component.literal("▋").withStyle(ChatFormatting.GREEN);
    private static final Component STICK_GRAY =
            Component.literal("▋").withStyle(ChatFormatting.DARK_GRAY);


    public static void tick(boolean shifting) {


        if (Minecraft.getInstance().player != null) {
            if (!(PlayerShape.getCurrentShape(Minecraft.getInstance().player) instanceof Animal)) return;
        }

        if (shifting && Minecraft.getInstance().player.onGround()) {
            boolean attackDown = Minecraft.getInstance().options.keyAttack.isDown();

            if (shiftKeyDownCounter > 142 || !Minecraft.getInstance().player.onGround()) return;

            shiftKeyDownCounter++;

            if (shiftKeyDownCounter >= 20) {
                leapPowerCounter+=2;
                leapPowerCounter = Math.min(leapPowerCounter, 100);
                assert Minecraft.getInstance().player != null;
                if (shiftKeyDownCounter <= 140 && Minecraft.getInstance().player.onGround()) {
                    Minecraft.getInstance().player.playSound(ModSounds.SHORT_WOOSH.get(), 0.3f, 0.7f);
//                    Component powerBar = buildLeapPowerBar(leapPowerCounter);
//
//                    Minecraft.getInstance().player.displayClientMessage(
//                            Component.literal("").append(powerBar.copy()), true);
                } else {
                    leapCanceledInShifting = true;
                    Minecraft.getInstance().player.displayClientMessage(
                            Component.literal("Leap canceled").withStyle(ChatFormatting.DARK_GRAY), true);
                }

                if (leapPowerCounter > 0) {
                    if (shiftKeyDownCounter <= 140 && Minecraft.getInstance().player.onGround()
                            && attackDown && !attackWasDown) {
                        ModPackets.sendToServer(new CtSPerformLeapPacket(leapPowerCounter));
                        Minecraft.getInstance().player.displayClientMessage(
                                Component.literal("Leap!").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC), true);
                        leapPowerCounter = 0;
                        shiftKeyDownCounter = 0;
                    }
                }
                attackWasDown = attackDown;

            }

        } else {
            if (leapPowerCounter > 0 && !leapCanceledInShifting) {
                Minecraft.getInstance().player.displayClientMessage(
                        Component.literal("Leap canceled").withStyle(ChatFormatting.DARK_GRAY), true);
            }
            leapPowerCounter = 0;
            shiftKeyDownCounter = 0;
            leapCanceledInShifting = false;

        }



    }


    public static Component buildLeapPowerBar(int leapPowerCounter) {
        int greenCount = Mth.clamp(leapPowerCounter/2, 0, 50);

        MutableComponent bar = Component.empty();

        for (int i = 0; i < greenCount; i++) {
            bar.append(STICK_GREEN);
        }

        for (int i = greenCount; i < 50; i++) {
            bar.append(STICK_GRAY);
        }

        return bar;
    }


    public static int getLeapPowerCounter() {
        return leapPowerCounter;
    }

}
