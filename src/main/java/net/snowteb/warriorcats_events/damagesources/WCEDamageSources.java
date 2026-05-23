package net.snowteb.warriorcats_events.damagesources;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class WCEDamageSources {

    public static DamageSource greencough(Level level) {
        Registry<DamageType> registry =
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);

        return new DamageSource(registry.getHolderOrThrow(WCEDamageTypes.GREENCOUGH));
    }

    public static DamageSource whitecough(Level level) {
        Registry<DamageType> registry =
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);

        return new DamageSource(registry.getHolderOrThrow(WCEDamageTypes.WHITECOUGH));
    }

    public static DamageSource deathberries(Level level) {
        Registry<DamageType> registry =
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);

        return new DamageSource(registry.getHolderOrThrow(WCEDamageTypes.DEATHBERRIES));
    }

}
