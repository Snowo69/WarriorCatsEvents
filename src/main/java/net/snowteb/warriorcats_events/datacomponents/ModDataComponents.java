package net.snowteb.warriorcats_events.datacomponents;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class ModDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, WarriorCatsEvents.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HAS_BELL =
            DATA_COMPONENTS.registerComponentType(
                    "has_bell",
                    builder -> builder.persistent(Codec.BOOL)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HAS_SPIKES =
            DATA_COMPONENTS.registerComponentType(
                    "has_spikes",
                    builder -> builder.persistent(Codec.BOOL)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HAS_GLOW =
            DATA_COMPONENTS.registerComponentType(
                    "has_glow",
                    builder -> builder.persistent(Codec.BOOL)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> HONEY_LEVEL =
            DATA_COMPONENTS.registerComponentType(
                    "honeylevel",
                    builder -> builder.persistent(Codec.INT)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WATER_LEVEL =
            DATA_COMPONENTS.registerComponentType(
                    "waterlevel",
                    builder -> builder.persistent(Codec.INT)
            );

    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }
}
