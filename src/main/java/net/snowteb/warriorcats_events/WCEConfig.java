package net.snowteb.warriorcats_events;

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

        public final ForgeConfigSpec.IntValue WILDCAT_WANDER_RADIUS;
        public final ForgeConfigSpec.IntValue KITTING_MINUTES;
        public final ForgeConfigSpec.IntValue KIT_GROWTH_MINUTES;
        public final ForgeConfigSpec.DoubleValue SKILL_COST_MULTIPLIER;

        public Common(ForgeConfigSpec.Builder builder) {

            builder.push("wce_config");

            WILDCAT_WANDER_RADIUS = builder
                    .comment("The radius in which the Wild Cats will wander (min: 8, max: 100)")
                    .defineInRange("wanderRadius", 15, 8, 100);

            KITTING_MINUTES = builder
                    .comment("The time in minutes it takes until she-cats give birth (min: 1, max: 120)")
                    .defineInRange("kittingMinutes", 10, 1, 120);

            KIT_GROWTH_MINUTES = builder
                    .comment("The time in minutes it takes for a kit to grow from kit to warrior (min: 10, max: 180)")
                    .defineInRange("kitGrowthMinutes", 90, 10, 180);

            SKILL_COST_MULTIPLIER = builder
                    .comment("The cost multiplier that will define the skill's cost (min: 0.2, max: 3)")
                    .defineInRange("skillCostValue", 1.0, 0.2, 3.0);


            builder.pop();
        }
    }
}
