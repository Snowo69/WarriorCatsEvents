package net.snowteb.warriorcats_events.util;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, WarriorCatsEvents.MODID);

    public static final RegistryObject<Attribute> PLAYER_JUMP = ATTRIBUTES.register(
            "player_jump",
            () -> new RangedAttribute(
                    "attribute.name.generic.player_jump",
                    0.0,
                    0.0,
                    1024.0
            ).setSyncable(true)
    );
}
