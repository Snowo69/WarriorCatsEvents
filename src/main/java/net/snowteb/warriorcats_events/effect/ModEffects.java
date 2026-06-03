package net.snowteb.warriorcats_events.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, WarriorCatsEvents.MODID);


    public static final RegistryObject<MobEffect> NUMB_EFFECT = MOB_EFFECTS.register("numb",
            () -> new NumbEffect(MobEffectCategory.HARMFUL, 0x969595));

    public static final RegistryObject<MobEffect> DEATHBERRIES = MOB_EFFECTS.register("deathberries",
            () -> new DeathberriesEffect(MobEffectCategory.HARMFUL, 0x969595));

    public static final RegistryObject<MobEffect> EAGLE_ESCAPIST = MOB_EFFECTS.register("eagle_escapist",
            () -> new EagleEscapistEffect(MobEffectCategory.BENEFICIAL, 0x5F4731));

    public static final RegistryObject<MobEffect> SHARP_SCENT = MOB_EFFECTS.register("sharp_scent",
            () -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x000000));

    public static final RegistryObject<MobEffect> SHARP_EYE = MOB_EFFECTS.register("sharp_sight",
            () -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x000000));

    public static final RegistryObject<MobEffect> THIRSTY = MOB_EFFECTS.register("thirsty",
            () -> new ThirstyEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));

    public static final RegistryObject<MobEffect> TIRED = MOB_EFFECTS.register("tired",
            () -> new SimpleSlownessEffect(MobEffectCategory.HARMFUL, 0x000000, true, -0.35f).setFactorDataFactory(
                    () -> new MobEffectInstance.FactorData(40)
            ));

    public static final RegistryObject<MobEffect> FEVER = MOB_EFFECTS.register("fever",
            () -> new SimpleSlownessEffect(MobEffectCategory.HARMFUL, 0x000000, true, -0.1f).setFactorDataFactory(
                    () -> new MobEffectInstance.FactorData(20)
            ));

    public static final RegistryObject<MobEffect> WEAK_PAWS = MOB_EFFECTS.register("weak_paws",
            () -> new WeakPawsEffect(MobEffectCategory.HARMFUL, 0x555555));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
