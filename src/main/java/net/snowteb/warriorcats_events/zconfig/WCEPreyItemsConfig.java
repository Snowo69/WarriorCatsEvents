package net.snowteb.warriorcats_events.zconfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WCEPreyItemsConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> STRING_ITEMS;

    public static final Set<Item> PREY_ITEMS = new HashSet<>();

    static {

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Prey Items Config")
                .push("carry");

        STRING_ITEMS = builder
                .comment("Items that are considered prey and should feed more than usual",
                        "Use the same format shown below")
                .defineListAllowEmpty(
                        "items",
                        List.of("minecraft:salmon", "minecraft:cod"),
                        o -> o instanceof String
                );

        builder.pop();

        SPEC = builder.build();
    }

    public static void getItemListFromString() {
        PREY_ITEMS.clear();
        for (String id : STRING_ITEMS.get()) {
            ResourceLocation location = new ResourceLocation(id);

            Item item = ForgeRegistries.ITEMS.getValue(location);
            if (item != null) {
                PREY_ITEMS.add(item);
            }
        }
    }

}
