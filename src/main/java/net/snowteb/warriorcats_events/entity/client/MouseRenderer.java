package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.MouseEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MouseRenderer extends GeoEntityRenderer<MouseEntity> {
    public MouseRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MouseModel());
    }


    @Override
    public ResourceLocation getTextureLocation(MouseEntity animatable) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/mouse.png");
    }

    @Override
    public void render(MouseEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();

        if(entity.isBaby()) {
            poseStack.scale(0.2F, 0.2F, 0.2F);
        } else {
            poseStack.scale(0.4F, 0.4F, 0.4F);
        }


        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }
}
