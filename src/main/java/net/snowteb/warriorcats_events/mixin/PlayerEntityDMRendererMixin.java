//package net.snowteb.warriorcats_events.mixin;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.player.AbstractClientPlayer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.entity.player.PlayerRenderer;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.phys.Vec3;
//import net.snowteb.warriorcats_events.WarriorCatsEvents;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import tocraft.walkers.api.PlayerShape;
//import tocraft.walkers.api.model.EntityUpdater;
//import tocraft.walkers.api.model.EntityUpdaters;
//
//@Mixin(PlayerRenderer.class)
//public abstract class PlayerEntityDMRendererMixin {
//
//    @Inject(
//            method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
//    private void afterRender(
//            AbstractClientPlayer player,
//            float yaw,
//            float tickDelta,
//            PoseStack poseStack,
//            MultiBufferSource buffer,
//            int light,
//            CallbackInfo ci
//    ) {
//        LivingEntity shape = PlayerShape.getCurrentShape(player);
//        if (shape != null) {
//
//            Vec3 rawDelta = new Vec3(
//                    player.getX() - player.xo,
//                    player.getY() - player.yo,
//                    player.getZ() - player.zo
//            );
//
//            Vec3 serverDelta = rawDelta.scale(1.0F / Math.max(tickDelta, 0.0001F));
//
//            shape.setDeltaMovement(Vec3.ZERO);
//
//
//            EntityUpdater<LivingEntity> entityUpdater = EntityUpdaters.getUpdater((EntityType<LivingEntity>) shape.getType());
//            if (entityUpdater != null) {
//                entityUpdater.update(player, shape);
//            }
//        }
//    }
//
//
//}
//
