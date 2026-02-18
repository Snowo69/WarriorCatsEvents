package net.snowteb.warriorcats_events.zconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class WCEServerConfig {

    public static final ForgeConfigSpec SPEC;
    public static final Server SERVER;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        SERVER = new Server(builder);
        SPEC = builder.build();
    }

    public static class Server {

        public final ForgeConfigSpec.IntValue WILDCAT_WANDER_RADIUS;
        public final ForgeConfigSpec.IntValue KITTING_MINUTES;
        public final ForgeConfigSpec.IntValue KIT_GROWTH_MINUTES;
        public final ForgeConfigSpec.DoubleValue SKILL_COST_MULTIPLIER;
        public final ForgeConfigSpec.BooleanValue ENHANCED_ANIMALS;
        public final ForgeConfigSpec.BooleanValue COLORED_NAMES;
        public final ForgeConfigSpec.BooleanValue VISIBLE_MORPH_NAME;


        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("wce_server");

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

            SKILL_COST_MULTIPLIER = builder
                    .comment("The cost multiplier that will define the skill's cost (min: 0.2, max: 3)")
                    .defineInRange("skillCostValue", 1.0, 0.2, 3.0);

            ENHANCED_ANIMALS = builder
                    .comment("Should wolves and foxes ignore their own behaviour and be aggressive towards Wild Cats and players instead?")
                    .define("aggroAnimals", true);

            VISIBLE_MORPH_NAME = builder
                    .comment("Whether your own morph's name should be visible")
                    .define("visibleMorphName", true);

            builder.pop();
        }
    }
}

