package net.snowteb.warriorcats_events.zconfig;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * This creates a .toml file that allows to change different values.
 */
public class WCEConfig {

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new Common(builder);
        COMMON_SPEC = builder.build();
    }

    public static class Common {

        public Common(ForgeConfigSpec.Builder builder) {

            builder.push("wce_common");

            builder.pop();
        }
    }
}
