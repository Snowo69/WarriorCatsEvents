package net.snowteb.warriorcats_events.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.block.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayer.class, priority = 9999)
public class ServerPlayerSleepInBedMixin {

    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    public void cancelSleepIf(ResourceKey<Level> pDimension, BlockPos pPosition, float pAngle, boolean pForced, boolean pSendMessage, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        ServerLevel level = player.serverLevel();
        if (level.getBlockState(pPosition).is(ModBlocks.MAKESHIFT_BED.get())) {
            ci.cancel();
        }
    }

}
