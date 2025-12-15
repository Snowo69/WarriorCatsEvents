package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.PigeonEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PigeonRenderer extends GeoEntityRenderer<PigeonEntity> {

    public PigeonRenderer(EntityRendererProvider.Context context) {
        super(context, new PigeonModel());
        this.shadowRadius = 0.4F;
    }

    /**
     * Depending on the variant chosen, make sure it loads does load a different Texture
     */
    @Override
    public ResourceLocation getTextureLocation(PigeonEntity entity) {
        return switch (entity.getPigeonVariant()) {
            case 1 -> new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/pigeon/pigeon_2.png");
            case 2 -> new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/pigeon/pigeon_3.png");
            default -> new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/pigeon/pigeon_1.png");
        };
    }


    @Override
    public void render(PigeonEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        if (entity.isBaby()) {
            poseStack.scale(0.3f, 0.3f, 0.3f);
        }
        else {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}



