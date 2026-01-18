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
        public final ForgeConfigSpec.BooleanValue ENHANCED_ANIMALS;
        public final ForgeConfigSpec.IntValue FISHING_SCREEN_Y_OFFSET;
        public final ForgeConfigSpec.BooleanValue COLORED_NAMES;
        public final ForgeConfigSpec.BooleanValue VISIBLE_MORPH_NAME;


        public Common(ForgeConfigSpec.Builder builder) {

            builder.push("wce_config");

            WILDCAT_WANDER_RADIUS = builder
                    .comment("The radius in which the Wild Cats will wander (min: 8, max: 100)")
                    .defineInRange("wanderRadius", 16, 8, 64);

            KITTING_MINUTES = builder
                    .comment("The time in minutes it takes until she-cats give birth (min: 1, max: 120)")
                    .defineInRange("kittingMinutes", 10, 1, 120);

            KIT_GROWTH_MINUTES = builder
                    .comment("The time in minutes it takes for a kit to grow from kit to warrior (min: 10, max: 960)")
                    .defineInRange("kitGrowthMinutes", 120, 10, 960);

            COLORED_NAMES = builder
                    .comment("Whether Wild Cats' names have a different color depending on their rank")
                    .define("coloredNames", true);

            VISIBLE_MORPH_NAME = builder
                    .comment("Whether your own morph's name should be visible")
                    .define("visibleMorphName", true);

            SKILL_COST_MULTIPLIER = builder
                    .comment("The cost multiplier that will define the skill's cost (min: 0.2, max: 3)")
                    .defineInRange("skillCostValue", 1.0, 0.2, 3.0);

            ENHANCED_ANIMALS = builder
                    .comment("Should wolves and foxes ignore their own behaviour and be aggressive towards Wild Cats and players instead?")
                            .define("aggroAnimals", true);

            FISHING_SCREEN_Y_OFFSET = builder
                    .comment("If your fishing GUI covers anything, use this to vertically move it (min: -500, max: 500)")
                    .defineInRange("fishingScreenYOffset", 0, -500, 500);

            builder.pop();
        }
    }
}
