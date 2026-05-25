package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffectInstance;
import net.snowteb.warriorcats_events.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Shadow
    Minecraft minecraft;

    @Inject(method = "getDarknessGamma", at = @At("HEAD"), cancellable = true)
    public void redirectDarknessPulsation(float pPartialTicks, CallbackInfoReturnable<Float> cir) {
        if (this.minecraft.player.hasEffect(ModEffects.TIRED)) {
            MobEffectInstance mobeffectinstance = this.minecraft.player.getEffect(ModEffects.TIRED);
            if (mobeffectinstance != null) {
                cir.setReturnValue(mobeffectinstance.getBlendFactor(this.minecraft.player, pPartialTicks)*1.5f);
                cir.cancel();
            }
        }
    }
}
