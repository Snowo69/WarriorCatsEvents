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

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("wce_client");

            FISHING_SCREEN_Y_OFFSET = builder
                    .comment("If your fishing GUI covers anything, use this to vertically move it (min: -500, max: 500)")
                    .defineInRange("fishingScreenYOffset", 0, -500, 500);


            builder.pop();
        }
    }
}

