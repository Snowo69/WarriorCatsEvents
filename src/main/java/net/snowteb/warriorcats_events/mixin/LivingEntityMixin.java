package net.snowteb.warriorcats_events.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.snowteb.warriorcats_events.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyArg(method = "hurt", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"), index = 1)
    public float reduceFallDamageEffect(DamageSource source, float amount) {
        LivingEntity entity = (LivingEntity)(Object)this;

        if (source.is(DamageTypes.FALL)) {
            if (entity.hasEffect(ModEffects.EAGLE_ESCAPIST.get())) {
                amount = 0.4f*amount;
            }
        }

        return amount;
    }

}
