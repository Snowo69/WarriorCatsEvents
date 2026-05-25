package net.snowteb.warriorcats_events.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.snowteb.warriorcats_events.item.custom.MossBallItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class HeldMossBallMixin {

    @Shadow
    private void applyItemArmTransform(PoseStack matrices, HumanoidArm arm, float equipProgress) {
    }

    @Shadow
    public void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
    }


    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/UseAnim;", shift = At.Shift.BEFORE), cancellable = true)
    public void renderBouncyBall(AbstractClientPlayer pPlayer, float pPartialTicks, float pPitch, InteractionHand pHand, float pSwingProgress, ItemStack pStack, float pEquippedProgress, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, CallbackInfo ci) {
        if (pStack.getItem() instanceof MossBallItem ballItem) {
            HumanoidArm arm = pHand == InteractionHand.MAIN_HAND ? pPlayer.getMainArm() : pPlayer.getMainArm().getOpposite();
            this.applyItemArmTransform(pPoseStack, arm, pEquippedProgress);
            ballItem.translateArm(pPoseStack, arm, pStack, pPartialTicks);
            boolean rightArm = arm == HumanoidArm.RIGHT;
            this.renderItem(pPlayer, pStack, rightArm ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !rightArm, pPoseStack, pBuffer, pCombinedLight);
            pPoseStack.popPose();
            ci.cancel();
        }
    }

}
