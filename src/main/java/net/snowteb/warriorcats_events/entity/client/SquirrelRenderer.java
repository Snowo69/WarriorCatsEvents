package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.MouseEntity;
import net.snowteb.warriorcats_events.entity.custom.SquirrelEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SquirrelRenderer extends GeoEntityRenderer<SquirrelEntity> {
    public SquirrelRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SquirrelModel());
    }


    @Override
    public ResourceLocation getTextureLocation(SquirrelEntity animatable) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/squirrel.png");
    }

    @Override
    public void render(SquirrelEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();

        if(entity.isBaby()) {
            poseStack.scale(0.3F, 0.3F, 0.3F);
        } else {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }


        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }
}
