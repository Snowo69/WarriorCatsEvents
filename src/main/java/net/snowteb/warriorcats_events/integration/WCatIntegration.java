package net.snowteb.warriorcats_events.integration;

import net.minecraft.world.entity.monster.Creeper;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import tocraft.walkers.ability.AbilityRegistry;
import tocraft.walkers.api.variant.TypeProviderRegistry;
import tocraft.walkers.integrations.AbstractIntegration;
import tocraft.walkers.traits.TraitRegistry;
import tocraft.walkers.traits.impl.FearedTrait;

/**
 * This class allows you to select different variants of Wild Cat to play as.
 */
public class WCatIntegration extends AbstractIntegration {
    public static final String MODID = "warriorcats_events";

    @Override
    public void initialize() {

    }

    @Override
    public void registerTypeProvider() {
        TypeProviderRegistry.register(ModEntities.WCAT.get(), new WCatTypeProvider());
        //TypeProviderRegistry.register(ModEntities.VANILLAWCAT.get(), new VanillaWCatTypeProvider());
        //TypeProviderRegistry.register(ModEntities.PLAYEABLECAT.get(), new PlayeableCatTypeProvider());
    }

    @Override
    public void registerAbilities() {
        AbilityRegistry.registerByClass(WCatEntity.class, new WCatNightVision<>());
    }

    @Override
    public void registerTraits() {
        TraitRegistry.registerByClass(WCatEntity.class, (FearedTrait<WCatEntity>) FearedTrait.ofFearfulClass(Creeper.class));

    }

}
