package net.snowteb.warriorcats_events.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EagleEscapistEffect extends MobEffect {
    public EagleEscapistEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel sLevel) {
            sLevel.sendParticles(
                    ParticleTypes.MYCELIUM,
                    entity.getX(), entity.getY(), entity.getZ(),
                    100, 0.7, 0.7, 0.7, 0.02
            );
            return true;
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}

