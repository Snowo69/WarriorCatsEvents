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
                Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID,"weak_paws"),
                -1.5, AttributeModifier.Operation.ADD_VALUE
        );
    }
}
