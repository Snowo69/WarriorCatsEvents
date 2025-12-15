package net.snowteb.warriorcats_events.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class DeathberriesEffect extends MobEffect {
    public DeathberriesEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "7f5d3d4b-1a2c-4e69-b7f1-9f8c5d9b6a3d",
                -0.7F, AttributeModifier.Operation.MULTIPLY_BASE);

    }

    /**
     * Hurt the entity every time this is called.
     * And if it's a player, then remove its saturation as well
     */
    public void applyEffectTick(LivingEntity entity, int amplifier) {
            entity.hurt(entity.damageSources().magic(), 1.2F + amplifier);
        if (entity instanceof Player player) {
            player.getFoodData().setSaturation(0);
        }
    }

    /**
     * Whatever the applyEfectTick does, it will do it every 40 ticks instead of every tick.
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }




}

