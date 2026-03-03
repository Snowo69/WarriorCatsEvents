package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.client.AnimationClientData;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.zconfig.WCEClientConfig;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import tocraft.walkers.api.PlayerShape;

import java.util.HashMap;
import java.util.Map;

public class WCRenderer extends GeoEntityRenderer<WCatEntity> {

    private static final Map<String, ResourceLocation> TEXTURE_CACHE = new HashMap<>();

    private static final ResourceLocation PLAYER_MORPH_TEXTURE =
            new ResourceLocation("warriorcats_events", "textures/hud/player_morph_icon.png");

    public WCRenderer(EntityRendererProvider.Context context) {
        super(context, new WCModel());
        this.addRenderLayer(new WCHeldItemLayer(this));
        this.addRenderLayer(new WCAccesoriesLayer(this, context));
        this.shadowRadius = 0.4F;
        this.shadowStrength = 0.5F;
    }

    /**
     * Fallback.
     * This also sets a texture for each variant.
     */
//    @Override
//    public ResourceLocation getTextureLocation(WCatEntity entity) {
//        int variant = entity.getVariant();
//        return WCModel.TEXTURES[Math.max(0, Math.min(variant, WCModel.TEXTURES.length - 1))];
//    }

    @Override
    public ResourceLocation getTextureLocation(WCatEntity cat) {


        if (!cat.isOnGeneticalSkin()) {
            int variant = cat.getVariant();
            return WCModel.TEXTURES[Math.max(0, Math.min(variant, WCModel.TEXTURES.length - 1))];
        }

        String[] layers = cat.getTextureLayersPaths();

        String key = cat.getCatTextureKey();

        ResourceLocation texture =TEXTURE_CACHE.get(key);

        if (texture == null) {
            LayerTexture skin = new LayerTexture(layers);
            texture = new ResourceLocation(key);
            Minecraft.getInstance().getTextureManager().register(texture, skin);
            TEXTURE_CACHE.put(key, texture);
        }

        return texture;
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

            case 30 -> 0.9f;
            case 31 -> 0.8f;
            case 32 -> 1.0f;
            case 33 -> 0.9f;
            case 34 -> 0.8f;
            case 35 -> 1.0f;
            case 36 -> 0.9f;
            case 37 -> 0.8f;
            case 38 -> 0.9f;
            case 39 -> 0.8f;
            case 40 -> 1.0f;
            case 41 -> 0.9f;
            case 42 -> 1.1f;
            case 43 -> 0.9f;
            case 44 -> 0.8f;
            case 45 -> 0.9f;
            case 46 -> 0.8f;
            case 47 -> 1.1f;
            case 48 -> 0.8f;
            case 49 -> 0.9f;
            case 50 -> 0.8f;
            case 51 -> 1.0f;
            case 52 -> 0.9f;
            default -> 0.8f;
        };

        if (animatable.getSize() > 0.4) {
            scale = animatable.getSize();
        }

        poseStack.scale(scale, scale, scale);
        if (animatable.hasCustomName() && animatable.getCustomName().getString().contains("squished")) {
            poseStack.scale(scale, 0.1f, scale);
            this.shadowRadius = 0.0F;
        } else {
            this.shadowRadius = 0.4f;
            this.shadowStrength = 0.5F;
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

//        if (entity != PlayerShape.getCurrentShape(Minecraft.getInstance().player)) {
//            float ageMoons = entity.getAgeInMoons();
//            float percentage = ageMoons / 12.0F;
//            float scale = (float) (0.4 + (percentage * 0.6));
//            poseStack.scale(scale, scale, scale);
//        } else {
//            if (entity.isBaby()) {
//                poseStack.scale(0.4f, 0.4f, 0.4f);
//            }
//            if (entity.isAppScale() && entity.isBaby()) {
//                poseStack.scale(1.75f, 1.75f, 1.75f);
//            }
//        }

        if (entity.shouldShowMorphName()) {
            if (Minecraft.getInstance().player != null) {
                if (!entity.getPlayerBoundUuid().equals(ClanData.EMPTY_UUID)
                        && entity != PlayerShape.getCurrentShape(Minecraft.getInstance().player) ) {
                    poseStack.pushPose();

                    float size = 16f;
                    int alpha = 255;

                    int color = 0xFFFFFFFF;
                    float yOffset = 0;

                    if (entity.isDiscrete()) {
                        alpha = 50;
                        color = 0x33FFFFFF;
                        yOffset = 0.07f;
                    }

                    poseStack.translate(0.0D, 1.7D - yOffset, 0.0D);
                    poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

                    poseStack.scale(-0.010F, -0.010F, 1F);

                    VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(PLAYER_MORPH_TEXTURE));
                    Matrix4f matrix = poseStack.last().pose();




                    buffer.vertex(matrix, -size, -size, 0)
                            .color(255, 255, 255, alpha)
                            .uv(0, 0)
                            .overlayCoords(OverlayTexture.NO_OVERLAY)
                            .uv2(packedLight)
                            .normal(0, 1, 0)
                            .endVertex();

                    buffer.vertex(matrix, -size, size, 0)
                            .color(255, 255, 255, alpha)
                            .uv(0, 1)
                            .overlayCoords(OverlayTexture.NO_OVERLAY)
                            .uv2(packedLight)
                            .normal(0, 1, 0)
                            .endVertex();

                    buffer.vertex(matrix, size, size, 0)
                            .color(255, 255, 255, alpha)
                            .uv(1, 1)
                            .overlayCoords(OverlayTexture.NO_OVERLAY)
                            .uv2(packedLight)
                            .normal(0, 1, 0)
                            .endVertex();

                    buffer.vertex(matrix, size, -size, 0)
                            .color(255, 255, 255, alpha)
                            .uv(1, 0)
                            .overlayCoords(OverlayTexture.NO_OVERLAY)
                            .uv2(packedLight)
                            .normal(0, 1, 0)
                            .endVertex();

                    String morphName = entity.hasCustomName() ? entity.getCustomName().getString() : "Unnamed cat";
                    poseStack.translate(0.0D, -1.7D, 0.0D);

                    poseStack.scale(2.5f,2.5f,1F);

                    float x = -Minecraft.getInstance().font.width(morphName) / 2f;

                    Minecraft.getInstance().font.drawInBatch(morphName, x, 10, color,
                            false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0x44000000, packedLight);


                    poseStack.popPose();
                }
            }
        }

        if (entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }
        if (entity.isAppScale() && entity.isBaby()) {
            poseStack.scale(1.75f, 1.75f, 1.75f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public RenderType getRenderType(WCatEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }

    @Override
    protected void renderNameTag(WCatEntity pEntity, Component pDisplayName, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

//        if (Minecraft.getInstance().player != null) {
//            if (!pEntity.getPlayerBoundUuid().equals(ClanData.EMPTY_UUID)
//                    && pEntity != PlayerShape.getCurrentShape(Minecraft.getInstance().player)) {
//                pPoseStack.pushPose();
//
//                pPoseStack.translate(0.0D, 1.4D, 0.0D);
//                pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
//
//                pPoseStack.scale(-0.010F, -0.010F, 0F);
//
//                VertexConsumer buffer = pBuffer.getBuffer(RenderType.entityTranslucent(PLAYER_MORPH_TEXTURE));
//                Matrix4f matrix = pPoseStack.last().pose();
//
//
//                float size = 16f;
//                int alpha = 255;
//
//                buffer.vertex(matrix, -size, -size, 0)
//                        .color(255, 255, 255, alpha)
//                        .uv(0, 0)
//                        .overlayCoords(OverlayTexture.NO_OVERLAY)
//                        .uv2(pPackedLight)
//                        .normal(0, 1, 0)
//                        .endVertex();
//
//                buffer.vertex(matrix, -size, size, 0)
//                        .color(255, 255, 255, alpha)
//                        .uv(0, 1)
//                        .overlayCoords(OverlayTexture.NO_OVERLAY)
//                        .uv2(pPackedLight)
//                        .normal(0, 1, 0)
//                        .endVertex();
//
//                buffer.vertex(matrix, size, size, 0)
//                        .color(255, 255, 255, alpha)
//                        .uv(1, 1)
//                        .overlayCoords(OverlayTexture.NO_OVERLAY)
//                        .uv2(pPackedLight)
//                        .normal(0, 1, 0)
//                        .endVertex();
//
//                buffer.vertex(matrix, size, -size, 0)
//                        .color(255, 255, 255, alpha)
//                        .uv(1, 0)
//                        .overlayCoords(OverlayTexture.NO_OVERLAY)
//                        .uv2(pPackedLight)
//                        .normal(0, 1, 0)
//                        .endVertex();
//
//                pPoseStack.popPose();
//            }
//        }

        if (Minecraft.getInstance().player != null) {
            if (!WCEClientConfig.CLIENT.OWN_MORPH_NAME.get() && PlayerShape.getCurrentShape(Minecraft.getInstance().player) == pEntity) {
                return;
            }
        }

        if (!pEntity.getPlayerBoundUuid().equals(ClanData.EMPTY_UUID)) {
            return;
        }

        super.renderNameTag(pEntity, pDisplayName, pPoseStack, pBuffer, pPackedLight);


    }
}



