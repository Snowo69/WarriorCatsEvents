package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
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

    /**
     * Fallback.
     * This also sets a texture for each variant.
     */
    @Override
    public ResourceLocation getTextureLocation(WCatEntity entity) {
        int variant = entity.getVariant();
        return WCModel.TEXTURES[Math.max(0, Math.min(variant, WCModel.TEXTURES.length - 1))];
    }

    /**
     * Depending on the variant, set a different visual size.
     */
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
            case 20 -> 0.9f; //13
            case 21 -> 0.8f; //14
            case 22 -> 1.0f; //15
            case 23 -> 0.9f; //16
            case 24 -> 0.9f; //17
            case 25 -> 1.1f; //18
            case 26 -> 0.8f; //19
            case 27 -> 0.8f; //20
            case 28 -> 1.2f; //21
            case 29 -> 1.0f; //22
            default -> 0.8f;
        };


        poseStack.scale(scale, scale, scale);
        if (animatable.hasCustomName() && animatable.getCustomName().getString().equals("squished")) {
            poseStack.scale(scale, 0.1f, scale);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void render(WCatEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {


        /**
         * If the entity is the entity the player is playing as (The one the player is morphed into), then set isPlayerShape.
         */

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



