package net.snowteb.warriorcats_events.zconfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class WCEClientConfig {

    public static final ModConfigSpec SPEC;
    public static final Client CLIENT;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        CLIENT = new Client(builder);
        SPEC = builder.build();
    }

    public static class Client {

        public final ModConfigSpec.IntValue FISHING_SCREEN_Y_OFFSET;
        public final ModConfigSpec.BooleanValue LEAP;
        public final ModConfigSpec.BooleanValue OWN_MORPH_NAME;
        public final ModConfigSpec.BooleanValue AMBIENT_MUSIC;
        public final ModConfigSpec.BooleanValue MORPH_CHAT_BUBBLES;
        public final ModConfigSpec.BooleanValue OWN_CHAT_BUBBLES;
        public final ModConfigSpec.BooleanValue DISPLAY_TERRITORY;
        public final ModConfigSpec.BooleanValue CUSTOM_PANORAMA;
        public final ModConfigSpec.BooleanValue SERENE_SEASONS_OVERLAY;

        public Client(ModConfigSpec.Builder builder) {
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

            CUSTOM_PANORAMA = builder
                    .comment("Whether WCE panorama will show")
                    .define("wcePanorama", true);

            SERENE_SEASONS_OVERLAY = builder
                    .comment("Whether the current season should display on your screen when Serene Seasons is installed")
                    .define("sereneSeasonsOverlay", true);


            builder.pop();
        }
    }

}

