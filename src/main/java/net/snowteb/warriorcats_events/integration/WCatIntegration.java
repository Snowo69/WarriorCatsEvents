package net.snowteb.warriorcats_events.integration;

import net.snowteb.warriorcats_events.entity.ModEntities;
import tocraft.walkers.api.variant.TypeProviderRegistry;
import tocraft.walkers.integrations.AbstractIntegration;

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




}
