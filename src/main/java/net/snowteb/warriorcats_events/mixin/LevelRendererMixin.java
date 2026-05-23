package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.snowteb.warriorcats_events.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "doesMobEffectBlockSky", at = @At("HEAD"), cancellable = true, remap = false)
    private void doesMobEffectBlockSky(Camera pCamera, CallbackInfoReturnable<Boolean> cir){
        Entity ent = pCamera.getEntity();
        if (ent instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(ModEffects.TIRED)) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        }
    }


}
