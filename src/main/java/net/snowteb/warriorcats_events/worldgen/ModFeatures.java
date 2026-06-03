package net.snowteb.warriorcats_events.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, WarriorCatsEvents.MODID);

    public static final RegistryObject<GlowrocksFeature> GLOWROCKS_FEATURE =
            FEATURES.register("glowrocks_feature", GlowrocksFeature::new);

    public static void register(IEventBus bus) {
        FEATURES.register(bus);
    }

}
