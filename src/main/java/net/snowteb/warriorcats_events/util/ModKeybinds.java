package net.snowteb.warriorcats_events.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    public static final String KEY_CATEGORY_WARRIORCATSEVENTS = "key.category.warriorcats_events.key";
    public static final String KEY_HISS = "key.warriorcats_events.hiss";
    public static final String KEY_WATER = "key.warriorcats_events.water";
    public static final String KEY_CLIMB = "key.warriorcats_events.climb";
    public static final String KEY_OPTIONS = "key.warriorcats_events.options";
    public static final String KEY_BACKPACK = "key.warriorcats_events.backpack";
    public static final String KEY_LEAP = "key.warriorcats_events.leap";
    public static final String KEY_LOCK_TARGET = "key.warriorcats_events.lock_target";

    public static final KeyMapping SOUND_MENU_KEY = new KeyMapping(KEY_HISS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, KEY_CATEGORY_WARRIORCATSEVENTS);

    public static final KeyMapping WATERDRINK_KEY = new KeyMapping(KEY_WATER, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY_WARRIORCATSEVENTS);

    public static final KeyMapping CLIMB_KEY = new KeyMapping(KEY_CLIMB, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY_WARRIORCATSEVENTS);

    public static final KeyMapping OPTIONS_KEY = new KeyMapping(KEY_OPTIONS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, KEY_CATEGORY_WARRIORCATSEVENTS);

    public static final KeyMapping BACKPACK_KEY = new KeyMapping(KEY_BACKPACK, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, KEY_CATEGORY_WARRIORCATSEVENTS);

    public static final KeyMapping LEAP_KEY = new KeyMapping(KEY_LEAP, KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT, KEY_CATEGORY_WARRIORCATSEVENTS);

    public static final KeyMapping LOCK_TARGET_KEY = new KeyMapping(KEY_LOCK_TARGET, KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, KEY_CATEGORY_WARRIORCATSEVENTS);

}
