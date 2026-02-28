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


            builder.pop();
        }
    }
}

