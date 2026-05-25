package net.snowteb.warriorcats_events.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class SimpleSlownessEffect extends MobEffect {
    public SimpleSlownessEffect(MobEffectCategory pCategory, int pColor, boolean slowness, float slownessValue) {
        super(pCategory, pColor);

        if (slowness) {
            this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                    ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "slowness"),
                    slownessValue, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        }

    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }

    @Override
    public int getBlendDurationTicks() {
        return 40;
    }
}

