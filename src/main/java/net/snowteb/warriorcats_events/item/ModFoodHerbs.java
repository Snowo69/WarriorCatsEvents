package net.snowteb.warriorcats_events.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodHerbs {

    public static final FoodProperties SORREL = new FoodProperties.Builder().alwaysEat().fast()
            .nutrition(1).saturationMod(10f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST,6000,0),1)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,6000,0),1)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION,140,0),1)
            .build();



}
