package net.snowteb.warriorcats_events.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.snowteb.warriorcats_events.block.custom.MossBedBlock;
import net.snowteb.warriorcats_events.entity.custom.EagleEntity;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tocraft.walkers.api.PlayerShape;

@Mixin(Player.class)
public class PlayerEntityMixin {

    @Inject(method = "rideTick", at = @At("TAIL"))
    public void redirectRidePosition(CallbackInfo ci) {

        Player self = (Player) (Object) this;

        if (self.getVehicle() != null) {
            Entity entityVehicle = self.getVehicle();

            if (entityVehicle instanceof Player catVehicle && PlayerShape.getCurrentShape(self) instanceof WCatEntity) {
                float yawDeg;
                float pitchDeg;

                yawDeg = catVehicle.yBodyRot;
                pitchDeg = catVehicle.getXRot();

                double yaw = Math.toRadians(-yawDeg);

                double dirX = Math.sin(yaw);
                double dirZ = Math.cos(yaw);

                double distance = 0.66;

                double offsetY = 0.15;

                double pitch = Math.toRadians(pitchDeg);

                double verticalOffset = Math.sin(-pitch) * 0.4;

                double offsetX = dirX * distance + (verticalOffset / 5);
                double offsetZ = dirZ * distance + (verticalOffset / 5);

                self.setPos(
                        catVehicle.getX() + offsetX,
                        catVehicle.getY() + offsetY + verticalOffset,
                        catVehicle.getZ() + offsetZ
                );

                float sideYaw = yawDeg + 200F;

                self.setOnGround(false);
                self.setYRot(sideYaw);
                self.setYHeadRot(sideYaw);
            }

        }

    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void negateWallDamage(DamageSource source, CallbackInfoReturnable<Boolean> ci) {
        if (source.is(DamageTypes.IN_WALL)) {
            Player self = (Player) (Object) this;
            if (self.getVehicle() instanceof Player) {
                ci.setReturnValue(true);
            }
        }
    }

    @Inject(method = "wantsToStopRiding", at = @At("HEAD"), cancellable = true)
    public void blockStopRiding(CallbackInfoReturnable<Boolean> cir) {
        Player player =  (Player) (Object) this;
        if (player.getVehicle() != null) {
            if (player.getVehicle() instanceof EagleEntity eagle && eagle.isLatching()) {
                cir.setReturnValue(false);
            }
        }
    }

}
