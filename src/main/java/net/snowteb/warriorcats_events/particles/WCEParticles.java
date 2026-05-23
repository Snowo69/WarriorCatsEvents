package net.snowteb.warriorcats_events.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class WCEParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, WarriorCatsEvents.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SLEEP =
            PARTICLES.register("sleep", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LAVENDER =
            PARTICLES.register("lavender", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREENCOUGH =
            PARTICLES.register("greencough", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WHITECOUGH =
            PARTICLES.register("whitecough", () -> new SimpleParticleType(true));


    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HERBS =
            PARTICLES.register("herbs", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HERBS_FALL =
            PARTICLES.register("herbs_fall", () -> new SimpleParticleType(true));



    public static final DeferredHolder<ParticleType<?>, EntityBasedParticleType> FOOTPRINT =
            PARTICLES.register("footprint", () -> new EntityBasedParticleType(true));

    public static void register(IEventBus bus) {
        PARTICLES.register(bus);
    }

}
