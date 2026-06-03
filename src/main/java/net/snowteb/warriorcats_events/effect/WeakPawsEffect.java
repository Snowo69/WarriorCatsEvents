package net.snowteb.warriorcats_events.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class WeakPawsEffect extends MobEffect {
    protected WeakPawsEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE, "7f5d0d4b-1a2c-4e69-b7f1-9f8cbdfb2a1d",
                -1.5, AttributeModifier.Operation.ADDITION
        );
    }
}
