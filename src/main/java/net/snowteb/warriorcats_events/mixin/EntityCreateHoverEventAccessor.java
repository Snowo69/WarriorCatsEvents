package net.snowteb.warriorcats_events.mixin;

import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityCreateHoverEventAccessor {

    @Invoker("createHoverEvent")
    HoverEvent wce$createHoverEvent();

}
