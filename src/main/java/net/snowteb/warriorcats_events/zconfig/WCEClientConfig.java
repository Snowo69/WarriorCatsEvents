package net.snowteb.warriorcats_events.zconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class WCEClientConfig {

    public static final ForgeConfigSpec SPEC;
    public static final Client CLIENT;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        CLIENT = new Client(builder);
        SPEC = builder.build();
    }

    public static class Client {

        public final ForgeConfigSpec.IntValue FISHING_SCREEN_Y_OFFSET;
        public final ForgeConfigSpec.BooleanValue LEAP;
        public final ForgeConfigSpec.BooleanValue OWN_MORPH_NAME;
        public final ForgeConfigSpec.BooleanValue AMBIENT_MUSIC;
        public final ForgeConfigSpec.BooleanValue MORPH_CHAT_BUBBLES;
        public final ForgeConfigSpec.BooleanValue OWN_CHAT_BUBBLES;
        public final ForgeConfigSpec.BooleanValue DISPLAY_TERRITORY;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("wce_client");

            FISHING_SCREEN_Y_OFFSET = builder
                    .comment("If your fishing GUI covers anything, use this to vertically move it (min: -500, max: 500)")
                    .defineInRange("fishingScreenYOffset", 0, -500, 500);

            LEAP = builder
                    .comment("Whether leaping is enabled for you")
                    .define("leap", true);

            OWN_MORPH_NAME = builder
                    .comment("Whether your own morph nametag is visible")
                    .define("showNametag", true);

            AMBIENT_MUSIC = builder
                    .comment("Whether ambient music from the mod will play for you")
                    .define("cuteAmbientMusic", true);

            MORPH_CHAT_BUBBLES = builder
                    .comment("Whether other player's morphs will display their chat messages")
                    .define("entityChatBubbles", true);

            OWN_CHAT_BUBBLES = builder
                    .comment("Whether your own chat bubble will display")
                    .define("ownChatBubbles", true);

            DISPLAY_TERRITORY = builder
                    .comment("Whether territory info will display on your HUD")
                    .define("displayTerritory", true);

            builder.pop();
        }
    }
}

