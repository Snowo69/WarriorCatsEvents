package net.snowteb.warriorcats_events.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class NumbEffect extends MobEffect {
    public NumbEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        /**
         * When this effect is active, it applied an Attribute of negative movement speed.
         */
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "numb"),
                -1.0F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    }

    /**
     * If the entity's movement in Y is positive, and it's not in water, then set its Y movement to zero.
     *
     * @return
     */
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity.getDeltaMovement().y > 0 && !entity.isInWaterOrBubble()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 1));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}

