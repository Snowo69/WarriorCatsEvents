package net.snowteb.warriorcats_events.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import javax.swing.text.Keymap;

public class ModKeybinds {
    public static final String KEY_CATEGORY_WARRIORCATSEVENTS = "key.category.warriorcats_events.key";
    public static final String KEY_HISS = "key.warriorcats_events.hiss";
    public static final String KEY_WATER = "key.warriorcats_events.water";

    public static final KeyMapping HISSING_KEY = new KeyMapping(KEY_HISS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_WARRIORCATSEVENTS);

    public static final KeyMapping WATERDRINK_KEY = new KeyMapping(KEY_WATER, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KEY_CATEGORY_WARRIORCATSEVENTS);
}
