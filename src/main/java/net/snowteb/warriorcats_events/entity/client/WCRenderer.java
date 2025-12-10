/*package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Parrot;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;

public class WCRenderer extends MobRenderer<WCatEntity, WCRenderer.WCModel> {

    public WCRenderer(EntityRendererProvider.Context context) {
        super(context, new WCModel(context.bakeLayer(ModelLayers.OCELOT)), 0.3F);
    }

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin1.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin2.png"),
            new ResourceLocation("warriorcats_events:textures/entity/wcat/wcskin3.png")
    };


    @Override
    public ResourceLocation getTextureLocation(WCatEntity entity) {
        int variant = entity.getVariant();
        return TEXTURES[Math.max(0, Math.min(variant, TEXTURES.length - 1))];
    }



    // Wrapper para usar OcelotModel como modelo de tu entidad
    public static class WCModel extends OcelotModel<WCatEntity> {

        public WCModel(ModelPart modelPart) {
            super(modelPart);
        }

        @Override
        public void renderToBuffer(
                PoseStack poseStack,
                VertexConsumer buffer,
                int packedLight,
                int packedOverlay,
                float red,
                float green,
                float blue,
                float alpha
        ) {
            super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }


    @Override
    protected void scale(WCatEntity cat, PoseStack poseStack, float partialTickTime) {
        float scale = switch (cat.getVariant()) {
            case 0 -> 0.8f;
            case 1 -> 1.0f;
            case 2 -> 1.3f;
            default -> 1.0f;
        };

        poseStack.scale(scale, scale, scale);
        super.scale(cat, poseStack, partialTickTime);
    }




}*/


package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.snowteb.warriorcats_events.client.AnimationClientData;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import tocraft.walkers.api.PlayerShape;

public class WCRenderer extends GeoEntityRenderer<WCatEntity> {

    public WCRenderer(EntityRendererProvider.Context context) {
        super(context, new WCModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public ResourceLocation getTextureLocation(WCatEntity entity) {
        int variant = entity.getVariant();
        return WCModel.TEXTURES[Math.max(0, Math.min(variant, WCModel.TEXTURES.length - 1))];
    }


    @Override
    public void preRender(PoseStack poseStack, WCatEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float scale = switch (animatable.getVariant()) {
            case 0 -> 0.8f;
            case 1 -> 1.0f;
            case 2 -> 1.0f;
            case 3 -> 0.9f;
            case 4 -> 0.8f;
            case 5 -> 1.0f;
            case 6 -> 1.1f;
            case 7 -> 1.2f;
            case 8 -> 0.8f;
            case 9 -> 1.1f;
            case 10 -> 1.0f;
            case 11 -> 0.9f;
            case 12 -> 0.9f; //chestnutpatch
            case 13 -> 1.0f; //ratstar
            case 14 -> 0.8f; //twitchstream
            case 15 -> 1.1f; //blazepit
            case 16 -> 1.0f; //bengalpelt
            case 17 -> 1.1f; //sparrowstar
            case 18 -> 0.8f; //foxeater
            case 19 -> 1.0f; //willowsong
            default -> 0.8f;
        };


        poseStack.scale(scale, scale, scale);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void render(WCatEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        if (entity == PlayerShape.getCurrentShape(Minecraft.getInstance().player)) {
            AnimationClientData.isPlayerShape = true;
        } else {
            AnimationClientData.isPlayerShape = false;
        }


        if (entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }
        if (entity.isAppScale() && entity.isBaby()){
            poseStack.scale(1.75f, 1.75f, 1.75f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}



