package net.snowteb.warriorcats_events.zconfig;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WCEPreyItemsConfig {

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<List<? extends String>> STRING_ITEMS;

    public static final Set<Item> PREY_ITEMS = new HashSet<>();

    static {

        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Prey Items Config")
                .push("items");

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
            ResourceLocation location = ResourceLocation.parse(id);

            Item item = BuiltInRegistries.ITEM.get(location);
            PREY_ITEMS.add(item);
        }
    }

}
