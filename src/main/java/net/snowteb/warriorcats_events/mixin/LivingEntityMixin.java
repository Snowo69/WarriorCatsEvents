package net.snowteb.warriorcats_events.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.snowteb.warriorcats_events.effect.ModEffects;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.walkers.api.PlayerShape;

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

//    /**
//     * @author Klyonstar
//     * @reason pq si
//     */
//    @Overwrite
//    private boolean checkBedExists() {
//        return true;
//    }

    @Inject(method = "startSleeping", at = @At("TAIL"))
    public void startSleep(BlockPos pPos, CallbackInfo ci) {
        LivingEntity entity =  (LivingEntity) (Object) this;
        if (entity instanceof ServerPlayer sPlayer) {
            if (PlayerShape.getCurrentShape(sPlayer) instanceof WCatEntity catShape) {
                catShape.setAnimIndex(9);
                PlayerShape.updateShapes(sPlayer, catShape);
                sPlayer.getPersistentData().putInt("wcat_animation_playing", sPlayer.server.getTickCount() + 10);
            }
        }
    }
}
