package net.snowteb.warriorcats_events.integration;

//import net.snowteb.warriorcats_events.entity.client.PlayeableCatTypeProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import tocraft.walkers.api.PlayerShape;
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
        //TypeProviderRegistry.register(ModEntities.PLAYEABLECAT.get(), new PlayeableCatTypeProvider());
    }


}
