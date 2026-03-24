package net.snowteb.warriorcats_events.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class WCEParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WarriorCatsEvents.MODID);

    public static final RegistryObject<SimpleParticleType> SLEEP =
            PARTICLES.register("sleep", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> LAVENDER =
            PARTICLES.register("lavender", () -> new SimpleParticleType(true));


    public static final RegistryObject<SimpleParticleType> HERBS =
            PARTICLES.register("herbs", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> HERBS_FALL =
            PARTICLES.register("herbs_fall", () -> new SimpleParticleType(true));



    public static final RegistryObject<EntityBasedParticleType> FOOTPRINT =
            PARTICLES.register("footprint", () -> new EntityBasedParticleType(true));

    public static void register(IEventBus bus) {
        PARTICLES.register(bus);
    }

}
