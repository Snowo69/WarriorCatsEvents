package net.snowteb.warriorcats_events.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class NumbEffect extends MobEffect {
    public NumbEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "7f5d3d4b-1a2c-4e28-b7f1-9f8c5d9b6a3d", // único
                -0.99,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }


    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);

        if (entity instanceof Player player) {

            player.setDeltaMovement(player.getDeltaMovement().x,
                    Math.min(player.getDeltaMovement().y, 0),
                    player.getDeltaMovement().z);
        }

    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }


}
