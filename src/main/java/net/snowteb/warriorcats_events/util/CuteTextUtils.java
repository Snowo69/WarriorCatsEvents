package net.snowteb.warriorcats_events.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import net.snowteb.warriorcats_events.mixin.EntityCreateHoverEventAccessor;

import java.util.Map;

public class CuteTextUtils {

    public static Component reformatToSmallText(Entity player, boolean useFancyFont) {
        if (!useFancyFont) {
            return player.getDisplayName();
        }

        String componentText = player.getDisplayName().getString().toLowerCase();

        Map<String, String> map = Map.ofEntries(
                Map.entry("a", "ᴀ"), Map.entry("b", "ʙ"), Map.entry("c", "ᴄ"),
                Map.entry("d", "ᴅ"), Map.entry("e", "ᴇ"), Map.entry("f", "ꜰ"),
                Map.entry("g", "ɢ"), Map.entry("h", "ʜ"), Map.entry("i", "ɪ"),
                Map.entry("j", "ᴊ"), Map.entry("k", "ᴋ"), Map.entry("l", "ʟ"),
                Map.entry("m", "ᴍ"), Map.entry("n", "ɴ"), Map.entry("o", "ᴏ"),
                Map.entry("p", "ᴘ"), Map.entry("q", "ǫ"), Map.entry("r", "ʀ"),
                Map.entry("s", "ѕ"), Map.entry("t", "ᴛ"), Map.entry("u", "ᴜ"),
                Map.entry("v", "ᴠ"), Map.entry("w", "ᴡ"), Map.entry("x", "х"),
                Map.entry("y", "ʏ"), Map.entry("z", "ᴢ")
        );

        for (Map.Entry<String, String> entry : map.entrySet()) {
            componentText = componentText.replace(entry.getKey(), entry.getValue());
        }

        return PlayerTeam.formatNameForTeam(player.getTeam(), Component.literal(componentText)).withStyle((p_185975_) -> {
            return p_185975_.withHoverEvent(((EntityCreateHoverEventAccessor)(player)).wce$createHoverEvent()).withInsertion(player.getStringUUID());
        });
    }


}
