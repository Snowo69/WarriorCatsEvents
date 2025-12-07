package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.BadgerEntity;
import net.snowteb.warriorcats_events.entity.custom.MouseEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BadgerRenderer extends GeoEntityRenderer<BadgerEntity> {
    public BadgerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BadgerModel());
    }


    @Override
    public ResourceLocation getTextureLocation(BadgerEntity animatable) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/badger/badger_1.png");
    }

    @Override
    public void render(BadgerEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();

        if(entity.isBaby()) {
            poseStack.scale(0.4F, 0.4F, 0.4F);
        } else {
            poseStack.scale(1F, 1F, 1F);
        }


        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }
}
