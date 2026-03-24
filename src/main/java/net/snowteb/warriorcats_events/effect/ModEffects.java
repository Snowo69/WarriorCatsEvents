package net.snowteb.warriorcats_events.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
            () -> new SharpScentEffect(MobEffectCategory.BENEFICIAL, 0x005F4731));


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
