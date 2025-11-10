package net.snowteb.warriorcats_events.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodHerbs {

    // Traveling herb. Can also build up appetite and quench thirst.
    public static final FoodProperties SORREL = new FoodProperties.Builder().alwaysEat().fast()
            .nutrition(1).saturationMod(10f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION,140,0),1f)
            .build();

    // A traveling herb. The leaves are swallowed. Used to give strength. Good for expecting queens.
    public static final FoodProperties BURNET = new FoodProperties.Builder().alwaysEat().fast()
            .nutrition(1).saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST,450,0),0.6f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION,140,0),1f)
            .build();

    // Strengthens the heart and soothes the mind. Also given to traveling cats for strength.
    public static final FoodProperties CHAMOMILE = new FoodProperties.Builder().alwaysEat().fast()
            .nutrition(1).saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,450,0),0.6f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION,140,0),1f)
            .build();

    // Chewed into a paste. Eases the pain of aching joints, such as back pain. It is also a traveling herb.
    public static final FoodProperties DAISY = new FoodProperties.Builder().alwaysEat().fast()
            .nutrition(1).saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,450,0),0.6f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION,140,0),1f)
            .build();

    // Traveling herbs bundle
    public static final FoodProperties TRAVELING_HERBS = new FoodProperties.Builder().alwaysEat().fast()
            .nutrition(1).saturationMod(14f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,6000,0),1f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,6000,0),1f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST,6000,0),1f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION,140,0),1f)
            .build();

}
