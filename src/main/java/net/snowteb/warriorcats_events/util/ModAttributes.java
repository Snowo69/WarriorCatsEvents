package net.snowteb.warriorcats_events.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import org.w3c.dom.Attr;

/**
 * Custom attribute that modifies the generic player jump
 */
public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, WarriorCatsEvents.MODID);

    public static final DeferredHolder<Attribute, Attribute> PLAYER_JUMP = ATTRIBUTES.register(
            "player_jump",
            () -> new RangedAttribute(
                    "attribute.name.generic.player_jump",
                    0.0,
                    0.0,
                    1024.0
            ).setSyncable(true)
    );
}
