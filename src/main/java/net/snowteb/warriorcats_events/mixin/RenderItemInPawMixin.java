package net.snowteb.warriorcats_events.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;
import net.snowteb.warriorcats_events.event.PawRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.walkers.api.PlayerShape;

@Mixin(ItemInHandRenderer.class)
public class RenderItemInPawMixin {

    @Inject(method = "renderItem", at = @At("HEAD"))
    private void renderItemInPaw(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext,
                                 boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int seed,
                                 CallbackInfo ci) {
        if (itemStack.getItem() instanceof CrossbowItem) return;
        if (!(entity instanceof AbstractClientPlayer player)) return;
        if (!(PlayerShape.getCurrentShape(player) instanceof WCatEntity)) return;

        PawRenderer.renderItemInPaw(itemStack, displayContext, leftHand, poseStack);

    }

}
