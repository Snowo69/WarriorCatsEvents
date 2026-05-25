package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.snowteb.warriorcats_events.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void cancelJumpClient(CallbackInfo ci) {
        LocalPlayer localPlayer = (LocalPlayer)(Object)this;
        if (localPlayer == null) return;
        if (localPlayer.hasEffect(ModEffects.NUMB_EFFECT) && localPlayer.input.jumping) {
            localPlayer.input.jumping = false;
        }
    }

    @Inject(method = "serverAiStep", at = @At("HEAD"))
    public void cancelJumpServer(CallbackInfo ci) {
        LocalPlayer localPlayer = (LocalPlayer)(Object)this;
        if (localPlayer == null) return;
        if (localPlayer.hasEffect(ModEffects.NUMB_EFFECT) && localPlayer.input.jumping) {
            localPlayer.input.jumping = false;
        }
    }

}
