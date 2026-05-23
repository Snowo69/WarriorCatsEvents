package net.snowteb.warriorcats_events.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.damagesources.WCEDamageSources;

public class DeathberriesEffect extends MobEffect {
    public DeathberriesEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "deathberries"),
                -0.4F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    }

    /**
     * Hurt the entity every time this is called.
     * And if it's a player, then remove its saturation as well
     *
     * @return
     */
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {

        entity.hurt(WCEDamageSources.deathberries(entity.level()), 1.0F + (amplifier*1.2f));

        if (entity instanceof Player player) {
            player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() - 3*amplifier);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }

}

