package net.snowteb.warriorcats_events;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.snowteb.warriorcats_events.sound.ModSounds;
import org.lwjgl.glfw.GLFW;

public class WCEClient {
    public static boolean isRenderingEmoteMenu = false;
    public static int emoteOffset = 0;
    public static final int MAX_EMOTES = 10;
    public static final KeyMapping EMOTES_HUD_MENU_KEY = new KeyMapping("key.warriorcats_events.emotes", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y, "key.category.warriorcats_events.key");


    public static final ResourceLocation WCE_TITLE = new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/wce_title_hd.png");


    public static class EmoteIndexData {

        private static final ResourceLocation ICON_NONE = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/none_icon.png");
        private static final ResourceLocation ICON_MORPH = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/morph_icon.png");
        private static final ResourceLocation ICON_ATTACK = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/attack_icon.png");
        private static final ResourceLocation ICON_GROOM = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/groom_icon.png");
        private static final ResourceLocation ICON_LAY = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/lay_icon.png");
        private static final ResourceLocation ICON_SCRATCH = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/scratch_icon.png");
        private static final ResourceLocation ICON_STAND = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/stand_icon.png");
        private static final ResourceLocation ICON_STRETCH = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/stretch_icon.png");
        private static final ResourceLocation ICON_SIT = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/sit_icon.png");
        private static final ResourceLocation ICON_LOAF = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/loaf_icon.png");
        private static final ResourceLocation ICON_BACKFLIP = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/backflip_icon.png");
        private static final ResourceLocation ICON_SLEEP = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/sleep_icon.png");
        private static final ResourceLocation ICON_DEAD = new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/emotes/dead_icon.png");

        public static ResourceLocation getIcon(int index) {
            return switch (index) {
                case -2 -> ICON_BACKFLIP;
                case -1 -> ICON_MORPH;
                case 0 -> ICON_NONE;
                case 1 -> ICON_GROOM;
                case 2 -> ICON_STRETCH;
                case 3 -> ICON_SCRATCH;
                case 4 -> ICON_ATTACK;
                case 5 -> ICON_STAND;
                case 6 -> ICON_LAY;
                case 7 -> ICON_SIT;
                case 8 -> ICON_LOAF;
                case 9 -> ICON_SLEEP;
                case 10 -> ICON_DEAD;
                default -> ICON_NONE;
            };
        }

        public static String getText(int index) {
            return switch (index) {
                case -2 -> "Backflip";
                case -1 -> "Morph";
                case 0 -> "";
                case 1 -> "Groom";
                case 2 -> "Stretch";
                case 3 -> "Scratch";
                case 4 -> "Attack";
                case 5 -> "Stand";
                case 6 -> "Lay";
                case 7 -> "Sit";
                case 8 -> "Loaf";
                case 9 -> "Sleep";
                case 10 -> "Play dead";
                default -> "Unnamed";
            };
        }

    }

    public static void playLocalSound(SoundEvent sound, SoundSource soundSource, float volume, float pitch) {
        Minecraft mc = Minecraft.getInstance();
        mc.level.playLocalSound(mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                sound, soundSource, volume,pitch, false);
    }

}
