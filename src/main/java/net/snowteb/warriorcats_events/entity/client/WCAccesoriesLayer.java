package net.snowteb.warriorcats_events.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.item.ModItems;
import net.snowteb.warriorcats_events.item.custom.CatSocksArmorItem;
import net.snowteb.warriorcats_events.item.custom.FeathersArmorItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

@OnlyIn(Dist.CLIENT)
public class WCAccesoriesLayer extends GeoRenderLayer<WCatEntity> {
    private final SquirrelAccessoryModel squirrelAccessoryModel = new SquirrelAccessoryModel();

    private final AccessoryModel flowerCrownAccesoryModel = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.flowercrown.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/flowercrown.png")
    );

    private final AccessoryModel leafManeAccesoryModel = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/leaf_mane.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/leaf_mane.png")
    );

    private final AccessoryModel flowerAccesoryModel = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.floweraccessory.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/flower_accessory.png")
    );

    private final AccessoryModel tailVinesModel = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.tail_vine_wrap.geo2.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/green_texture.png")
    );

    private final AccessoryModel socksModel = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.socks.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/cat_socks.png")
    );

    private final AccessoryModel leafShieldModel = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.leaf_shield.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/leaf_shield.png")
    );

    private final AccessoryModel tailLichenModel = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.tail_lichen.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/tail_lichen.png")
    );

    private final AccessoryModel dandelionModel = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.head_dandelion.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/head_dandelion.png")
    );


    private final AccessoryModel bodyFeathersModelUp = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.body_feathers_up.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/chicken_feathers.png")
    );
    private final AccessoryModel bodyFeathersModelMid = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.body_feathers_mid.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/chicken_feathers.png")
    );
    private final AccessoryModel bodyFeathersModelDown = new AccessoryModel(
            new ResourceLocation(WarriorCatsEvents.MODID, "geo/wcat.body_feathers_down.geo.json"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/entity/accessories/chicken_feathers.png")
    );


    private final AccessoryRenderer crownRenderer;
    private final AccessoryRenderer leafmaneRenderer;
    private final AccessoryRenderer flowerAccesoryRenderer;
    private final AccessoryRenderer tailVinesRenderer;
    private final AccessoryRenderer socksRenderer;
    private final AccessoryRenderer leafShieldRenderer;
    private final AccessoryRenderer tailLichenRenderer;
    private final AccessoryRenderer dandelionRenderer;
    private final AccessoryRenderer bodyFeathersRenderer;




    public WCAccesoriesLayer(GeoRenderer<WCatEntity> entityRendererIn, EntityRendererProvider.Context context) {
        super(entityRendererIn);
        this.crownRenderer = new AccessoryRenderer(context, flowerCrownAccesoryModel);
        this.leafmaneRenderer = new AccessoryRenderer(context, leafManeAccesoryModel);
        this.flowerAccesoryRenderer = new AccessoryRenderer(context, flowerAccesoryModel);
        this.tailVinesRenderer = new AccessoryRenderer(context, tailVinesModel);
        this.socksRenderer = new AccessoryRenderer(context, socksModel);
        this.leafShieldRenderer = new AccessoryRenderer(context, leafShieldModel);
        this.tailLichenRenderer = new AccessoryRenderer(context, tailLichenModel);
        this.dandelionRenderer = new AccessoryRenderer(context, dandelionModel);

        this.bodyFeathersRenderer = new AccessoryRenderer(context, bodyFeathersModelUp);
    }

    @Override
    public void renderForBone(PoseStack poseStack, WCatEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        if (bone.getName().equals("bodydown2") && (animatable.hasCustomName() && animatable.getCustomName().getString().contains("squirrellover"))) {
            var bakedModel = squirrelAccessoryModel.getBakedModel(squirrelAccessoryModel.getModelResource(animatable));


            poseStack.pushPose();


            RenderType accessoryRenderType = RenderType.entityCutout(squirrelAccessoryModel.getTextureResource(animatable));

            VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);
            RenderUtils.translateToPivotPoint(poseStack, bone);
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);


            poseStack.translate(0.0D, -0.185D, -0.3D);

            float scale = 0.15F;
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(Axis.XP.rotationDegrees(180f));


            float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);


            poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));
            getRenderer().reRender(
                    bakedModel,
                    poseStack,
                    bufferSource,
                    animatable,
                    accessoryRenderType,
                    accessoryBuffer,
                    partialTick,
                    packedLight,
                    packedOverlay,
                    1f, 1f, 1f, 1f
            );

            poseStack.popPose();
            buffer = bufferSource.getBuffer(renderType);

        }

        boolean hasCrown = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.FLOWER_CROWN.get());
        boolean hasMane = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.LEAF_MANE.get());
        boolean hasFlower = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.HEAD_FLOWER.get());
        boolean hasDandelion = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.HEAD_DANDELION.get());
        boolean hasLeaf = animatable
                .getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.HEAD_LEAF.get());

        boolean hasFlowerArmor = animatable
                .getItemBySlot(EquipmentSlot.CHEST)
                .is(ModItems.FLOWER_ARMOR.get());

        boolean hasTailVines = animatable
                .getItemBySlot(EquipmentSlot.LEGS)
                .is(ModItems.TAIL_VINES.get());
        boolean hasDrapedTailVines = animatable
                .getItemBySlot(EquipmentSlot.LEGS)
                .is(ModItems.DRAPED_TAIL_VINES.get());

        if (bone.getName().equals("head")) {


            if (hasCrown) {
                var bakedModel = flowerCrownAccesoryModel.getBakedModel(flowerCrownAccesoryModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.61D, -0.6D);
                poseStack.mulPose(Axis.XP.rotationDegrees(15f));
                float scale = 0.87f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(flowerCrownAccesoryModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                crownRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            } else if (hasMane) {
                var bakedModel = leafManeAccesoryModel.getBakedModel(leafManeAccesoryModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.50D, -0.349D);
                poseStack.mulPose(Axis.XP.rotationDegrees(-82f));
                float scale = 0.95f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(leafManeAccesoryModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                leafmaneRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            } else if (hasDandelion) {
                var bakedModel = dandelionModel.getBakedModel(dandelionModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.098D, 0.64D, -0.51D);
                poseStack.mulPose(Axis.ZP.rotationDegrees(-92f));
                poseStack.mulPose(Axis.YP.rotationDegrees(25f));
                poseStack.mulPose(Axis.XP.rotationDegrees(5f));

                float scale = 0.8f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(dandelionModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                dandelionRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            } else if (hasFlower) {
                var bakedModel = flowerAccesoryModel.getBakedModel(flowerAccesoryModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(-0.135D, 0.70D, -0.519D);

                float scale = 1.00f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.ZP.rotationDegrees(62f));

                RenderType accessoryRenderType = RenderType.entityCutout(flowerAccesoryModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                flowerAccesoryRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }

        }

        if (bone.getName().equals("tail")) {
            if (hasTailVines) {
                var bakedModel = tailVinesModel.getBakedModel(tailVinesModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.25f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));
//                poseStack.mulPose(Axis.XP.rotationDegrees(15f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 1.82D, -2.00D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailVinesModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                tailVinesRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (bone.getName().equals("tailsub")) {
            if (hasTailVines && !WCGenetics.Bobtail.isBobtail(animatable.getGenetics().bobtail)) {
                var bakedModel = tailVinesModel.getBakedModel(tailVinesModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.25f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 1.82D, -1.00D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailVinesModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                tailVinesRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (bone.getName().equals("tail2")) {
            if (hasTailVines) {
                var bakedModel = tailVinesModel.getBakedModel(tailVinesModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.25f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 1.82D, 0.00D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailVinesModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                tailVinesRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                poseStack.pushPose();

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));
//                poseStack.mulPose(Axis.XP.rotationDegrees(15f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 1.82D, 1.00D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));

                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                tailVinesRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }


        if ((animatable.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof CatSocksArmorItem socks)) {

            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;

            if (socks.equals(ModItems.BLACK_CAT_SOCKS.get())) {
                red = 0.2f;
                green = 0.2f;
                blue = 0.2f;
            } else if (socks.equals(ModItems.WHITE_CAT_SOCKS.get())) {
                red = 1f;
                green = 1f;
                blue = 1f;
            } else if (socks.equals(ModItems.BLUE_CAT_SOCKS.get())) {
                red = 0.6f;
                green = 0.6f;
                blue = 1f;
            } else if (socks.equals(ModItems.GREEN_CAT_SOCKS.get())) {
                red = 0.6f;
                green = 1.0f;
                blue = 0.6f;
            } else if (socks.equals(ModItems.PINK_CAT_SOCKS.get())) {
                red = 0.9f;
                green = 0.6f;
                blue = 1f;
            } else if (socks.equals(ModItems.ORANGE_CAT_SOCKS.get())) {
                red = 1.0f;
                green = 0.6f;
                blue = 0.4f;
            }

            if (bone.getName().equals("front_left_leg2down")) {
                var bakedModel = socksModel.getBakedModel(socksModel.getModelResource(animatable));

                poseStack.pushPose();
                RenderUtils.translateMatrixToBone(poseStack, bone);

                float scale = 0.52f;
                poseStack.scale(scale, scale, scale);



                poseStack.translate(-0.13D, 0.00D, -0.48D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(socksModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                socksRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        red, green, blue, 1f
                );

                buffer = bufferSource.getBuffer(renderType);
                poseStack.popPose();
            }
            if (bone.getName().equals("front_right_legDOWN")) {
                var bakedModel = socksModel.getBakedModel(socksModel.getModelResource(animatable));

                poseStack.pushPose();
                RenderUtils.translateMatrixToBone(poseStack, bone);

                float scale = 0.52f;
                poseStack.scale(scale, scale, scale);



                poseStack.translate(0.13D, 0.00D, -0.48D);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(socksModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                socksRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        red, green, blue, 1f
                );

                buffer = bufferSource.getBuffer(renderType);
                poseStack.popPose();
            }
        }

        if (bone.getName().equals("head")){
            if (hasLeaf) {
                var bakedModel = leafShieldModel.getBakedModel(leafShieldModel.getModelResource(animatable));

                poseStack.pushPose();

                poseStack.translate(0.0D, 0.67D, -0.65D);

                float scale = 0.28f;
                poseStack.scale(scale, scale, scale);


                RenderType accessoryRenderType = RenderType.entityCutout(leafShieldModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                leafShieldRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();
                buffer = bufferSource.getBuffer(renderType);
            }
        }



        if (bone.getName().equals("tail")) {
            if (hasDrapedTailVines) {
                var bakedModel = tailLichenModel.getBakedModel(tailLichenModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.51f;
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));
//                poseStack.mulPose(Axis.XP.rotationDegrees(15f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 0.67D, -0.85D);
//                poseStack.mulPose(Axis.YP.rotationDegrees(90f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailLichenModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                tailLichenRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (bone.getName().equals("tailsub")) {
            if (hasDrapedTailVines && !WCGenetics.Bobtail.isBobtail(animatable.getGenetics().bobtail)) {
                var bakedModel = tailLichenModel.getBakedModel(tailLichenModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.51f;

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 0.67D, -0.50D);
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));


                RenderType accessoryRenderType = RenderType.entityCutout(tailLichenModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                tailLichenRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (bone.getName().equals("tail2")) {
            if (hasDrapedTailVines) {
                var bakedModel = tailLichenModel.getBakedModel(tailLichenModel.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 0.51f;

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                poseStack.translate(0.00D, 0.67D, 0.10D);


                RenderType accessoryRenderType = RenderType.entityCutout(tailLichenModel.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                tailLichenRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();



                buffer = bufferSource.getBuffer(renderType);
            }
        }

        if (animatable.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof FeathersArmorItem featherArmor) {
            bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[5];
            bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[5];
            bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[5];

            if (featherArmor == ModItems.BLUE_PARROT_BODY_FEATHERS.get()) {
                bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[3];
                bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[3];
                bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[3];
            } else if (featherArmor == ModItems.LIGHTBLUE_PARROT_BODY_FEATHERS.get()) {
                bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[4];
                bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[4];
                bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[4];
            } else if (featherArmor == ModItems.GRAY_PARROT_BODY_FEATHERS.get()) {
                bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[1];
                bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[1];
                bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[1];
            } else if (featherArmor == ModItems.GREEN_PARROT_BODY_FEATHERS.get()) {
                bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[2];
                bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[2];
                bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[2];
            } else if (featherArmor == ModItems.RED_PARROT_BODY_FEATHERS.get()) {
                bodyFeathersModelUp.texture = AccessoryModel.FEATHER_TEXTURES[0];
                bodyFeathersModelMid.texture = AccessoryModel.FEATHER_TEXTURES[0];
                bodyFeathersModelDown.texture = AccessoryModel.FEATHER_TEXTURES[0];
            }

            if (bone.getName().equals("moreup")) {
                var bakedModel = bodyFeathersModelUp.getBakedModel(bodyFeathersModelUp.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 1f;
                poseStack.translate(0.00D, -0.01D, 0.00D);

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(0f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                RenderType accessoryRenderType = RenderType.entityCutout(bodyFeathersModelUp.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                bodyFeathersRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
            if (bone.getName().equals("up")) {
                var bakedModel = bodyFeathersModelMid.getBakedModel(bodyFeathersModelMid.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 1f;
                poseStack.translate(0.00D, -0.01D, 0.00D);

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(0f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                RenderType accessoryRenderType = RenderType.entityCutout(bodyFeathersModelMid.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                bodyFeathersRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
            if (bone.getName().equals("bodydown2")) {
                var bakedModel = bodyFeathersModelDown.getBakedModel(bodyFeathersModelDown.getModelResource(animatable));

                poseStack.pushPose();

                float scale = 1f;
                poseStack.translate(0.00D, +0.50D, -0.32D);

                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Axis.XP.rotationDegrees(90f));

                RenderUtils.translateMatrixToBone(poseStack, bone);

                RenderType accessoryRenderType = RenderType.entityCutout(bodyFeathersModelDown.getTextureResource(animatable));

                VertexConsumer accessoryBuffer = bufferSource.getBuffer(accessoryRenderType);

                float interpolatedYaw = Mth.lerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(interpolatedYaw + 180f));


                bodyFeathersRenderer.reRender(
                        bakedModel,
                        poseStack,
                        bufferSource,
                        animatable,
                        accessoryRenderType,
                        accessoryBuffer,
                        partialTick,
                        packedLight,
                        packedOverlay,
                        1f, 1f, 1f, 1f
                );

                poseStack.popPose();

                buffer = bufferSource.getBuffer(renderType);
            }
        }

    }
}
