package net.snowteb.warriorcats_events.diseases.kinds;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.snowteb.warriorcats_events.diseases.Disease;
import net.snowteb.warriorcats_events.diseases.Diseaseable;

public class DeathberriesPoisoning extends Disease<DeathberriesPoisoning> {

    @Override
    public <T extends LivingEntity> void onAdd(Diseaseable<T> tDiseaseable, boolean organic) {
        LivingEntity livingEntity = tDiseaseable.getEntity();
        if (!livingEntity.level().isClientSide()) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION,7200,0));
        }
    }

    @Override
    public <T extends LivingEntity> void onRemove(Diseaseable<T> tDiseaseable) {
        LivingEntity livingEntity = tDiseaseable.getEntity();
        if (!livingEntity.level().isClientSide()) {
            livingEntity.removeEffect(MobEffects.CONFUSION);
            livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION,3600,0));
        }
    }
}
