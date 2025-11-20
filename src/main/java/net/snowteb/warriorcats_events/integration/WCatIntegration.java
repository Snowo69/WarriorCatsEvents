package net.snowteb.warriorcats_events.integration;

import net.snowteb.warriorcats_events.entity.client.WCatTypeProvider;
import net.snowteb.warriorcats_events.entity.ModEntities;
import tocraft.walkers.api.variant.TypeProviderRegistry;
import tocraft.walkers.integrations.AbstractIntegration;

public class WCatIntegration extends AbstractIntegration {
    public static final String MODID = "warriorcats_events";

    @Override
    public void initialize() {
        System.out.println("WCat Integration loaded");
    }

    @Override
    public void registerTypeProvider() {
        TypeProviderRegistry.register(ModEntities.WCAT.get(), new WCatTypeProvider());
    }
}
