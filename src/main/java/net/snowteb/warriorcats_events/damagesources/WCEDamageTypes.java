package net.snowteb.warriorcats_events.damagesources;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class WCEDamageTypes {

    public static final ResourceKey<DamageType> GREENCOUGH =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "greencough"));

    public static final ResourceKey<DamageType> WHITECOUGH =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "whitecough"));

    public static final ResourceKey<DamageType> DEATHBERRIES =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "deathberries"));

}
