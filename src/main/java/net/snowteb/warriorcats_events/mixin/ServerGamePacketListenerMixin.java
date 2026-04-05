package net.snowteb.warriorcats_events.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.snowteb.warriorcats_events.entity.custom.EagleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerMixin {

    @Shadow
    public ServerPlayer player;

    @Shadow
    private boolean clientIsFloating;

    @Shadow
    private boolean clientVehicleIsFloating;

    @Shadow
    private int aboveGroundTickCount;

    @Shadow
    private int aboveGroundVehicleTickCount;

    @Inject(method = "tick", at = @At("HEAD"))
    public void avoidKickFromFlyingEagle(CallbackInfo ci) {
        if (this.player.getVehicle() instanceof EagleEntity) {
            this.clientIsFloating = false;
            this.clientVehicleIsFloating = false;
            this.aboveGroundTickCount = 0;
            this.aboveGroundVehicleTickCount = 0;
        }
    }

}
