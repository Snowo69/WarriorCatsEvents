package net.snowteb.warriorcats_events.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class NumbEffect extends MobEffect {
    public NumbEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        /**
         * When this effect is active, it applied an Attribute of negative movement speed.
         */
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "7f5d3d4b-1a2c-4e28-b7f1-9f8c5d9b6a3d",
                -1.0F, AttributeModifier.Operation.MULTIPLY_BASE);

    }

    /**
     * If the entity's movement in Y is positive, and it's not in water, then set its Y movement to zero.
     */
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity.getDeltaMovement().y > 0 && !entity.isInWaterOrBubble()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 1));
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }


}

