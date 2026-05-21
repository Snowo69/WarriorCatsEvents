package net.snowteb.warriorcats_events.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SimpleSlownessEffect extends MobEffect {
    public SimpleSlownessEffect(MobEffectCategory pCategory, int pColor, boolean slowness, float slownessValue) {
        super(pCategory, pColor);

        if (slowness) {
            this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                    "7f5d3d4b-1a2c-4e69-b7f1-9f8c5d9b6a3d",
                    slownessValue, AttributeModifier.Operation.MULTIPLY_BASE);
        }

    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }

    @Override
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        return pModifier.getAmount();
    }
}

