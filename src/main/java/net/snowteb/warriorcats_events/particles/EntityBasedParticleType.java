package net.snowteb.warriorcats_events.particles;


import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;

public class EntityBasedParticleType extends SimpleParticleType {

    public LivingEntity entity;

    public EntityBasedParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }

    public ParticleOptions setEntity(LivingEntity entity) {
        this.entity = entity;
        return this;
    }

}


