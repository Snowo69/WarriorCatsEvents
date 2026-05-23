package net.snowteb.warriorcats_events.effect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, WarriorCatsEvents.MODID);


    public static final DeferredHolder<MobEffect, MobEffect> NUMB_EFFECT = MOB_EFFECTS.register("numb",
            () -> new NumbEffect(MobEffectCategory.HARMFUL, 0x969595));

    public static final DeferredHolder<MobEffect, MobEffect> DEATHBERRIES = MOB_EFFECTS.register("deathberries",
            () -> new DeathberriesEffect(MobEffectCategory.HARMFUL, 0x969595));

    public static final DeferredHolder<MobEffect, MobEffect> EAGLE_ESCAPIST = MOB_EFFECTS.register("eagle_escapist",
            () -> new EagleEscapistEffect(MobEffectCategory.BENEFICIAL, 0x5F4731));

    public static final DeferredHolder<MobEffect, MobEffect> SHARP_SCENT = MOB_EFFECTS.register("sharp_scent",
            () -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x000000));

    public static final DeferredHolder<MobEffect, MobEffect> SHARP_EYE = MOB_EFFECTS.register("sharp_sight",
            () -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x000000));

    public static final DeferredHolder<MobEffect, MobEffect> THIRSTY = MOB_EFFECTS.register("thirsty",
            () -> new ThirstyEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));

    public static final DeferredHolder<MobEffect, MobEffect> TIRED = MOB_EFFECTS.register("tired",
            () -> new SimpleSlownessEffect(MobEffectCategory.HARMFUL, 0x000000, true, -0.35f));


    public static final DeferredHolder<MobEffect, MobEffect> FEVER = MOB_EFFECTS.register("fever",
            () -> new SimpleSlownessEffect(MobEffectCategory.HARMFUL, 0x000000, true, -0.1f));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
