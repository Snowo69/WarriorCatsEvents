package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.EagleEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EagleRenderer extends GeoEntityRenderer<EagleEntity> {

    public EagleRenderer(EntityRendererProvider.Context context) {
        super(context, new EagleModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public ResourceLocation getTextureLocation(EagleEntity entity) {
        return new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/eagle.png");
    }


    @Override
    public void render(EagleEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}



